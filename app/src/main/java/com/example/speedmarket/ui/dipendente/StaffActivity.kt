package com.example.speedmarket.ui.dipendente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityAppBinding
import com.example.speedmarket.databinding.ActivityStaffBinding
import com.example.speedmarket.databinding.FragmentOrdiniBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.MainActivity
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.profile.OrdiniAdapter
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaffActivity : AppCompatActivity() {

    lateinit var binding:ActivityStaffBinding
    private lateinit var recyclerView : RecyclerView
    private val viewModelAuth: AuthViewModel by viewModels()
    private val adapter by lazy { OrdiniAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAuth.getListUser()
        observer()
        recyclerView = binding.recyclerViewOrdini
        recyclerView.layoutManager =  LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        binding.btnLogOut.setOnClickListener{
            viewModelAuth.logout {
                startActivity(Intent(this, MainActivity::class.java))}
            }
    }
    private fun observer() {
        viewModelAuth.utenti.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                }
                is UiState.Success -> {
                    var lista = arrayListOf<Carrello>()
                   for(user in state.data.toMutableList()){
                       if(user?.lista_carrelli!!.isNotEmpty()){
                           for(carrello in user?.lista_carrelli!!){
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