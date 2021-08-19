package com.jfsb.antwortn.comments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.activities.Utils
import com.jfsb.antwortn.databinding.LayoutCommentBinding
import com.jfsb.antwortn.databinding.LayoutCreateBinding
import com.jfsb.antwortn.post.Post
import com.squareup.picasso.Picasso
import java.util.*

class CreateCommentDialog(postId: String) : DialogFragment(){

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val postId = postId

    private var _binding : LayoutCommentBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = LayoutCommentBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            //alertBuilder.setView(requireActivity().layoutInflater.inflate(R.layout.layout_create,null))

            alertBuilder.setTitle("Comentario")
            alertBuilder.setIcon(R.drawable.antwort_iso)

            alertBuilder.setPositiveButton("Comentar", DialogInterface.OnClickListener { dialog, id ->

                val commentText = binding.etLycommentText.text.toString()
                val date = Date()
                val userName = auth.currentUser!!.displayName!!
                val userId = auth.currentUser!!.uid
                val commentId = db.collection("comment").document().id

                val comment = Comment(commentId, postId, commentText, date, userName, userId)

                db.collection("comment").document(commentId).set(comment)
                    .addOnSuccessListener {
                        val doc = db.collection("post").document(postId)

                        db.runTransaction {
                            it.update(doc, "commentsCount", FieldValue.increment(1))
                            null
                        }
                    }
                    .addOnFailureListener {
                        Utils.showError(requireContext(), it.message.toString())
                    }
            })



            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }

}