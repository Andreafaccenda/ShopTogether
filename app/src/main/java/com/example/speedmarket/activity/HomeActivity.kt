package com.example.speedmarket.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityHomeBinding
import com.example.speedmarket.databinding.ActivityMainBinding

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