package com.example.weatherapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    var user by mutableStateOf(auth.currentUser)

    fun signup(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user = auth.currentUser
                    onSuccess()
                } else {
                    onError(it.exception?.message ?: "Signup Failed")
                }
            }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user = auth.currentUser
                    onSuccess()
                } else {
                    onError(it.exception?.message ?: "Login Failed")
                }
            }
    }

    fun logout() {
        auth.signOut()
        user = null
    }
}
