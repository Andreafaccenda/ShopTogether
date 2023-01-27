package com.example.speedmarket.ui.impostazioni

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.model.Utente
import androidx.fragment.app.viewModels
import com.example.speedmarket.databinding.FragmentImpostazioniBinding
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.assistenzaClienti.AssistenzaClientiFragment
import com.example.speedmarket.ui.impostazioni.profile.Profile
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Impostazioni : Fragment(), ProfileManager {
    lateinit var binding: FragmentImpostazioniBinding
    private val viewModelAuth: AuthViewModel by viewModels()
    override var utente: Utente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImpostazioniBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        getUserSession()
        observer()
        utente?.let { viewModelAuth.getUtente(it.id) }
        binding.assistenzaClientiLayout.setOnClickListener{
            replaceFragment(AssistenzaClientiFragment())
        }
        binding.accountLayout.setOnClickListener {
            replaceFragment(Profile())
        }
    }

    private fun observer() {
        viewModelAuth.utente.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    utente = state.data!!
                    updateUI()
                }
            }
        }
    }

    override fun updateUI() {
        binding.txtEmailUser.text = utente?.email
        binding.txtNome.text = "${utente?.nome} ${utente?.cognome}"
        super.bindImage(binding.imageProfile, utente?.immagine_profilo)
    }

    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null)
                utente = user
        }

    }


}