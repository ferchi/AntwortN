package com.jfsb.antwortn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.ActivityMainBinding
import com.jfsb.antwortn.fragments.main.ExploreFragment
import com.jfsb.antwortn.fragments.main.FriendsFragment
import com.jfsb.antwortn.fragments.main.ProfileFragment
import com.jfsb.antwortn.fragments.main.SearchFragment
import com.jfsb.antwortn.activities.Utils.openProfile


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addFragment(ProfileFragment.newInstance())
        binding.bottomNavigation.show(0)
        binding.bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.ic_profile_24))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.ic_friends_24))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.ic_search_24))
        binding.bottomNavigation.add(MeowBottomNavigation.Model(3,R.drawable.ic_explore_24))

        binding.bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    replaceFragment(ProfileFragment.newInstance())
                }
                1 -> {
                    replaceFragment(FriendsFragment.newInstance())
                }
                2 -> {
                    replaceFragment(SearchFragment.newInstance())
                }
                3 -> {
                    replaceFragment(ExploreFragment.newInstance())
                }
                else -> {
                    replaceFragment(ProfileFragment.newInstance())
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    override fun onBackPressed() {
        val intentExit = Intent(this, StartActivity::class.java).apply {}
        mAuth.signOut()
        startActivity(intentExit)
        finish()
    }
}