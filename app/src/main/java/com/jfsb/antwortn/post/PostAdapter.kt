package com.jfsb.antwortn.post

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.CardPostBinding
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat


class PostAdapter (private val fragment: Fragment, private val dataset: List<Post>):RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    class ViewHolder (val binding: CardPostBinding) : RecyclerView.ViewHolder(binding.root)

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post =  dataset[position]
        val likes = post.likes!!.toMutableList()
        var liked = likes.contains(auth.uid)

        holder.binding.tvPostLikes.text = "${(likes.size)}" // "${(likes.size)} likes"
        holder.binding.tvPostAuthor.text = post.userName
        holder.binding.tvPostContent.text = post.post

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")

        holder.binding.tvPostDate.text = sdf.format(post.date)
        setColor(liked, holder.binding.civPostLikes)

        holder.binding.civPostLikes.setOnClickListener{
            liked = !liked
            setColor(liked, holder.binding.civPostLikes)

            if(liked) likes.add(auth.uid!!)
            else likes.remove(auth.uid)

            val doc = db.collection("post").document(post.uid!!)

            db.runTransaction{
                 it.update(doc,"likes",likes)
                null
            }
        }

        holder.binding.civPostComments.setOnClickListener{
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,post.post)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            fragment.startActivity(shareIntent)
        }
    }

    private fun setColor(liked: Boolean, likeButton: CircleImageView){
        if(liked) likeButton.setBackgroundResource(R.drawable.ic_ok_orange_24)
        else likeButton.setBackgroundResource(R.drawable.ic_ok_grey_24)
    }
}