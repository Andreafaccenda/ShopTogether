package com.example.speedmarket

import android.util.Log
import com.example.speedmarket.fragment.login.RegistrazioneUtenteFragment
import com.example.speedmarket.model.Utente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(fragment : RegistrazioneUtenteFragment, userInfo : Utente){
        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener{
                fragment.userRegistrationSuccess()
            }

            .addOnFailureListener{
                    e->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while registering the user.",
                    e

                )
            }
    }
    fun getCurrentUserID():String{
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}