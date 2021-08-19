package com.jfsb.antwortn.comments

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
import com.jfsb.antwortn.databinding.LayoutCommentListBinding
import com.jfsb.antwortn.post.Post

class CommentsDialog (val postId:String) : DialogFragment() {

    lateinit var rev: RecyclerView
    private var _binding : LayoutCommentListBinding? = null
    val binding get() = _binding!!

    private val db = FirebaseDatabase.getInstance()
    private val db_store = FirebaseFirestore.getInstance()
    private val db_ref = db.reference
    private val auth = FirebaseAuth.getInstance()
    private val comments: MutableList<Comment> = mutableListOf()
    private lateinit var comment: Comment
    private var commentsCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val alertBuilder = AlertDialog.Builder(it)

            _binding = LayoutCommentListBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            alertBuilder.setTitle("Comentarios")
            alertBuilder.setIcon(R.drawable.antwort_iso)

            rev = binding.rvCommentList

            db_store.collection("comment").whereEqualTo("postId", postId).orderBy("date")
                .addSnapshotListener { value, error ->
                    val comments = value!!.toObjects(Comment::class.java)

                    rev.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context)
                        adapter = CommentAdapter(context,comments)
                    }

/*
                    Log.d("comment", postId)
                    Log.d("comment", "comments " + comments.size.toString())

                    commentsCount = comments.size

                    comments.forEachIndexed { index, comment ->

                        comments.clear()

                        comments.add(comment)
                        listComment()
                    }*/
                    //posts.add(2,Post("Texto donde se explica la duda que se tiene", Date(),"Username"))
                }
            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }

    fun listComment(limit:Int = commentsCount){

        if(comments.size == limit){
            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = CommentAdapter(context,comments)
            }
        }
    }
}