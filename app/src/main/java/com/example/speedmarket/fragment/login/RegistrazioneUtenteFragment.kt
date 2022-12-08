package com.example.speedmarket.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.speedmarket.FirestoreClass
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentAccediBinding
import com.example.speedmarket.databinding.FragmentRegistrazioneUtenteBinding
import com.example.speedmarket.fragment.UtilsFragment
import com.example.speedmarket.model.Utente
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegistrazioneUtenteFragment : UtilsFragment() {

    lateinit var binding: FragmentRegistrazioneUtenteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrazioneUtenteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etNome.requestFocus()

        binding.btnRegistrati.setOnClickListener(){

            registerUser(it)
        }
    }
    private fun clear_all(){
        binding.etNome.text?.clear()
        binding.etCognome.text?.clear()
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etRipetiPassword.text?.clear()

    }
    private fun validateRegisterDetails(view : View):Boolean{

        return when{
            binding.etNome.text.toString().isEmpty() -> {showErrorSnackbar(view,"Please enter the first name",false)
                false}
            binding.etCognome.text.toString().isEmpty() -> {showErrorSnackbar(view,"Please enter the second name",false)
                false}
            binding.etEmail.text.toString().isEmpty() -> {showErrorSnackbar(view,"Please enter the emailID",false)
                false}
            binding.etPassword.text.toString().isEmpty() -> {showErrorSnackbar(view,"Please enter the password",false)
                false}
            binding.etRipetiPassword.text.toString().isEmpty() -> {showErrorSnackbar(view,"Please enter the conferm password",false)
                false}
            else ->{ true}
        }
        return true
    }
    private fun registerUser(view:View){

        if( validateRegisterDetails(view)){
            showProgressDialog()
            val email  = binding.etEmail.text.toString().trim(){it <= ' '}
            val password  = binding.etPassword.text.toString().trim(){it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>{ task ->
                    hideProgressDialog()

                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser = task.result!!.user!!
                        val  user = Utente(
                            firebaseUser.uid,
                            binding.etNome.text.toString(),
                            binding.etCognome.text.toString(),
                            email,password
                        )

                        FirestoreClass().registerUser(this,user)

                        FirebaseAuth.getInstance().signOut()
                        clear_all()
                        view.findNavController().navigate(R.id.action_registrazioneUtenteFragment_to_accediFragment)
                    }else{
                        hideProgressDialog()
                        showErrorSnackbar(view,task.exception!!.message.toString(),false) }
                })
        }
    }
    fun userRegistrationSuccess(){
        hideProgressDialog()
        Toast.makeText(
            requireContext(),
            "You are register successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

}