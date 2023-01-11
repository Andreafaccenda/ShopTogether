package com.example.speedmarket.ui.impostazioni

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import coil.load
import com.example.speedmarket.databinding.FragmentImpostazioniBinding
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.assistenzaClienti.AssistenzaClientiFragment
import com.example.speedmarket.ui.impostazioni.profile.Profile
import com.example.speedmarket.util.replaceFragment
import com.example.speedmarket.util.setupOnBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Impostazioni : Fragment() {
    lateinit var binding: FragmentImpostazioniBinding
    private val viewModelAuth: AuthViewModel by viewModels()
    private lateinit var utente: Utente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImpostazioniBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        getUserSession()
        observer()
        viewModelAuth.getUtente(this.utente.id)
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
                    this.utente = state.data!!
                    updateUI()
                }
            }
        }
    }

    private fun updateUI() {
        binding.txtEmailUser.text = this.utente.email
        binding.txtNome.text = "${this.utente.nome} ${this.utente.cognome}"
        bindImage(binding.imageProfile, this.utente.immagine_profilo)
    }

    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            this.utente = user!!
        }
    }

    private fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
        }
    }

}