package com.jfsb.antwortn.fragments.adapters

import androidx.appcompat.app.AppCompatActivity
import com.jfsb.antwortn.R

class ToolbarSearchAdapter {
    fun show(activities:AppCompatActivity, title:String, upButton:Boolean){
        activities.setSupportActionBar(activities.findViewById(R.id.toolbarSearch))
        activities.supportActionBar?.title = title
        //activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }
}