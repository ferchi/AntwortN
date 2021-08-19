package com.jfsb.antwortn.comments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.CardCommentBinding
import com.jfsb.antwortn.social.FriendCard
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

class CommentAdapter (val context: Context, private val dataset: List<Comment>):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    class ViewHolder (val binding: CardCommentBinding) : RecyclerView.ViewHolder(binding.root)

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val db_fire = FirebaseDatabase.getInstance()
    private val db_ref = db_fire.reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val comment = dataset[position]

        val likes = comment.likes!!.toMutableList()
        val unlikes = comment.unlikes!!.toMutableList()

        var liked = likes.contains(auth.uid)
        var uniked = unlikes.contains(auth.uid)


        holder.binding.tvCommentLikes.text = "${(likes.size)}" // "${(likes.size)} likes"
        holder.binding.tvCommentUnlikes.text = "${(unlikes.size)}"
        holder.binding.tvCommentUsername.text = comment.userName
        holder.binding.tvCommentTextcomment.text = comment.text

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")

        holder.binding.tvCommentDate.text = sdf.format(comment.date)
        setColor(liked, holder.binding.civCommentLike)


        holder.binding.civCommentLike.setOnClickListener {
            liked = !liked
            setColor(liked, holder.binding.civCommentLike)

            if (liked) likes.add(auth.uid!!)
            else likes.remove(auth.uid)

            val doc = db.collection("comment").document(comment.commentId!!)

            db.runTransaction {
                it.update(doc, "likes", likes)
                null
            }
        }

        try {
            db_ref.child("Users").child(comment.userId.toString()).addValueEventListener(object :
                ValueEventListener {

                override fun onDataChange(datatwo: DataSnapshot) {

                    val imageProfile = datatwo.child("imgProfile").value.toString()

                    Picasso.get().load(imageProfile).into(holder.binding.civCommentUser)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }catch (e:Exception){
            Picasso.get().load(R.drawable.woman                                                                                                                                                                     ).into(holder.binding.civCommentUser)
        }
    }

    private fun setColor(liked: Boolean, likeButton: CircleImageView){
        if(liked) likeButton.setBackgroundResource(R.drawable.ic_ok_orange_24)
        else likeButton.setBackgroundResource(R.drawable.ic_ok_white_24)
    }
    }