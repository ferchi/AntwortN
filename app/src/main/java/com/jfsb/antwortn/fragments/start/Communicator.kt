package com.jfsb.antwortn.fragments.start

import android.widget.EditText
import androidx.fragment.app.Fragment

interface Communicator {
    fun changeFragment(fragment: Fragment, animation:Int)
    fun changeFragment(fragment: Fragment)
}