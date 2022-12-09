package com.example.speedmarket.fragment.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.activity.HomeActivity
import com.example.speedmarket.activity.LoginActivity
import com.example.speedmarket.activity.MainActivity
import com.example.speedmarket.databinding.ActivityMainBinding
import com.example.speedmarket.databinding.FragmentAccediBinding
import com.example.speedmarket.fragment.UtilsFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class AccediFragment : UtilsFragment() {

    companion object {

        private const val TAG = "GoogleActivity"
        private val RC_SIGN_IN = 89
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var binding: FragmentAccediBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccediBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
        auth = FirebaseAuth.getInstance()

        binding.etEmail.requestFocus()
       /* binding.txtVRegister.setOnClickListener() {

            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener() {
            loginRegisterUser(it)
        }*/
        binding.btnRecuperaPassword.setOnClickListener() {
            recupera_password()
        }
        binding.btnRegistrati.setOnClickListener(){
            view.findNavController().navigate(R.id.action_accediFragment_to_registrazioneUtenteFragment)
        }
        binding.btnAccedi.setOnClickListener() {
            loginRegisterUser(it)
        }
        binding.imageGoogleIcon.setOnClickListener(){
            signIn()
        }

    }
    private fun recupera_password(){
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val chiudi = dialog.findViewById<ImageView>(R.id.cancelId)
        val invia = dialog.findViewById<Button>(R.id.btnInvia)
        chiudi.setOnClickListener() {
            dialog.dismiss()
        }
        invia.setOnClickListener(){
            val email_da_recuperare = dialog.findViewById<EditText>(R.id.etEmailrecuperaPassword)
            val email : String = email_da_recuperare.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                showErrorSnackbar(it,getString(R.string.messaggio_email),false)
            }else{
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{
                            task->
                        hideProgressDialog()
                        if(task.isSuccessful){
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.messaggio_invio_riuscito_email),
                                Toast.LENGTH_LONG)
                                .show()
                            dialog.dismiss()
                        }else{
                            showErrorSnackbar(it,task.exception!!.message.toString(),false)
                        }
                    }
            }
        }
    }
    private fun validateLoginDetails(view: View):Boolean{

        return when{
            binding.etEmail.text.toString().isEmpty() -> {showErrorSnackbar(view,getString(R.string.messaggio_email),false)
                false}
            binding.etPassword.text.toString().isEmpty() -> {showErrorSnackbar(view,getString(R.string.messaggio_inserisci_password),false)
                false}
            else ->{
                true}
        }
        true
    }
    private fun loginRegisterUser(view : View){
        if(validateLoginDetails(view)){
            showProgressDialog()

            val email = binding.etEmail.text.toString().trim{it <= ' '}
            val password = binding.etPassword.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                        task ->
                    hideProgressDialog()
                    if(task.isSuccessful){

                       /* showErrorSnackbar(view,"You are logged in successfully!",
                            true)*/
                        startActivity(Intent(requireContext(), HomeActivity::class.java))

                    }else{
                        showErrorSnackbar(view,task.exception!!.message.toString(),false) }
                }

        }

    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                view?.let { firebaseAuthWithGoogle(it,account.idToken!!) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(view:View,idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        showProgressDialog()
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    hideProgressDialog()
                    //Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val email = user?.email
                   /* showErrorSnackbar(view,"You are logged in successfully!",
                        true)*/
                    googleSignInClient.signOut()
                   /* val intent = Intent(this.activity, MainActivity::class.java)
                    // Error: "Please specify constructor invocation;
                    // classifier 'Page2' does not have a companion object"

                    startActivity(intent)*/

                } else {

                    // Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showErrorSnackbar(view,"Accesso fallito! ${task.exception}",
                        true)
                }
            }
    }

}