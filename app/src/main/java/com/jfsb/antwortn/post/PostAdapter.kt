package com.jfsb.antwortn.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.comments.CommentsDialog
import com.jfsb.antwortn.comments.CreateCommentDialog
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

        val post = dataset[position]
        val likes = post.likes!!.toMutableList()
        var liked = likes.contains(auth.uid)

        holder.binding.titleTextView.text = post.title
        holder.binding.tvPostLikes.text = "${(likes.size)}" // "${(likes.size)} likes"
        holder.binding.tvPostAuthor.text = post.userName
        holder.binding.tvPostContent.text = post.post
        holder.binding.tvPostComments.text = post.commentsCount.toString()

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")

        holder.binding.tvPostDate.text = sdf.format(post.date)
        setLike(liked, holder.binding.civPostLikes)

        holder.binding.tvPostLikes.setOnClickListener{
            LikersDialog(post.postId.toString()).show(fragment.requireActivity().supportFragmentManager, "Crear")
        }
        holder.binding.textView3.setOnClickListener{
            LikersDialog(post.postId.toString()).show(fragment.requireActivity().supportFragmentManager, "Crear")
        }

        holder.binding.tvPostComments.setOnClickListener {
            CommentsDialog(post.postId.toString()).show(fragment.requireActivity().supportFragmentManager, "Comentarios")
        }

        holder.binding.textView4.setOnClickListener {
            CommentsDialog(post.postId.toString()).show(fragment.requireActivity().supportFragmentManager, "Comentarios")
        }

        holder.binding.civPostLikes.setOnClickListener {
            liked = !liked
            setLike(liked, holder.binding.civPostLikes)

            if (liked) likes.add(auth.uid!!)
            else likes.remove(auth.uid)

            val doc = db.collection("post").document(post.postId!!)

            db.runTransaction {
                it.update(doc, "likes", likes)
                null
            }
        }

        holder.binding.civPostComments.setOnClickListener {
            CreateCommentDialog(post.postId!!).show(fragment.requireActivity().supportFragmentManager, "Comentar")
        }

        val isExpanded: Boolean = post.isExpanded()
        holder.binding.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.binding.titleTextView.setOnClickListener {
            post.setExpanded(!post.isExpanded())
            notifyItemChanged(position)
        }

    }

    private fun setLike(liked: Boolean, likeButton: CircleImageView){
        if(liked) likeButton.setBackgroundResource(R.drawable.ic_ok_orange_24)
        else likeButton.setBackgroundResource(R.drawable.ic_ok_white_24)
    }

}