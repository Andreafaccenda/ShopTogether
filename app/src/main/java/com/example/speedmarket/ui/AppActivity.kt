package com.example.speedmarket.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityAppBinding
import com.example.speedmarket.ui.catalogo.CatalogoFragment
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.home.Home
import com.example.speedmarket.ui.impostazioni.Impostazioni
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    lateinit var binding: ActivityAppBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(Home())
        binding = ActivityAppBinding.inflate(layoutInflater)
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

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(Home())
                    removeFragment(CatalogoFragment())
                    removeFragment(Impostazioni())
                    removeFragment(CarrelloFragment())
                }
                R.id.catalogo-> {
                    replaceFragment(CatalogoFragment())
                    removeFragment(Impostazioni())
                    removeFragment(CarrelloFragment())
                    removeFragment(Home())
                }
                R.id.impostazioni-> {
                    replaceFragment(Impostazioni())
                    removeFragment(CarrelloFragment())
                    removeFragment(Home())
                    removeFragment(CatalogoFragment())
                }
                R.id.carrello-> {
                    replaceFragment(CarrelloFragment())
                    removeFragment(Home())
                    removeFragment(Impostazioni())
                    removeFragment(CatalogoFragment())
                }
                else->{

                }
            }
            true
        }


    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
    private fun removeFragment(fragment: Fragment){
        val transaction = supportFragmentManager?.beginTransaction()
        transaction?.remove(fragment)
        transaction?.commitAllowingStateLoss()
    }
}
