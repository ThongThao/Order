package com.example.admin.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogInViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.let { user ->
                        // Retrieve user data from Firestore to check role
                        firestore.collection("users")
                            .document(user.uid)
                            .get()
                            .addOnSuccessListener { document ->
                                val role = document.getString("role")
                                if (role == "Admin") {
                                    onSuccess()
                                } else {
                                    onError("Bạn không có quyền truy cập")
                                }

                            }
                            .addOnFailureListener { e ->
                                onError(e.message ?: "Failed to retrieve user data")
                            }
                    }
                } else {
                    onError(task.exception?.message ?: "Đăng nhập thất bại")
                }
            }
    }
}
