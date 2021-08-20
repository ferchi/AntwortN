package com.jfsb.antwortn.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentAnswersBinding
import com.jfsb.antwortn.databinding.FragmentConsultBinding
import com.jfsb.antwortn.post.CreatePostDialog
import com.jfsb.antwortn.post.Post
import com.jfsb.antwortn.post.PostAdapter


class AnswersFragment (private val profileId:String) : Fragment() {

    var _binding : FragmentAnswersBinding? = null
    val binding get() = _binding!!

    lateinit var rev: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnswersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rev = binding.rvAnswers

        db.collection("post").whereEqualTo("userId",profileId).whereGreaterThan("commentsCount",0).orderBy("commentsCount").orderBy("date").addSnapshotListener{value, error ->
            val posts = value!!.toObjects(Post::class.java)

            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostAdapter(this@AnswersFragment,posts)
            }
        }

    }
}