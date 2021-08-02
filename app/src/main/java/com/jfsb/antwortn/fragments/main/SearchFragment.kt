package com.jfsb.antwortn.fragments.main

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentProfileBinding
import com.jfsb.antwortn.databinding.FragmentSearchBinding
import com.jfsb.antwortn.fragments.adapters.ToolbarSearchAdapter
import com.jfsb.antwortn.social.FriendAdapter
import com.jfsb.antwortn.social.FriendCard


class SearchFragment : Fragment() {
    var _binding : FragmentSearchBinding? = null
    val binding get() = _binding!!

    private val friends: MutableList<FriendCard> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);

        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ToolbarSearchAdapter().show(activity as AppCompatActivity,"Buscar",true)
        val layoutManager = LinearLayoutManager(context)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        binding.rvSearch.layoutManager = layoutManager


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_context, menu)
        //(activity as AppCompatActivity).menuInflater.inflate(R.menu.menu_context,menu)
        val item: MenuItem? = menu?.findItem(R.id.search_item)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)){
                    searchFriends(query)
                }else{

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!TextUtils.isEmpty(newText)){
                    //searchFriends(newText)
                }else{

                }
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }
 /*   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        (activity as AppCompatActivity).menuInflater.inflate(R.menu.menu_context,menu)
        val item: MenuItem? = menu?.findItem(R.id.search_item)
        val searchView: SearchView = MenuItemCompat.getActionView(item) as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)){
                    searchFriends(query)
                }else{

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!TextUtils.isEmpty(newText)){
                    searchFriends(newText)
                }else{

                }
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_item){
            Toast.makeText(context,"Si jala  opcion 1", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchFriends(searchQuery:String?){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference
        friends.clear()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val children = snapshot!!.child("Users").children

                children.forEach{

                    val name = it.child("name").value.toString()
                    val username = it.child("username").value.toString()
                    val img = it.child("imgProfile").value.toString()
                    val id = it.child("uid").value.toString()
                    val userType = it.child("usertype").value.toString()

                    val modelFriendCard : FriendCard? = FriendCard(img,username,name,id,userType)

                    if(modelFriendCard?.fullName?.toLowerCase()?.contains(searchQuery?.toLowerCase().toString()) == true
                        || modelFriendCard?.userName?.toLowerCase()?.contains(searchQuery?.toLowerCase().toString()) == true){

                        friends.add(modelFriendCard)
                    }

                    val friendAdapter = FriendAdapter(requireContext(),friends)
                    binding.rvSearch.adapter = friendAdapter
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}