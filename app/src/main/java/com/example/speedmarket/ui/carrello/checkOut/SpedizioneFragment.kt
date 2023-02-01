package com.example.speedmarket.ui.carrello.checkOut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.databinding.FragmentSpedizioneBinding
import com.example.speedmarket.model.Indirizzo
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.impostazioni.profile.Profile
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpedizioneFragment : Fragment(), ProfileManager {

    lateinit var binding: FragmentSpedizioneBinding
    val viewModel: AuthViewModel by viewModels()
    override var utente: Utente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSpedizioneBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment())
        binding.layoutSalva.hide()
        getUserSession()
        getUserObserver()
        utente?.let { viewModel.getUtente(it.id) }

        binding.checkboxIndirizzo.setOnClickListener {
            if (binding.checkboxIndirizzo.isChecked) {
                binding.layoutCheckbox.hide()
                binding.layoutSalva.show()
                enableAndClear()
            }
        }

        binding.btnSalvaIndirizzo.setOnClickListener {
            salvaIndirizzo()
        }
    }

    override fun updateUI() {
        if (utente?.indirizzo_spedizione!!.citta.isNotEmpty())
            setIndirizzoSpedizione()
        else if (utente?.residenza!!.citta.isNotEmpty())
            setResidenza()
        else {
            dialog(
                Profile(),
                "Indirizzo di residenza",
                "Inserisci il tuo indirizzo di residenza prima di procedere con l'ordine",
                "Profilo")
            binding.layoutCheckbox.hide()
            binding.layoutSalva.show()
            enableAndClear()
        }
    }

    private fun setIndirizzoSpedizione(){
        binding.txtCitta.setText(utente?.indirizzo_spedizione!!.citta)
        binding.txtCap.setText(utente?.indirizzo_spedizione!!.cap)
        binding.txtProvincia.setText(utente?.indirizzo_spedizione!!.provincia)
        binding.txtVia.setText(utente?.indirizzo_spedizione!!.via)
        binding.txtNumeroCivico.setText(utente?.indirizzo_spedizione!!.numero_civico)
    }
    private fun setResidenza(){
        binding.txtCitta.setText(utente?.residenza!!.citta)
        binding.txtCap.setText(utente?.residenza!!.cap)
        binding.txtProvincia.setText(utente?.residenza!!.provincia)
        binding.txtVia.setText(utente?.residenza!!.via)
        binding.txtNumeroCivico.setText(utente?.residenza!!.numero_civico)
    }

    private fun getUserSession() {
        viewModel.getSession { user ->
            if (user != null)
                utente = user
        }
    }

    private fun salvaIndirizzo() {
        if (binding.txtCitta.text.isNotEmpty() and binding.txtProvincia.text.isNotEmpty()
        and binding.txtCap.text.isNotEmpty() and binding.txtVia.text.isNotEmpty()
        and binding.txtNumeroCivico.text.isNotEmpty()) {
            val indirizzo = Indirizzo(
                binding.txtCitta.text.toString(),
                binding.txtProvincia.text.toString(),
                binding.txtCap.text.toString(),
                binding.txtVia.text.toString(),
                binding.txtNumeroCivico.text.toString()
            )
            utente?.indirizzo_spedizione = indirizzo
            utente?.let { viewModel.updateUserInfo(it) }
            toast("Indirizzo salvato!")
        }
        else
            toast("Le informazioni inserite sono incomplete!")
    }

    private fun enableAndClear() {
        binding.txtCitta.isEnabled = true
        binding.txtCap.isEnabled = true
        binding.txtProvincia.isEnabled = true
        binding.txtVia.isEnabled = true
        binding.txtNumeroCivico.isEnabled = true

        binding.txtCitta.setText("")
        binding.txtCap.setText("")
        binding.txtProvincia.setText("")
        binding.txtVia.setText("")
        binding.txtNumeroCivico.setText("")
    }

    private fun getUserObserver() {
        viewModel.utente.observe(viewLifecycleOwner) { state ->
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
}