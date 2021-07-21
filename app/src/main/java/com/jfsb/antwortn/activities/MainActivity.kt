package com.jfsb.antwortn.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.ActivityMainBinding
import com.jfsb.antwortn.fragments.main.ExploreFragment
import com.jfsb.antwortn.fragments.main.FriendsFragment
import com.jfsb.antwortn.fragments.main.ProfileFragment
import com.jfsb.antwortn.fragments.main.SearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addFragment(ProfileFragment.newInstance())
        binding.bottomNavigation.show(1)
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
}