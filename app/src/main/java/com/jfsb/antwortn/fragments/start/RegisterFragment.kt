package com.jfsb.antwortn.fragments.start

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jfsb.antwortn.R
import com.jfsb.antwortn.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding:FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var communicator: Communicator

    //Declarar variables que almacenarán el texto de los EditText
    var txtusername:String = ""
    var txtemail:String = ""
    var txtname:String = ""
    var txtusertype:String = ""
    var txtpassword:String = ""
    var txtRepassword:String = ""

    //Declarar el objeto que será la instancia de la base de datos
    lateinit var oAuth: FirebaseAuth
    lateinit var userDB_ref: DatabaseReference
    lateinit var userDB: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Instanciar la base de datos
        oAuth = FirebaseAuth.getInstance()
        userDB = FirebaseDatabase.getInstance()
        userDB_ref = userDB.getReference("Users")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swRegistro.setOnCheckedChangeListener{_,isChecked ->
            txtusertype = if (isChecked){
                "maestro"
            } else{
                "estudiante"
            }
        }

        binding.btnRegistroRegistro.setOnClickListener {

            txtusername = binding.etRegisterUsername.text.toString()
            txtemail = binding.etRegisterEmail.text.toString()
            txtpassword = binding.etRegisterPassword.text.toString()
            txtRepassword = binding.etRegisterRepassword.text.toString()
            txtname = binding.etRegisterFullname.text.toString()

            if(txtusername.isNotEmpty()&&txtemail.isNotEmpty()&&txtpassword.isNotEmpty()){
                if (txtRepassword==txtpassword){
                if (txtpassword.length >= 6) {
                    oAuth.createUserWithEmailAndPassword(txtemail,txtpassword).addOnCompleteListener(requireActivity()){ task ->
                        if (task.isSuccessful) {
                            val id = oAuth.currentUser!!.uid

                            //Mapeo de los datos del usuario
                            var map:HashMap<String, String> = HashMap()

                            map["uid"] = id
                            map["name"] = txtname
                            map["username"] = txtusername
                            map["email"] = txtemail
                            map["imgProfile"] = ""
                            map["imgBanner"] = ""
                            map["usertype"] = txtusertype

                            userDB_ref.child(id).setValue(map)


                            val profile = UserProfileChangeRequest.Builder()
                                .setDisplayName(txtusername)
                                .build()

                            task.addOnSuccessListener {
                                it.user!!.updateProfile(profile)
                            }


                            Toast.makeText(context,"Registro completo", Toast.LENGTH_SHORT).show()
                            communicator = activity as Communicator
                            communicator.changeFragment(LoginFragment(), R.anim.slide_left)

                        } else {
                            Toast.makeText(context,"No se pudo registrar este usuario "+task.result,
                                Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                else {
                    Toast.makeText(context,"La contraseña debe ser de mínimo seis caracteres",
                        Toast.LENGTH_SHORT).show()
                }
            }
                else{
                    Toast.makeText(context,"La contraseña no ha sido validada correctamente", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(context,"Favor de llenar todos los datos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}