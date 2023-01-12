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
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.setupOnBackPressedExit
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedExit()
        getUserSession()
        observer()
        utente?.let { viewModel.getUtente(it.id) }
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
            "Frutta",
            "Carne",
            "Affettati",
            "Surgelati",
            "Latte",
            "Cereali",
            "Caffe",
            "Dolci",
            "Snack",
            "Panneteria",
            "Condimenti",
            "Conserve",
            "Pasta",
            "Alcolici",
            "Bibite",
            "Freschi"
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

        binding.btnRicercaProdotto.setOnClickListener(){
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, CatalogoFragment())
            transaction?.commit()
        }
        categorieAdapter.onItemClick = {
           val bundle = Bundle()
            bundle.putString("nome_categoria",it.title.toString())
            val fragment = CatalogoFragment()
            fragment.arguments= bundle
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, fragment)
            transaction?.commit()

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