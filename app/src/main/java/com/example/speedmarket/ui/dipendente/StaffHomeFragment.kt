package com.example.speedmarket.ui.dipendente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentStaffHomeBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.ui.MainActivity
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.profile.OrdiniAdapter
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.setupOnBackPressedExit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaffHomeFragment : Fragment() {

    lateinit var binding : FragmentStaffHomeBinding
    private lateinit var recyclerView : RecyclerView
    private val viewModelAuth: AuthViewModel by viewModels()
    private val adapter by lazy { OrdiniAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStaffHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedExit()
        viewModelAuth.getListUser()
        observer()
        recyclerView = binding.recyclerViewOrdini
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        binding.btnLogOut.setOnClickListener{
            viewModelAuth.logout {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
                it.findNavController().popBackStack(R.id.staffHomeFragment,false)
                it.findNavController().popBackStack(R.id.ordineDetailsFragment,false)
            }
        }
        binding.btnAddproduct.setOnClickListener{
            it.findNavController().navigate(R.id.action_staffHomeFragment_to_aggiungiProdotto)
        }
        adapter.onItemClick= {
            val directions = StaffHomeFragmentDirections.actionStaffHomeFragmentToOrdineDetailsFragment(it)
            findNavController().navigate(directions)
        }
    }
    private fun observer() {
        viewModelAuth.utenti.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                }
                is UiState.Success -> {
                    val lista = arrayListOf<Carrello>()
                    for(user in state.data.toMutableList()){
                        if(user?.lista_carrelli!!.isNotEmpty()){
                            for(carrello in user.lista_carrelli!!){
                                lista.add(carrello)
                            }
                        }
                    }
                    adapter.updateList(lista)
                }
            }
        }
    }
}