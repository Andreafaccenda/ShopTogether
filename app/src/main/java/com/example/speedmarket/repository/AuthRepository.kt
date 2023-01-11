package com.example.speedmarket.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.UiState

interface AuthRepository {
    fun registerUser(email: String, password: String, utente: Utente, result: (UiState<String>) -> Unit)
    fun getUser(id: String, result: (UiState<Utente?>) -> Unit)
    fun updateUserInfo(utente: Utente, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun storeSession(id: String, result: (Utente?) -> Unit)
    fun getSession(result: (Utente?) -> Unit)
    suspend fun uploadImage(uri: Uri, utente: Utente, result: (UiState<Uri>) -> Unit)
    suspend fun getImage(utente: Utente, result: (String?) -> Unit)
}