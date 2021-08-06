package com.jfsb.antwortn.post

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalStateException
import com.jfsb.antwortn.R
import com.jfsb.antwortn.activities.Utils
import com.jfsb.antwortn.databinding.FragmentConsultBinding
import com.jfsb.antwortn.databinding.LayoutCreateBinding
import java.util.*

class CreatePostDialog : DialogFragment(){
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var _binding : LayoutCreateBinding? = null
    val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = LayoutCreateBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            //alertBuilder.setView(requireActivity().layoutInflater.inflate(R.layout.layout_create,null))
            alertBuilder.setPositiveButton("Submit",DialogInterface.OnClickListener { dialog, id ->
                val title = binding.etCreateTitle.text.toString()
                val postString = binding.postText.text.toString()
                val date = Date()
                val userName = auth.currentUser!!.displayName!!
                val userId = auth.currentUser!!.uid

                Log.d("prueba", date.toString())
                Log.d("prueba", userName)

                val post = Post(title, postString, date, userName, userId)

                db.collection("post").add(post)
                    .addOnSuccessListener {
                        //finish()
                    }
                    .addOnFailureListener {
                        Utils.showError(requireContext(), it.message.toString())
                    }
            })


            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }

}