package com.example.speedmarket.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentRegistrazioneUtenteBinding
import com.example.speedmarket.model.Indirizzo
import com.example.speedmarket.model.Pagamento
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.*

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrazioneUtenteFragment : Fragment() {

    val TAG: String = "RegisterFragment"
    lateinit var binding: FragmentRegistrazioneUtenteBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrazioneUtenteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        binding.btnRegistrati.setOnClickListener {
            if (validation()) {
                    viewModel.register(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString(),
                    utente = getUtenteObj()
                )
            }
        }
    }

    fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.btnRegistrati.setText("")
                    binding.registerProgress.show()
                }
                is UiState.Failure -> {
                    binding.btnRegistrati.setText("Registrati")
                    binding.registerProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnRegistrati.setText("Registrati")
                    binding.registerProgress.hide()
                    toast(state.data)
                    view?.findNavController()?.navigate(R.id.action_registrazioneUtenteFragment_to_accediFragment)
                }
            }
        }
    }

    fun getUtenteObj(): Utente {
        return Utente(
            id = "",
            nome = binding.etNome.text.toString(),
            cognome = binding.etCognome.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString(),
            immagine_profilo = "",
            residenza = Indirizzo("","","","",""),
            pagamento = Pagamento("",""),
            indirizzo_spedizione = Indirizzo("","","","",""),
            lista_carrelli = arrayListOf(),
            numero_telefono = 0,
            genere = "",
            profileCompleted = false
        )
    }

    fun validation(): Boolean {

    return when{
        binding.etNome.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_inserisci_nome))
            false}
        binding.etCognome.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_inserisci_cognome))
            false}
        binding.etEmail.text.isNullOrEmpty() -> { toast(getString(R.string.messaggio_email))
            false}
        !binding.etEmail.text.toString().isValidEmail() -> {toast(getString(R.string.email_invalida))
            false}
        binding.etPassword.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_inserisci_password))
            false}
        binding.etPassword.text.toString().length < 8 ->{toast(getString(R.string.messaggio_password_invalida))
            false}
        !binding.etPassword.text.toString().equals(binding.etRipetiPassword.text.toString())->{toast(getString(R.string.messaggio_password_diverse))
            false}
        else ->{ true}
    }
    return true
}
}