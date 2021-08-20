package com.jfsb.antwortn.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentAnswersBinding
import com.jfsb.antwortn.databinding.FragmentNewsBinding
import com.jfsb.antwortn.post.Post
import com.jfsb.antwortn.post.PostAdapter
import com.jfsb.antwortn.social.FriendCard
import java.lang.Exception


class NewsFragment (private val profileId:String) : Fragment() {

    var _binding : FragmentNewsBinding? = null
    val binding get() = _binding!!

    private val db_fire = FirebaseDatabase.getInstance()
    private val db_ref = db_fire.reference
    private val auth = FirebaseAuth.getInstance()

    lateinit var rev: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private var  postsList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rev = binding.rvNews

        db_ref.child("Users").child(auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                val children = dataSnapshot.child("friends")!!.children
                children.forEach {
                    val uid = it.value

                    db.collection("post")
                        .whereEqualTo("userId",uid)
                        .orderBy("date")
                        .addSnapshotListener{   value, error ->
                            val posts = value!!.toObjects(Post::class.java)
                            //postsList!!.addAll(posts)
                            rev.apply {
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(context)
                                adapter = PostAdapter(this@NewsFragment,posts)
                            }
                        }
                }
                } catch (e:Exception){

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}