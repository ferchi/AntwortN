package com.jfsb.antwortn.post

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.activities.Utils
import com.jfsb.antwortn.databinding.ActivityCreateBinding
import java.util.*

class CreateActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.publicarBtn.setOnClickListener{
            val postString = binding.postText.text.toString()
            val date = Date()
            val userName = auth.currentUser!!.displayName!!
            val userId = auth.currentUser!!.uid

            Log.d("prueba",date.toString())
            Log.d("prueba",userName)

            val post = Post(postString, date, userName ,userId)

            db.collection("post").add(post)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener{
                    Utils.showError(this, it.message.toString())
                }
        }
    }
}