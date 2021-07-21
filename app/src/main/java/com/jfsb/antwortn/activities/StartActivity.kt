package com.jfsb.antwortn.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.ActivityStartBinding
import com.jfsb.antwortn.fragments.start.Communicator
import com.jfsb.antwortn.fragments.start.MainFragment

class StartActivity : AppCompatActivity(), Communicator {

    lateinit var binding:ActivityStartBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        changeFragment(MainFragment())

/*
        Picasso.get()
            .load(R.drawable.antwort_logo)
            .resize(500, 500)
            .centerCrop()
            .into(binding.ivLogo)*/
    }

    override fun changeFragment(fragment: Fragment, animation: Int){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(binding.fragmentContainerMain.id,fragment)
        fragmentTransaction.setCustomAnimations(animation,0)
        fragmentTransaction.show(fragment)
        fragmentTransaction.commit()
    }

    override fun changeFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(binding.fragmentContainerMain.id,fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {

        changeFragment(MainFragment(),R.anim.slide_right)
    }
}