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
        //val adapter = ViewPagerAdapter((activity as AppCompatActivity?)!!.supportFragmentManager)

        //Utilizar el childFragmentManager para evitar errores dentro del tab layout
        val adapter = ViewPagerAdapter(childFragmentManager)

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