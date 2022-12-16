package com.example.speedmarket.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.speedmarket.databinding.ActivityHomeBinding
import com.example.speedmarket.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null){
                binding.txtAccount.text = "Benvenuto,"+user.nome.toString()
            }
        }
    }

}