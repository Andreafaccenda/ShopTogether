package com.example.speedmarket.repository

import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.UiState

interface AuthRepository {
    fun registerUser(email: String, password: String, utente: Utente, result: (UiState<String>) -> Unit)
    fun updateUserInfo(utente: Utente, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun autoLogin(email: String, password: String): Boolean
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun storeSession(id: String, result: (Utente?) -> Unit)
    fun getSession(result: (Utente?) -> Unit)
}