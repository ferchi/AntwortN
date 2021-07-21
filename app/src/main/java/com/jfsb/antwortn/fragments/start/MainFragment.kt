package com.jfsb.antwortn.fragments.start

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jfsb.antwortn.R
import com.jfsb.antwortn.adapters.SliderAdapter
import com.jfsb.antwortn.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var communicator: Communicator

    var imageContainer: ViewPager2? = null
    var adapter: SliderAdapter? = null
    lateinit var colors: IntArray
    val titles = arrayOfNulls<String>(3)
    lateinit var dots: Array<TextView?>
    var layout: LinearLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageContainer = binding.viewPagerMain
        layout = binding.dotsContainer

        dots = arrayOfNulls(3)
        colors = IntArray(3)

        colors[0] = resources.getColor(R.color.orange_1)
        colors[1] = resources.getColor(R.color.orange_2)
        colors[2] = resources.getColor(R.color.orange_3)

        titles[0] = resources.getString(R.string.info_one)
        titles[1] = resources.getString(R.string.info_two)
        titles[2] = resources.getString(R.string.info_three)

        adapter = SliderAdapter(colors, titles)
        imageContainer!!.adapter = adapter

        setIndicators()

        imageContainer!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedDots(position)
                super.onPageSelected(position)
            }
        })

        communicator = activity as Communicator
        binding.btnMainLogin.setOnClickListener {
            communicator.changeFragment(LoginFragment(),R.anim.slide_left)
        }
        binding.btnMainRegistro.setOnClickListener {
            communicator.changeFragment(RegisterFragment(),R.anim.slide_right)
        }

    }

    private fun selectedDots(position: Int) {
        for (i in dots.indices) {
            if (i == position) {
                dots[i]?.setTextColor(colors[position])
            } else {
                dots[i]?.setTextColor(resources.getColor(R.color.grey))
            }
        }
    }

    private fun setIndicators() {
        for (i in dots.indices) {
            dots[i] = TextView(context)
            dots[i]?.text = Html.fromHtml("&#9679;")
            dots[i]?.textSize = 18f
            layout!!.addView(dots[i])
        }
    }

}