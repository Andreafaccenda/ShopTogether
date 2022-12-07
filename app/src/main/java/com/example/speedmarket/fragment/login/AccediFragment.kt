package com.example.speedmarket.fragment.login

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityMainBinding
import com.example.speedmarket.databinding.FragmentAccediBinding
import com.example.speedmarket.fragment.UtilsFragment
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class AccediFragment : Fragment() {

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

       /* val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
        auth = FirebaseAuth.getInstance()*/

        binding.etEmail.requestFocus()
       /* binding.txtVRegister.setOnClickListener() {

            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener() {
            loginRegisterUser(it)
        }*/
        binding.btnRecuperaPassword.setOnClickListener() {
            val dialog = Dialog(requireContext())
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.custom_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val chiudi = dialog.findViewById<ImageView>(R.id.cancelId)

            chiudi.setOnClickListener() {
                dialog.dismiss()
            }
        }
        binding.btnRegistrati.setOnClickListener(){
            view.findNavController().navigate(R.id.action_accediFragment_to_registrazioneUtenteFragment)
        }

    }
}