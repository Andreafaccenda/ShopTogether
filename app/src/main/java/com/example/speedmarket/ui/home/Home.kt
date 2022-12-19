package com.example.speedmarket.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.CategorieAdapter
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentHomeBinding
import com.example.speedmarket.model.Categorie
import com.example.speedmarket.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class Home : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: AuthViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var lista_categorie: ArrayList<Categorie>
    private lateinit var immagineId: Array<Int>
    private lateinit var categorie: Array<String>
    private lateinit var sfondo: Array<Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       immagineId = arrayOf(
            R.drawable.cat_1,
            R.drawable.cat_2,
           R.drawable.cat_3,
           R.drawable.cat_4,
           R.drawable.cat_5,
           R.drawable.cat_6
        )
        categorie = arrayOf(
            "Frutta",
            "Carne",
            "Pesce",
            "Formaggio",
            "Latte",
            "Cereali"
        )
        sfondo = arrayOf(
            R.drawable.background_categorie1,
            R.drawable.background_categorie2,
            R.drawable.background_categorie3,
            R.drawable.background_categorie4,
            R.drawable.background_categorie5,
            R.drawable.background_categorie6
        )
        recyclerView = binding.recyclerViewCategorie
        recyclerView.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        lista_categorie = arrayListOf()
        getCategoriaData()

        binding.editTextTextPersonName.setOnClickListener(){

        }


    }
    private fun getCategoriaData(){
        for(i in immagineId.indices){
            val categoria = Categorie(immagineId[i],categorie[i],sfondo[i])
            lista_categorie.add(categoria)
        }
        recyclerView.adapter = CategorieAdapter(lista_categorie)
    }
    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null) {
                binding.txtAccount.text = "Benvenuto," + user.nome.toString()
            }
        }
    }


}