package com.jfsb.antwortn.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentConsultBinding

import com.jfsb.antwortn.post.CreatePostDialog
import com.jfsb.antwortn.post.Post
import com.jfsb.antwortn.post.PostAdapter


class ConsultFragment : Fragment() {

    var _binding : FragmentConsultBinding? = null
    val binding get() = _binding!!

    lateinit var rev: RecyclerView
    lateinit var fab: FloatingActionButton
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        rev = binding.rvConsultasFragment
        fab = binding.fabNewConsult

        db.collection("post").addSnapshotListener{value, error ->
            val posts = value!!.toObjects(Post::class.java)

            posts.forEachIndexed{ index, post ->
                post.uid = value.documents[index].id
            }

            //posts.add(2,Post("Texto donde se explica la duda que se tiene", Date(),"Username"))

            rev.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = PostAdapter(this@ConsultFragment,posts)
            }
        }

        fab.setOnClickListener{

            CreatePostDialog().show(requireActivity().supportFragmentManager, "Crear")
            /*
            val intent = Intent (requireContext(), CreateActivity::class.java)
            startActivity(intent)*/
        }

    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}