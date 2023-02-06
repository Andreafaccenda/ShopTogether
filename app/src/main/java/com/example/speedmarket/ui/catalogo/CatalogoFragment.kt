package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCatalogoBinding
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.ui.catalogo.filtri.Filtri
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_app.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CatalogoFragment : Fragment() {

    lateinit var binding: FragmentCatalogoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit  var nomeCategoria :String
    private lateinit var filtri :ArrayList<String>
    private lateinit var listaProdotti: List<Prodotto>
    private var offerta =false
    var isBackFromB = false
    val viewModel: ProdViewModel by viewModels()
    private val adapter by lazy { ProdottoAdapter() }

    /**
     * Per UI Test commentare riga 77, 82, 110 e 111.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCatalogoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottomNavigationView.menu.findItem(R.id.catalogo).isChecked = true
        if(!isOnline(requireContext())) dialogInternet()
        setupOnBackPressed(R.id.home)
        binding.catalogoVuoto.hide()
        val args = this.arguments
        this.nomeCategoria = args?.get("nome_categoria").toString()
        this.offerta= args?.getBoolean("offerta") == true
        observeLocal()
        binding.barraDiRicerca.clearFocus()
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
        filtri = arrayListOf("vuoto","vuoto","vuoto","vuoto")
        if(args?.getStringArrayList("filtri").toString() != "null"){
            filtri=args?.getStringArrayList("filtri") as ArrayList<String>
        }
        binding.btnFiltri.setOnClickListener{
            replaceFragment(Filtri())
        }
        binding.barraDiRicerca.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val testo = query.toString()
                adapter.filtraListaNome(testo, listaProdotti.toMutableList())
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val testo = newText.toString()
                adapter.filtraListaNomeChange(testo, listaProdotti.toMutableList())
                return false
            }
        })
    }

    private fun observeRemote() {
        if (isOnline(requireContext())) {
            viewModel.prodotto.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                    }
                    is UiState.Failure -> {
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        listaProdotti = state.data
                        adapterFiltro(listaProdotti.toMutableList())
                    }
                }
            }
        }
    }

    private fun observeLocal() {
        viewModel.prodottiLocal.observe(viewLifecycleOwner) { prodotti ->
            prodotti?.apply {
                if (prodotti.isEmpty()) {
                    observeRemote()
                    viewModel.getProducts()
                    return@observe
                }
                listaProdotti = prodotti
                adapterFiltro(listaProdotti.toMutableList())
            }
        }
    }

    private fun adapterFiltro(mutableList: MutableList<Prodotto>){
        if(nomeCategoria == "null") adapter.updateList(mutableList)
        else adapter.filtraListaCategoria(nomeCategoria, mutableList)
        if (offerta) adapter.filtraListaOfferta(mutableList)
        if (filtri[3] != "vuoto") adapter.filtraListaqrCode(filtri[3],
            mutableList)
        if (filtri[0] != "vuoto") adapter.filtraListaPrezzo(filtri[0],
            mutableList)
        if (filtri[1] != "vuoto") adapter.filtraListaMarchio(filtri[1],
            mutableList)
        if (filtri[2] != "vuoto") adapter.filtraListaSottoCategoria(filtri[2],
            mutableList)
        if (filtri[1] != "vuoto" && filtri[0] != "vuoto") adapter.filtraListaMarchioPrezzo(filtri[0], filtri[1], mutableList)
        if (filtri[2] != "vuoto" && filtri[0] != "vuoto") adapter.filtraListaCategoriaPrezzo(filtri[0], filtri[2], mutableList)
        if (filtri[2] != "vuoto" && filtri[1] != "vuoto") adapter.filtraListaCategoriaMarchio(filtri[1], filtri[2], mutableList)
        if (filtri[2] != "vuoto" && filtri[1] != "vuoto" && filtri[0] != "vuoto") adapter.filtraLista(filtri[0], filtri[1], filtri[2], mutableList)
        if (adapter.itemCount <= 0) {
            binding.catalogoVuoto.show()
        }
    }

    private fun observerTest() {
        viewModel.prodottiLocal.observe(viewLifecycleOwner) { prodotti ->
            prodotti?.apply {
                if(nomeCategoria == "null") adapter.updateList(prodotti.toMutableList())
                else adapter.filtraListaCategoria(nomeCategoria, prodotti.toMutableList())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isBackFromB = true
    }
    override fun onResume() {
        super.onResume()
        if (isBackFromB) {
            isBackFromB = false
            reload(CatalogoFragment())
        }
    }
}

