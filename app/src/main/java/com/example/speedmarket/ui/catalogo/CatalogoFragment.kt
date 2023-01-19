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
import com.example.speedmarket.databinding.FragmentCatalogoBinding
import com.example.speedmarket.ui.catalogo.filtri.Filtri
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogoFragment : Fragment() {

    lateinit var binding: FragmentCatalogoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var nome_categoria :String
    private lateinit var filtri :ArrayList<String>
    private var offerta =false
    val viewModel: ProdViewModel by viewModels()
    private val adapter by lazy { ProdottoAdapter() }

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
        setupOnBackPressed()
        binding.catalogoVuoto.hide()
        val args = this.arguments
        this.nome_categoria = args?.get("nome_categoria").toString()
        this.offerta= args?.getBoolean("offerta") == true
        observer()
        binding.barraDiRicerca.clearFocus()
        viewModel.getProducts()
        recyclerView = binding.recyclerViewCatalogo
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        adapter.onItemClick = {
            val bundle = Bundle()
            bundle.putSerializable("prodotto",it)
            val fragment = DettagliProdottoFragment()
            fragment.arguments= bundle
            replaceFragment(fragment)
        }
        filtri = arrayListOf("vuoto","vuoto","vuoto")
        if(args?.getStringArrayList("filtri").toString() != "null"){
            filtri=args?.getStringArrayList("filtri") as ArrayList<String>
            Log.d("filtri",filtri.toString())
        }
        binding.btnFiltri.setOnClickListener{
            replaceFragment(Filtri())
        }
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
    private fun observer() {
        viewModel.prodotto.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                  if(this.nome_categoria == "null") adapter.updateList(state.data.toMutableList())
                  else adapter.filtraListaCategoria(this.nome_categoria,state.data.toMutableList())
                    if(offerta)adapter.filtraListaOfferta(state.data.toMutableList())
                    if(filtri[0]!="vuoto")adapter.filtraListaPrezzo(filtri[0],state.data.toMutableList())
                    if(filtri[1]!="vuoto")adapter.filtraListaMarchio(filtri[1],state.data.toMutableList())
                    if(filtri[2]!="vuoto") adapter.filtraListaSottoCategoria(filtri[2],state.data.toMutableList())
                    if(filtri[1]!="vuoto"&&filtri[0]!="vuoto")adapter.filtraListaMarchioPrezzo(filtri[0],filtri[1],state.data.toMutableList())
                    if(filtri[2]!="vuoto"&&filtri[0]!="vuoto")adapter.filtraListaCategoriaPrezzo(filtri[0],filtri[2],state.data.toMutableList())
                    if(filtri[2]!="vuoto"&&filtri[1]!="vuoto")adapter.filtraListaCategoriaMarchio(filtri[1],filtri[2],state.data.toMutableList())
                    if(filtri[2]!="vuoto"&&filtri[1]!="vuoto"&&filtri[0]!="vuoto")adapter.filtraLista(filtri[0],filtri[1],filtri[2],state.data.toMutableList())
                    if(adapter.itemCount<= 0) {
                        binding.catalogoVuoto.show()
                        }
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
                    adapter.filtraListaNomeChange(testo,state.data.toMutableList())
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
                    adapter.filtraListaNome(testo,state.data.toMutableList())
                }
            }
        }
    }

}

