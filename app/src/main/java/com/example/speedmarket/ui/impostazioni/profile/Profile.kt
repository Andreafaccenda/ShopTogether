package com.example.speedmarket.ui.impostazioni.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.databinding.FragmentProfileBinding
import com.example.speedmarket.util.IMAGE
import com.example.speedmarket.util.setupOnBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : Fragment() {

    companion  object{
        const val IMAGE_PICK_CODE = 100
        const val IMAGE_REQUEST_CODE = 100
        private val PERMISSION_CODE = 101
    }

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        binding.layoutUploadImage.setOnClickListener{
            immagineGalleria()
        }
    }

    private fun immagineGalleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,IMAGE_REQUEST_CODE)
    }






}