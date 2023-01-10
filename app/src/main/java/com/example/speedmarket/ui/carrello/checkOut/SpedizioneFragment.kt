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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpedizioneFragment : Fragment() {

    lateinit var binding: FragmentSpedizioneBinding
    val viewModel: AuthViewModel by viewModels()
    private lateinit var utente: Utente

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
        getUtente()


    }
    /*private fun popola_cambi_spedizione(){
        if(utente.indirizzo_spedizione.isNullOrEmpty()){

        }
    }*/
    private fun getUtente() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null) {
                this.utente=user

            }
        }
    }

}