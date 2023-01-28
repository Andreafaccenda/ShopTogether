package com.example.speedmarket.ui.impostazioni.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.databinding.FragmentDettagliaCartaCreditoBinding
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.setupOnBackPressedFragment
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DettagliaCartaCreditoFragment : Fragment(), ProfileManager {

    lateinit var binding: FragmentDettagliaCartaCreditoBinding
    val viewModelAuth: AuthViewModel by viewModels()
    override var utente : Utente? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDettagliaCartaCreditoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(Profile())
        getSession()
        observerGetUser()
        utente?.let { viewModelAuth.getUtente(it.id) }
        observerUpdateUser()
        utentePagamento()
    }

    private fun getSession() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }
    private fun utentePagamento() {
        binding.checkBoxSalva.setOnClickListener {
            if (checkCaselle()) {
                utente?.pagamento!!.numero_carta = binding.etNumeroCarta.text.toString()
                utente?.pagamento!!.data_scadenza = binding.etDataScadenza.text.toString()
                viewModelAuth.updateUserInfo(utente!!)
                binding.checkBoxSalva.isEnabled = false
            } else {
                binding.checkBoxSalva.isChecked = false
                toast("Le informazioni inserite non sono complete")
            }
        }
    }

    override fun updateUI(){
        if(utente?.pagamento!!.numero_carta != ""){
            binding.etNumeroCarta.setText(utente?.pagamento!!.numero_carta)
            binding.etDataScadenza.setText(utente?.pagamento!!.data_scadenza)
            binding.etCvv.isEnabled = false
        }
    }

    private fun checkCaselle(): Boolean {
        return !(binding.etNumeroCarta.text.isNullOrEmpty() || binding.etDataScadenza.text.isNullOrEmpty())
    }

    private fun observerGetUser() {
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

    private fun observerUpdateUser() {
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