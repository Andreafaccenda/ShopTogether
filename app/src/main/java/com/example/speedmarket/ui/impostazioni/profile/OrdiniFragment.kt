package com.example.speedmarket.ui.impostazioni.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentDettagliProdottoBinding
import com.example.speedmarket.databinding.FragmentOrdiniBinding
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.catalogo.ProdottoSimileAdapter
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdiniFragment : Fragment(), ProfileManager {

    private lateinit var recyclerView : RecyclerView
    private lateinit var binding:FragmentOrdiniBinding
    private val viewModelAuth: AuthViewModel by viewModels()
    private val adapter by lazy { OrdiniAdapter() }
    override var utente: Utente? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdiniBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserSession()
        observer()
        utente?.let { viewModelAuth.getUtente(it.id) }
        recyclerView = binding.recyclerViewOrdini
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
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
        utente?.lista_carrelli?.let { adapter.updateList(it.toMutableList()) }
    }
    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }
}