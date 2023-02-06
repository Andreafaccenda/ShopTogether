package com.example.speedmarket.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentHomeBinding
import com.example.speedmarket.model.Categorie
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.catalogo.CatalogoFragment
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_app.*
import java.util.ArrayList

@AndroidEntryPoint
class Home : Fragment(), ProfileManager {

    lateinit var binding: FragmentHomeBinding
    val viewModel: AuthViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var listaCategorie: ArrayList<Categorie>
    private lateinit var immagineId: Array<Int>
    private lateinit var categorie: Array<String>
    private lateinit var sfondo: Array<Int>
    private lateinit var categorieAdapter : CategorieAdapter
    override var utente: Utente? = null

    /**
     * Per UI Test commentare riga 53, 54 e aggiungere riga 55.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottomNavigationView.menu.findItem(R.id.home).isChecked = true
        setupOnBackPressedExit()
        getUserSession()
        observer()
        utente?.let { viewModel.getUtente(it.id) }
     //   updateUI()
        immagineId = arrayOf(
           R.drawable.cat_1,
           R.drawable.cat_2,
           R.drawable.cat_3,
           R.drawable.cat_4,
           R.drawable.cat_5,
           R.drawable.cat_6,
           R.drawable.cat_7,
           R.drawable.cat_8,
           R.drawable.cat_9,
           R.drawable.cat_10,
           R.drawable.cat_11,
           R.drawable.cat_12,
           R.drawable.cat_13,
           R.drawable.cat_14,
           R.drawable.cat_15,
           R.drawable.cat_16

        )
        categorie = arrayOf(
            "Frutta e verdura",
            "Carne e salumi",
            "Formaggi latte e uova",
            "Surgelati e gelati",
            "Pesce e sushi",
            "Biscotti cereali e dolci",
            "Caffe e infusi",
            "Preparazioni dolci e salate",
            "Animali domestici",
            "Panneteria e snack salati",
            "Condimenti e conserve",
            "Articoli per la casa",
            "Pasta e riso",
            "Vino birra e altri alcolici",
            "Bevande e preparati",
            "Piatti pronti"
        )
        sfondo = arrayOf(
            R.drawable.background_categorie1,
            R.drawable.background_categorie2,
            R.drawable.background_categorie3,
            R.drawable.background_categorie4,
            R.drawable.background_categorie5,
            R.drawable.background_categorie6,
            R.drawable.background_categorie7,
            R.drawable.background_categorie8,
            R.drawable.background_categorie9,
            R.drawable.background_categorie10,
            R.drawable.background_categorie11,
            R.drawable.background_categorie12,
            R.drawable.background_categorie13,
            R.drawable.background_categorie14,
            R.drawable.background_categorie15,
            R.drawable.background_categorie16

        )
        recyclerView = binding.recyclerViewCategorie
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 4)
        recyclerView.setHasFixedSize(true)
        listaCategorie = arrayListOf()
        getCategoriaData()

        binding.btnRicercaProdotto.setOnClickListener{
            requireActivity().bottomNavigationView.selectedItemId=R.id.catalogo
        }
        binding.txtOfferte.setOnClickListener{
            val bundle = Bundle()
            bundle.putBoolean("offerta",true)
            val fragment = CatalogoFragment()
            fragment.arguments= bundle
            replaceFragment(fragment)
        }
        categorieAdapter.onItemClick = {
           val bundle = Bundle()
            bundle.putString("nome_categoria",it.title)
            val fragment = CatalogoFragment()
            fragment.arguments= bundle
            replaceFragment(fragment)
        }
    }
    private fun getCategoriaData(){
        for(i in immagineId.indices){
            val categoria = Categorie(immagineId[i],categorie[i],sfondo[i])
            listaCategorie.add(categoria)
        }
        categorieAdapter = CategorieAdapter(listaCategorie)
        recyclerView.adapter = categorieAdapter
    }

    private fun observer() {
        viewModel.utente.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    utente = state.data!!
                    updateUI()
                }
            }
        }
    }

    private fun getUserSession() {
        viewModel.getSession { user ->
            utente = user
        }
    }

    override fun updateUI() {
        binding.txtAccount.text = "Ciao, ${utente?.nome}"
        bindImage(binding.imageProfile, utente?.immagine_profilo)
    }


}