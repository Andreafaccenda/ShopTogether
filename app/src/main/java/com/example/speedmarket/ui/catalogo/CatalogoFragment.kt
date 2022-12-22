package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.databinding.FragmentCatalogoBinding
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogoFragment : Fragment() {

    lateinit var binding: FragmentCatalogoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var nome_categoria :String
    val viewModel: ProdViewModel by viewModels()
    val adapter by lazy { ProdottoAdapter() }

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
        binding.barraDiRicerca.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val testo = query.toString()
                oberver_searchView_text(testo)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val testo = newText.toString()
                oberver_searchView_text_change(testo)
                return false

            }

        })

    }
    private fun oberver() {
        viewModel.prodotto.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                  if(this.nome_categoria == "null") {adapter.updateList(state.data.toMutableList())}
                  else{adapter.filtraLista_categoria(this.nome_categoria,state.data.toMutableList())}
                    }
                }
            }
    }
    private fun oberver_searchView_text_change(testo : String) {
        viewModel.prodotto.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    adapter.filtraLista_nome_change(testo,state.data.toMutableList())
                }
            }
        }
    }
    private fun oberver_searchView_text(testo : String) {
        viewModel.prodotto.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    adapter.filtraLista_nome(testo,state.data.toMutableList())
                }
            }
        }
    }
}

