package com.example.speedmarket.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityMainBinding
import com.example.speedmarket.fragment.login.RegistrazioneUtenteFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


      /*  @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity,HomeActivity::class.java))
                finish()
            },
            2000
        )*/
        binding.btnAccedi.setOnClickListener() {
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }


        /* val uri = Uri.parse("tel:8005551234")
            intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
            finish()*/

    }
}