package com.example.speedmarket.ui.dipendente

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffBinding.inflate(layoutInflater)
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

    }

}