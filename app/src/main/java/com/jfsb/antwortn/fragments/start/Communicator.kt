package com.jfsb.antwortn.fragments.start

import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

interface Communicator {
    fun changeFragment(fragment: Fragment, animation:Int)
    fun changeFragment(fragment: Fragment)


}