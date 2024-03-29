package com.example.speedmarket.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentAccediBinding
import com.example.speedmarket.ui.AppActivity
import com.example.speedmarket.ui.dipendente.StaffActivity
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccediFragment : Fragment() {

    lateinit var binding: FragmentAccediBinding
    val viewModel: AuthViewModel by viewModels()
    var isBackFromB = false

    /**
     * Per UI Test commentare riga 46, 47, 50 e, dopo riga 50, aggiungere le seguenti istruzioni:
     *
     *  val intent = Intent(requireContext(), AppActivity::class.java)
        intent.putExtra("Username", "Benvenuto,")
        startActivity(intent)
        activity?.finish()
     *
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccediBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedExit()
        getDatiSalvati()
        observer()
        binding.btnAccediLogIn.setOnClickListener {
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
    fun getDatiSalvati() {
        viewModel.getSession { user ->
            if (user != null) {
                binding.etEmail.setText(user.email)
                binding.etPassword.setText(user.password)
                viewModel.login(email = binding.etEmail.text.toString(),password = binding.etPassword.text.toString())
            }
        }
    }
    fun observer() {
        if (isOnline(requireContext())) {
            viewModel.login.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.btnAccediLogIn.text=""
                        binding.accediProgressBar.show()
                    }
                    is UiState.Failure -> {
                        binding.btnAccediLogIn.text="Accedi"
                        binding.accediProgressBar.hide()
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        binding.btnAccediLogIn.text="Accedi"
                        binding.accediProgressBar.hide()
                        if(state.data == "Login staff conad effettuato con successo!") {
                            startActivity(Intent(requireContext(), StaffActivity::class.java))
                            activity?.finish()
                        }else{
                            val intent = Intent(requireContext(), AppActivity::class.java)
                            intent.putExtra("Username", "Benvenuto,")
                            startActivity(intent)
                            activity?.finish()
                        }
                        toast(state.data)

                    }
                }
            }
        }
        else dialogInternet()
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
    override fun onPause() {
        super.onPause()
        isBackFromB = true
    }
    override fun onResume() {
        super.onResume()
        if (isBackFromB) {
            isBackFromB = false
            reload(AccediFragment())
        }
    }
}



