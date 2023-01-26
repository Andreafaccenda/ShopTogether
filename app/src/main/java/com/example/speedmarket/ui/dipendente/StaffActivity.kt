package com.example.speedmarket.ui.dipendente

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityStaffBinding
import com.example.speedmarket.util.createDialog
import com.example.speedmarket.util.dialog
import com.example.speedmarket.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

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

    override fun onBackPressed() {
        super.onBackPressed()
        val dialog = dialog()
        dialog.show()
        val button = dialog.findViewById<Button>(R.id.btn_esci)
        button.setOnClickListener {
            it.findNavController().popBackStack(R.id.staffHomeFragment,false)
            it.findNavController().popBackStack(R.id.ordineDetailsFragment,false)
            finish()
            exitProcess(0)
        }
        val imageButton = dialog.findViewById<ImageButton>(R.id.image_close)
        imageButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}