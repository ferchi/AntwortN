package com.jfsb.antwortn.fragments.start

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jfsb.antwortn.activities.MainActivity
import com.jfsb.antwortn.activities.StartActivity
import com.jfsb.antwortn.activities.Utils
import com.jfsb.antwortn.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    private lateinit var mAuth: FirebaseAuth

    private lateinit var txt_username:String
    private lateinit var txt_password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            txt_username = binding.etEmailLogin.text.toString()
            txt_password = binding.etPasswordLogin.text.toString()

            if (txt_username.isNotEmpty() && txt_password.isNotEmpty()){
                logUser()
            }
            else{
                Toast.makeText(requireContext(),"Favor de ingresar todos los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logUser(){
        mAuth.signInWithEmailAndPassword(txt_username,txt_password).addOnCompleteListener(){ task ->
            if(task.isSuccessful){
                startApp()
                (activity as AppCompatActivity?)!!.finish()
            } else{
                Toast.makeText(requireContext(),"Verificar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startApp(){
        val intentProfile = Intent(context, MainActivity::class.java).apply {}
        startActivity(intentProfile)
       // Utils.openProfile(FirebaseDatabase.getInstance().reference, "Users", mAuth.currentUser!!.uid, requireActivity(), MainActivity(), true)
    }

}
