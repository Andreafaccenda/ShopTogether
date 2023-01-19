package com.example.speedmarket.ui.impostazioni.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.R
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast

class OrdiniFragment : Fragment(), ProfileManager {


    private val viewModelAuth: AuthViewModel by viewModels()
    override var utente: Utente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ordini, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserSession()
        observer()
        utente?.let { viewModelAuth.getUtente(it.id) }
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
                    utente = state.data
                    updateUI()
                }
            }
        }
    }

    override fun updateUI() {
        for (ordine in utente?.lista_carrelli!!) {
            /**
             * stampa ordine
             */
        }
    }

    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }
}