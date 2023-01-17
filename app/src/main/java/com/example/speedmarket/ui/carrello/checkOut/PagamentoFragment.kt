package com.example.speedmarket.ui.carrello.checkOut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCarrelloBinding
import com.example.speedmarket.databinding.FragmentPagamentoBinding
import com.example.speedmarket.model.Pagamento
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.setupOnBackPressedFragment
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagamentoFragment : Fragment(), ProfileManager {

    lateinit var binding: FragmentPagamentoBinding
    val viewModelAuth: AuthViewModel by viewModels()
    override var utente: Utente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPagamentoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment())
        getUserSession()
        getUserObserver()
        utente?.let { viewModelAuth.getUtente(it.id) }
        binding.btnSalva.setOnClickListener {
            salvaPagamento()
        }
    }

    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null)
                utente = user
        }
    }

    private fun salvaPagamento() {
        if (binding.etProprietario.text.isNotEmpty() and binding.etDataScadenza.text.isNotEmpty()
        and binding.etNumeroCarta.text.isNotEmpty()) {
            val pagamento = Pagamento(
                binding.etNumeroCarta.text.toString(),
                binding.etDataScadenza.text.toString()
            )
            utente?.pagamento = pagamento
            utente?.let { viewModelAuth.updateUserInfo(it) }
            toast("Metodo di pagamento salvato!")
        }
        else
            toast("Le informazioni inserite sono incomplete!")
    }

    private fun getUserObserver() {
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
        binding.etProprietario.setText(utente?.nome + " " + utente?.cognome)
        binding.etNumeroCarta.setText(utente?.pagamento!!.numero_carta)
        binding.etDataScadenza.setText(utente?.pagamento!!.data_scadenza)
    }
}