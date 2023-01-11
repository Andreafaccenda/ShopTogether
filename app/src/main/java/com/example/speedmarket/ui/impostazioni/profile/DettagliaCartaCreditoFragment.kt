package com.example.speedmarket.ui.impostazioni.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCatalogoBinding
import com.example.speedmarket.databinding.FragmentDettagliProdottoBinding
import com.example.speedmarket.databinding.FragmentDettagliaCartaCreditoBinding
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.setupOnBackPressed
import com.example.speedmarket.util.setupOnBackPressedFragment
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DettagliaCartaCreditoFragment : Fragment() {

    lateinit var binding: FragmentDettagliaCartaCreditoBinding
    val viewModelAuth: AuthViewModel by viewModels()
    private lateinit var utente : Utente
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDettagliaCartaCreditoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(Profile())
        getUtente()
        oberver()
        utente_pagamento()
        popola()

    }
    private fun getUtente() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }
    private fun utente_pagamento() {
        binding.checkBoxSalva.setOnClickListener {
            utente.pagamento.numero_carta = binding.etNumeroCarta.text.toString()
            utente.pagamento.data_scadenza = binding.etDataScadenza.text.toString()
            viewModelAuth.updateUserInfo(utente)
        }
    }
    private fun popola(){
        if(utente.pagamento != null){
            binding.etNumeroCarta.setText(utente.pagamento.numero_carta)
            binding.etDataScadenza.setText(utente.pagamento.data_scadenza)
            binding.etDataScadenza.isEnabled = false
            binding.etCvv.isEnabled = false
            binding.checkBoxSalva.isChecked=true
            binding.checkBoxSalva.isEnabled=false
        }
    }
    private fun oberver() {
            viewModelAuth.updateUserInfo.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                    }
                    is UiState.Failure -> {
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        toast(state.data)
                        binding.etNumeroCarta.isEnabled = false
                        binding.etDataScadenza.isEnabled = false
                        binding.etCvv.isEnabled = false

                    }
                }
            }
    }
}