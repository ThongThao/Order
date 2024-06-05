package com.example.order.model.repository

import com.example.order.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepository {
    private val db = FirebaseFirestore.getInstance().collection("Category")

    suspend fun getCategories(): List<Category> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { it.toObject(Category::class.java)?.apply { categoryId = it.id } }
        } catch (e: Exception) {
            emptyList()
        }
    }
}