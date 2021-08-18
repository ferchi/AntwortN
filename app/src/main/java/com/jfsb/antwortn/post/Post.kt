package com.jfsb.antwortn.post;
import com.google.firebase.database.Exclude
import java.util.*;

class Post(var postId: String? = null, val title: String? = null, val post:String? = null, val date: Date? = null, val userName: String? = null, val userId: String? = null, val likes: ArrayList<String>? = arrayListOf()){
    @Exclude
    @set:Exclude
    @get:Exclude
    //var uid: String? = null
    private var expanded = false


    fun isExpanded(): Boolean {
        return expanded
    }

    fun setExpanded(expand: Boolean) {
        expanded = expand
    }
    constructor(): this(null,null,null,null,null,null, null)
}