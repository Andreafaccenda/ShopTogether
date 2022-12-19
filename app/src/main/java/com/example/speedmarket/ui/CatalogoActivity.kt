package com.example.speedmarket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speedmarket.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
 class CatalogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catagolo)
    }
}