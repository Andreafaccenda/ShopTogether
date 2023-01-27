package com.example.speedmarket.ui.dipendente

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityStaffBinding
import com.example.speedmarket.util.dialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
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