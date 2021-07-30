package com.jfsb.antwortn.fragments.main

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jfsb.antwortn.R
import com.jfsb.antwortn.activities.MainActivity
import com.jfsb.antwortn.activities.Utils
import com.jfsb.antwortn.databinding.FragmentProfileBinding
import com.jfsb.antwortn.fragments.adapters.ViewPagerAdapter
import com.jfsb.antwortn.fragments.profile.AnswersFragment
import com.jfsb.antwortn.fragments.profile.ConsultFragment
import com.jfsb.antwortn.fragments.profile.NewsFragment
import com.jfsb.antwortn.profileview.CircleImageViewBehavior
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.ArrayList


class ProfileFragment : Fragment() {
    var _binding : FragmentProfileBinding? = null
    val binding get() = _binding!!
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB_ref: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Variables que contienen la informacion del usuario en la vista actual
    lateinit var nameS: String
    lateinit var usernameS: String
    lateinit var imgProfileS: String
    lateinit var imgBannerS: String
    var miPerfil: Boolean = true
    lateinit var uidS: String
    lateinit var userType: String

    var clickFollow : Boolean = false

    // Variables para la obtencion y cambio de las imagenes de perfil
    val TAKE_IMG_CODE = 1046
    lateinit var vista: View
    lateinit var storageChild: String
    lateinit var databaseChild: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolBarLayout)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getDataPofile(FirebaseDatabase.getInstance().reference, "Users", mAuth.currentUser!!.uid)
    }

    private fun setUpTabs() {
        //val adapter = ViewPagerAdapter((activity as AppCompatActivity?)!!.supportFragmentManager)

        //Utilizar el childFragmentManager para evitar errores dentro del tab layout
        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(NewsFragment(), "Recientes")
        adapter.addFragment(ConsultFragment(), "Consultas")
        adapter.addFragment(AnswersFragment(), "Respuestas")

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_new_24)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_hearing_24)
        binding.tabs.getTabAt(2)!!.setIcon(R.drawable.ic_question_24)
    }

    fun getDataPofile(mDatabase: DatabaseReference, child:String, user:String) {

        mDatabase.child(child).child(user).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                usernameS = dataSnapshot.child("username").value.toString()
                nameS = dataSnapshot.child("name").value.toString()
                imgProfileS = dataSnapshot.child("imgProfile").value.toString()
                imgBannerS = dataSnapshot.child("imgBanner").value.toString()
                uidS = dataSnapshot.child("uid").value.toString()
                userType = dataSnapshot.child("usertype").value.toString()

                binding.tvFullname.text = nameS
                binding.tvUsername.text = usernameS

                //Implementacion de las propiedades del FAB a un CircleImage
                val params = binding.imageUser.layoutParams as CoordinatorLayout.LayoutParams
                params.behavior = CircleImageViewBehavior()

                setUpTabs()

                loadImg()
                isFriend()

                if (!miPerfil) {
                    binding.tvUsername.setOnClickListener {
                        if(!clickFollow){
                            Log.d("seguir", "añadir")
                            addFriend()
                            clickFollow = !clickFollow
                        } else {
                            Log.d("seguir", "eliminar")
                            removeFriend()
                            clickFollow = !clickFollow
                        }
                    }
                }
                else{
                    binding.imageUser.setOnLongClickListener {
                        changeImg(it)
                    }
                    binding.imageBanner.setOnLongClickListener {
                        changeImg(it)
                    }
                }

                if(userType == "maestro"){
                    Picasso.get().load(R.drawable.ic_teacher_24).into(binding.usertypeImg)
                }
                else{
                    Picasso.get().load(R.drawable.ic_student_24).into(binding.usertypeImg)
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    fun loadImg() {
        userDB_ref.child("Users").child(uidS).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val urlImg = dataSnapshot.child("imgProfile").value.toString()
                val urlImgB = dataSnapshot.child("imgBanner").value.toString()
                try {
                    Picasso.get().load(urlImg).into(binding.imageUser)
                    Picasso.get().load(urlImgB).into(binding.imageBanner)
                } catch (e: Exception) {
                    Picasso.get().load(R.drawable.woman).into(binding.imageUser)
                    Picasso.get().load(R.drawable.campo).into(binding.imageBanner)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun changeImg(view: View): Boolean {
        when (view.id) {
            binding.imageUser.id -> {
                vista = binding.imageUser
                databaseChild = "imgProfile"
                storageChild = "profileImages"
            }
            binding.imageBanner.id -> {
                vista = binding.imageBanner
                databaseChild = "imgBanner"
                storageChild = "bannerImages"
            }
        }

        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, TAKE_IMG_CODE)
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMG_CODE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)
                    if (vista.id == binding.imageUser.id) {
                        binding.imageUser.setImageBitmap(bitmap)
                    } else {
                        binding.imageBanner.setImageBitmap(bitmap)
                    }
                    handleUpload(bitmap)
                }
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uid: String = uidS
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child(storageChild)
            .child("$uid.jpeg")

        ref.putBytes(baos.toByteArray())
            .addOnSuccessListener {
                getDownloadUrl(ref)
            }
            .addOnFailureListener() {
                Log.e("Errorimg", "onFailure", it.cause)
            }
    }

    private fun getDownloadUrl(ref: StorageReference) {
        ref.downloadUrl.addOnSuccessListener {
            setUserProfileUrl(it)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val request: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setPhotoUri(uri)
            .build()

        user.updateProfile(request)
            .addOnSuccessListener {
                userDB_ref.child("Users").child(uidS).child(databaseChild).setValue(uri.toString())
                loadImg()
                Toast.makeText(context, "Actualización exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Actualización fallida", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addFriend() {
        userDB_ref.child("Users").child(mAuth.currentUser!!.uid).child("friends").child(uidS).setValue(uidS)
    }

    private fun removeFriend() {
        userDB_ref.child("Users").child(mAuth.currentUser!!.uid).child("friends").child(uidS).removeValue()
    }

    private fun isFriend(){
        userDB_ref.child("Users").child(mAuth.currentUser!!.uid).child("friends").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.hasChild(uidS)){
                    binding.tvUsername.background = resources.getDrawable(R.drawable.rounded_corners_or)
                    clickFollow = true
                }else{
                    binding.tvUsername.background = resources.getDrawable(R.drawable.rounded_corners_bl)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}