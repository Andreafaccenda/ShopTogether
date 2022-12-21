package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCatalogoBinding
import com.example.speedmarket.databinding.FragmentHomeBinding
import com.example.speedmarket.model.Categorie
import com.example.speedmarket.model.CategorieAdapter
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.ProdottoAdapter
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.prod.ProdViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.hide
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
@AndroidEntryPoint
class CatalogoFragment : Fragment() {

    lateinit var binding: FragmentCatalogoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var nome_categoria :String
    val viewModel: ProdViewModel by viewModels()
    val adapter by lazy { ProdottoAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCatalogoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = this.arguments
        this.nome_categoria = args?.get("nome_categoria").toString()

        oberver()
        binding.barraDiRicerca.clearFocus()
        viewModel.getProducts()
        recyclerView = binding.recyclerViewCatalogo
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewCatalogo.adapter=adapter

    }
    private fun oberver() {
        viewModel.note.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    adapter.updateList(state.data.toMutableList())
                    adapter.filtaLista(this.nome_categoria)
                }
            }
        }
    }

}