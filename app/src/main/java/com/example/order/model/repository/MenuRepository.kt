package com.example.order.model.repository

import com.example.order.model.Menu
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MenuRepository {
    private val db = FirebaseFirestore.getInstance().collection("Menu")
    suspend fun getMenus(): List<Menu> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { it.toObject(Menu::class.java)?.apply { itemId= it.id } }
        } catch (e: Exception) {
            emptyList()
        }
    }
}