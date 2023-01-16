package com.example.speedmarket.ui

import android.widget.ImageView
import androidx.core.net.toUri
import coil.load
import com.example.speedmarket.model.Utente

interface ProfileManager {
    var utente: Utente?

    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
        }
    }

    fun updateUI()
}