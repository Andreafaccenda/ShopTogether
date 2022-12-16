package com.example.speedmarket.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentAccediBinding
import com.example.speedmarket.ui.HomeActivity
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccediFragment : Fragment() {

    val TAG: String = "RegisterFragment"
    lateinit var binding: FragmentAccediBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccediBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        binding.btnAccedi.setOnClickListener {
            if (validation()) {
                viewModel.login(email = binding.etEmail.text.toString(),password = binding.etPassword.text.toString())
            }
        }
        binding.btnRegistrati.setOnClickListener {
            view.findNavController().navigate(R.id.action_accediFragment_to_registrazioneUtenteFragment)
        }
        binding.btnRecuperaPassword.setOnClickListener{
            view.findNavController().navigate(R.id.action_accediFragment_to_recuperaPasswordFragment)
        }

    }

    fun observer(){
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.btnAccedi.setText("")
                    binding.accediProgressBar.show()
                }
                is UiState.Failure -> {
                    binding.btnAccedi.setText("Accedi")
                    binding.accediProgressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnAccedi.setText("Accedi")
                    binding.accediProgressBar.hide()
                    toast(state.data)
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    intent.putExtra("Username", "Benvenuto,")
                    startActivity(intent)
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.etEmail.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.inserisci_email))
        }else{
            if (!binding.etEmail.text.toString().isValidEmail()){
                isValid = false
                toast(getString(R.string.email_invalida))
            }
        }
        if (binding.etPassword.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.messaggio_inserisci_password))
        }else{
            if (binding.etPassword.text.toString().length < 8){
                isValid = false
                toast(getString(R.string.messaggio_password_invalida))
            }
        }
        return isValid
    }


}



