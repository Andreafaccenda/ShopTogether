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
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagamentoFragment : Fragment() {

    lateinit var binding: FragmentPagamentoBinding
    val viewModelAuth: AuthViewModel by viewModels()
    private lateinit var utente: Utente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPagamentoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onStart() {
        super.onStart()
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente=user
            }
        }
    }

}