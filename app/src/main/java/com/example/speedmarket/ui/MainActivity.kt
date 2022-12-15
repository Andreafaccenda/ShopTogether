package com.example.speedmarket.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.speedmarket.databinding.ActivityMainBinding
import com.example.speedmarket.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
  //      autoLogin()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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

       /* @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            },
            2000
        )*/

        binding.btnAccedi.setOnClickListener() {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }


    }


    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null){
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
 /*   private fun autoLogin() {
        /*val sharedPref = applicationContext.getSharedPreferences(SharedPrefConstants.LOCAL_SHARED_PREF, Context.MODE_PRIVATE)
        val shared = getSharedPreferences("local_shared_pref", MODE_PRIVATE)
        val email = sharedPref.getString("email", "0")
        val password = sharedPref.getString("password", "0")
        Log.d("email",email.toString())
        Log.d("password",password.toString())
        if(email.equals("0") || password.equals("0")) {
            return
        }
        objViewModel.login(email.toString(), password.toString())
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))*/
    } */
}


