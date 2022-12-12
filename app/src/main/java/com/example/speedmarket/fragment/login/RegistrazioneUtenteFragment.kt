package com.example.speedmarket.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentRegistrazioneUtenteBinding
import com.example.speedmarket.fragment.UtilsFragment
import com.example.speedmarket.model.Utente
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class RegistrazioneUtenteFragment : UtilsFragment() {

    lateinit var binding: FragmentRegistrazioneUtenteBinding
    lateinit  var db : FirebaseFirestore

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
        binding.imageTurnBack.setOnClickListener(){
            view.findNavController().navigate(R.id.action_registrazioneUtenteFragment_to_accediFragment)
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
            binding.etNome.text.toString().isEmpty() -> {showErrorSnackbar(view,getString(R.string.messaggio_inserisci_nome),false)
                false}
            binding.etCognome.text.toString().isEmpty() -> {showErrorSnackbar(view,getString(R.string.messaggio_inserisci_cognome),false)
                false}
            binding.etEmail.text.toString().isEmpty()  -> {showErrorSnackbar(view,getString(R.string.messaggio_inserisci_email),false)
                false}
            binding.etPassword.text.toString().isEmpty() -> {showErrorSnackbar(view,getString(R.string.messaggio_inserisci_password),false)
                false}
            binding.etRipetiPassword.text.toString().isEmpty() -> {showErrorSnackbar(view,getString(
                            R.string.messaggio_inserisci_password_conferma),false)
                false}
            binding.etPassword.text.toString() != binding.etRipetiPassword.text.toString() -> {showErrorSnackbar(view,getString(
                            R.string.messaggio_password_diverse),false)
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
                        add_to_database(user)
                        //FirestoreClass().registerUser(this,user)

                        //FirebaseAuth.getInstance().signOut()
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
            getString(R.string.messaggio_registrazione_corretta),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun add_to_database(utente: Utente){

        db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, String> = HashMap()

        user["nome"] = utente.nome
        user["cognome"] = utente.cognome
        user["email"] = utente.email
        user["password"] = utente.password
        user["immagine_profilo"] = utente.immagine_profilo
        user["residenza"] = utente.residenza
        user["numero_telefono"] = utente.numero_telefono.toString()
        user["genere"] = utente.genere
        user["profilo_completo"] = utente.profileCompleted.toString()

        db.collection("Utenti")
            .document(utente.id)
            .set(user)


    }
}