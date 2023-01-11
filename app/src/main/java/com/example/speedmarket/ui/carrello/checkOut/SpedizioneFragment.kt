package com.example.speedmarket.ui.carrello.checkOut

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.databinding.FragmentSpedizioneBinding
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.impostazioni.profile.Profile
import com.example.speedmarket.util.dialog
import com.example.speedmarket.util.setupOnBackPressedFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpedizioneFragment : Fragment() {

    lateinit var binding: FragmentSpedizioneBinding
    val viewModel: AuthViewModel by viewModels()
    private var utente= Utente()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpedizioneBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment())
        //getUtente()

        if(!utente.indirizzo_spedizione.citta.isNullOrEmpty()){
            setIndirizzoSpedizione()
        }else{
            if(!utente.residenza.citta.isNullOrEmpty()){
                setResidenza()
            }else{
                Log.d("Tag",utente.residenza.citta)
                dialog(Profile(),"Indirizzo di residenza","Inserisci il tuo indirizzo di residenza prima di procedere con l'ordine","Profilo")
            }
        }
    }

    private fun setIndirizzoSpedizione(){
        binding.txtCitta.setText(utente.indirizzo_spedizione.citta)
        binding.txtCap.setText(utente.indirizzo_spedizione.cap)
        binding.txtProvincia.setText(utente.indirizzo_spedizione.provincia)
        binding.txtVia.setText(utente.indirizzo_spedizione.via)
        binding.txtNumeroCivico.setText(utente.indirizzo_spedizione.numero_civico)
    }
    private fun setResidenza(){
        binding.txtCitta.setText(utente.residenza.citta)
        binding.txtCap.setText(utente.residenza.cap)
        binding.txtProvincia.setText(utente.residenza.provincia)
        binding.txtVia.setText(utente.residenza.via)
        binding.txtNumeroCivico.setText(utente.residenza.numero_civico)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null) {
                this.utente=user

            }
        }
    }

}