package com.example.speedmarket.repository

import android.util.Log
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.FireStoreCollection
import com.example.speedmarket.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepositoryImp(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
    ) : AuthRepository {
    override fun registerUser(email: String, password: String, utente: Utente, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {

                if (it.isSuccessful){
                    Log.d("Tag","it.isSuccessfull")
                    utente.id = it.result.user?.uid ?: ""
                    updateUserInfo(utente) { state ->
                        when (state) {
                            is UiState.Success -> {
                                /*storeSession(id = it.result.user?.uid ?: "") {
                                    if (it == null){
                                        result.invoke(UiState.Failure("User register successfully but session failed to store"))
                                    }else{*/
                                result.invoke(
                                    UiState.Success("User register successfully!")
                                )

                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                        }
                    }
                }else{
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateUserInfo(utente: Utente, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.UTENTI).document(utente.id)
        document
            .set(utente)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("User has been update successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                            result.invoke(UiState.Success("Login successfully!"))}
                    }.addOnFailureListener {
                    result.invoke(UiState.Failure("Authentication failed, Check email and password"))
                }
    }
    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {

    }




}