package com.example.order.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.order.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel: ViewModel() {
    private var currentUserId: String = ""
    private var currentUser: User? = null
    fun getUser(userId: String?): LiveData<User?> {

        val userLiveData = MutableLiveData<User?>()
        userId?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    userLiveData.value = user
                }
                .addOnFailureListener {
                    userLiveData.value = null
                }
        }
        return userLiveData
    }
    fun updateUser(userId: String?, updatedUser: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userId?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(it)
                .set(updatedUser)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }
    }


}