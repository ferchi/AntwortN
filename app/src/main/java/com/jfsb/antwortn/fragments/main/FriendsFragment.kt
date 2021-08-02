package com.jfsb.antwortn.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentFriendsBinding
import com.jfsb.antwortn.databinding.FragmentProfileBinding
import com.jfsb.antwortn.social.FriendAdapter
import com.jfsb.antwortn.social.FriendCard

class FriendsFragment : Fragment() {
    var _binding : FragmentFriendsBinding? = null
    val binding get() = _binding!!

    lateinit var rev: RecyclerView
    private val db = FirebaseDatabase.getInstance()
    private val db_ref = db.reference
    private val auth = FirebaseAuth.getInstance()
    private val friends: MutableList<FriendCard> = mutableListOf()
    private lateinit var friend:FriendCard
    private var friensCount = 0

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {


        _binding = FragmentFriendsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolBarLayout)
        binding.toolBarLayout.title = "Siguiendo"

        rev = binding.rvFriends

        db_ref.child("Users").child(auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val children = dataSnapshot.child("friends")!!.children
                friensCount = dataSnapshot.child("friends")!!.children.count()

                friends.clear()

                children.forEach {
                    val uid = it.value
                    db_ref.child("Users").child(uid.toString()).addValueEventListener(object :
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
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


        //(activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
    companion object {
        @JvmStatic
        fun newInstance() =
            FriendsFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}