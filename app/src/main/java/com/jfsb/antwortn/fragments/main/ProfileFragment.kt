package com.jfsb.antwortn.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentProfileBinding
import com.jfsb.antwortn.fragments.adapters.ViewPagerAdapter
import com.jfsb.antwortn.fragments.profile.AnswersFragment
import com.jfsb.antwortn.fragments.profile.ConsultFragment
import com.jfsb.antwortn.fragments.profile.NewsFragment
import com.jfsb.antwortn.profileview.CircleImageViewBehavior


class ProfileFragment : Fragment() {
    var _binding : FragmentProfileBinding? = null
    val binding get() = _binding!!

    lateinit var toolbar: Toolbar
    lateinit var appbar: AppBarLayout


    val TAKE_IMG_CODE = 1046
    lateinit var vista: View
    lateinit var storageChild: String
    lateinit var databaseChild: String

    lateinit var nameS: String
    lateinit var usernameS: String
    lateinit var imgProfileS: String
    lateinit var imgBannerS: String
    var miPerfil: Boolean = false
    lateinit var uidS: String
    lateinit var userType: String

    var clickFollow : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolBarLayout)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Implementacion de las propiedades del FAB a un CircleImage
        val params = binding.imageUser.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = CircleImageViewBehavior()

        setUpTabs()

    }
    private fun setUpTabs() {
        val adapter = ViewPagerAdapter((activity as AppCompatActivity?)!!.supportFragmentManager)
        adapter.addFragment(NewsFragment(), "Recientes")
        adapter.addFragment(ConsultFragment(), "Consultas")
        adapter.addFragment(AnswersFragment(), "Respuestas")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_new_24)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_hearing_24)
        binding.tabs.getTabAt(2)!!.setIcon(R.drawable.ic_question_24)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}