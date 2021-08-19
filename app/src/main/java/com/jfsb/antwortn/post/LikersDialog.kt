package com.jfsb.antwortn.post

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.LayoutLikersBinding
import com.jfsb.antwortn.social.FriendAdapter
import com.jfsb.antwortn.social.FriendCard

class LikersDialog (val postId:String) : DialogFragment() {

    lateinit var rev: RecyclerView
    private var _binding : LayoutLikersBinding? = null
    val binding get() = _binding!!

    private val db = FirebaseDatabase.getInstance()
    private val db_store = FirebaseFirestore.getInstance()
    private val db_ref = db.reference
    private val auth = FirebaseAuth.getInstance()
    private val friends: MutableList<FriendCard> = mutableListOf()
    private lateinit var friend:FriendCard
    private var friensCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val alertBuilder = AlertDialog.Builder(it)

            _binding = LayoutLikersBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            alertBuilder.setTitle("Me gusta")
            alertBuilder.setIcon(R.drawable.antwort_iso)

            rev = binding.rvLikers

            db_store.collection("post").whereEqualTo("postId",postId).orderBy("date").addSnapshotListener{value, error ->
                val posts = value!!.toObjects(Post::class.java)

                Log.d("post", postId)
                Log.d("post", "posts "+ posts.size.toString())

                posts.forEachIndexed{ index, post ->

                    friensCount = posts.size
                    friends.clear()

                    post.likes!!.forEachIndexed{ indice, liker ->
                        Log.d("post", "likes "+post.likes.size.toString())
                        val uid = liker

                        Log.d("post", "liker1 $uid")

                        db_ref.child("Users").child(uid).addValueEventListener(object :
                            ValueEventListener {

                            override fun onDataChange(datatwo: DataSnapshot) {

                                val imageProfile = datatwo.child("imgProfile").value.toString()
                                val username = datatwo.child("username").value.toString()
                                val name = datatwo.child("name").value.toString()
                                val id = datatwo.child("uid").value.toString()
                                val userType = datatwo.child("usertype").value.toString()

                                friend = FriendCard(imageProfile, username, name, id, userType)

                                listFriend()
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
                //posts.add(2,Post("Texto donde se explica la duda que se tiene", Date(),"Username"))
            }

            alertBuilder.create()

    } ?: throw IllegalStateException("Exception !! Activity is null")
    }

    fun listFriend(limit:Int = friensCount){
        friends.add(friend)

        if(friends.size == limit){
            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = FriendAdapter(context,friends)
            }
        }
    }
}