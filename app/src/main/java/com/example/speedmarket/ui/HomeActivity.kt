package com.example.speedmarket.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.CategorieAdapter
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityHomeBinding
import com.example.speedmarket.databinding.ActivityMainBinding
import com.example.speedmarket.model.Categorie
import com.example.speedmarket.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    val viewModel: AuthViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var lista_categorie : ArrayList<Categorie>
    private lateinit var immagineId : Array<Int>
    private lateinit var categorie : Array<String>
    private lateinit var sfondo : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        immagineId = arrayOf(
            R.drawable.cat_1,
            R.drawable.cat_2
        )
        categorie = arrayOf(
            "Frutta",
            "Carne"
        )
        sfondo = arrayOf(
            R.drawable.background_categorie1,
            R.drawable.background_categorie2
        )
        recyclerView = findViewById(R.id.recyclerView_categorie)
        recyclerView.layoutManager =  LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        lista_categorie = arrayListOf<Categorie>()
        getCategoriaData()

        binding.editTextTextPersonName.setOnClickListener(){
            val intent = Intent(this, CatalogoActivity::class.java)
            startActivity(intent)
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