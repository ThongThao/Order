package com.example.order.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.order.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel: ViewModel() {
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
}