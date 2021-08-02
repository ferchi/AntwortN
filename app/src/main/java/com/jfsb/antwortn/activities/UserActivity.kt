package com.jfsb.antwortn.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.ActivityStartBinding
import com.jfsb.antwortn.databinding.ActivityUserBinding
import com.jfsb.antwortn.fragments.adapters.ViewPagerAdapter
import com.jfsb.antwortn.fragments.profile.AnswersFragment
import com.jfsb.antwortn.fragments.profile.ConsultFragment
import com.jfsb.antwortn.fragments.profile.NewsFragment
import com.jfsb.antwortn.profileview.CircleImageViewBehavior
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding

    lateinit var toolbar: Toolbar
    lateinit var appbar: AppBarLayout

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userDB_ref: DatabaseReference

    val TAKE_IMG_CODE = 1046
    lateinit var vista: View
    lateinit var storageChild: String
    lateinit var databaseChild: String

    lateinit var nameS: String
    lateinit var usernameS: String
    lateinit var imgProfileS: String
    lateinit var imgBannerS: String
    var miPerfil: Boolean = false
    lateinit var uidS: String
    lateinit var userType: String

    var clickFollow : Boolean = false

    @SuppressLint("UseCompatLoadingForDrawables")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        nameS = intent.getStringExtra("name").toString()
        usernameS = intent.getStringExtra("username").toString()
        uidS = intent.getStringExtra("uid").toString()
        imgProfileS = intent.getStringExtra("imgProfile").toString()
        imgBannerS = intent.getStringExtra("imgBanner").toString()
        miPerfil = intent.getBooleanExtra("miperfil", false)
        userType = intent.getStringExtra("usertype").toString()


        //Instancia de los elementos dentro del layout
        toolbar = findViewById(R.id.toolBarLayout)
        appbar = findViewById(R.id.appbarLayout)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        userDB_ref = FirebaseDatabase.getInstance().reference

        binding.tvFullname.text = nameS
        binding.tvUsername.text = usernameS

        //Implementacion de las propiedades del FAB a un CircleImage
        val params = binding.imageUser.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = CircleImageViewBehavior()

        //Metodo para habilitar el uso de Tabs dentro del Layout
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
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(NewsFragment(), "Recientes")
        adapter.addFragment(ConsultFragment(), "Consultas")
        adapter.addFragment(AnswersFragment(), "Respuestas")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_new_24)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_hearing_24)
        binding.tabs.getTabAt(2)!!.setIcon(R.drawable.ic_question_24)
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

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, TAKE_IMG_CODE)
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMG_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
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
                Toast.makeText(this, "Actualización exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Actualización fallida", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addFriend() {
        userDB_ref.child("Users").child(mAuth.currentUser!!.uid).child("friends").child(uidS).setValue(uidS)
    }
    private fun removeFriend() {
        userDB_ref.child("Users").child(mAuth.currentUser!!.uid).child("friends").child(uidS).removeValue()
    }

    private fun isFriend(){
        userDB_ref.child("Users").child(mAuth.currentUser!!.uid).child("friends").addValueEventListener(object :
            ValueEventListener {
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

}