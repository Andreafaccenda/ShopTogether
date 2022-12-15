package com.example.speedmarket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speedmarket.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileName=intent.getStringExtra("Username")
        binding.txtAccount.text = profileName.toString()
    }
}