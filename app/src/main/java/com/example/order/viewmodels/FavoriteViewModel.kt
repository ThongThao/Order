package com.example.order.viewmodels

import androidx.lifecycle.ViewModel
import com.example.order.model.favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FavoriteViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val favoritesCollection = db.collection("favorites")

    suspend fun addFavorite(userId: String, restaurantId: String) {
        val favoriteDoc = favoritesCollection.document(userId).get().await()
        if (favoriteDoc.exists()) {
            val favorite = favoriteDoc.toObject(favorite::class.java)
            favorite?.restaurants = favorite?.restaurants.orEmpty().toMutableList().apply {
                if (!contains(restaurantId)) add(restaurantId)
            }
            if (favorite != null) {
                favoritesCollection.document(userId).set(favorite).await()
            }
        } else {
            val newFavorite = favorite(
                favoriteId = UUID.randomUUID().toString(),
                custometId = userId,
                restaurants = listOf(restaurantId)
            )
            favoritesCollection.document(userId).set(newFavorite).await()
        }
    }

    suspend fun removeFavorite(userId: String, restaurantId: String) {
        val favoriteDoc = favoritesCollection.document(userId).get().await()
        if (favoriteDoc.exists()) {
            val favorite = favoriteDoc.toObject(favorite::class.java)
            favorite?.restaurants = favorite?.restaurants.orEmpty().toMutableList().apply {
                remove(restaurantId)
            }
            if (favorite != null) {
                favoritesCollection.document(userId).set(favorite).await()
            }
        }
    }

    suspend fun isFavorite(userId: String, restaurantId: String): Boolean {
        val favoriteDoc = favoritesCollection.document(userId).get().await()
        if (favoriteDoc.exists()) {
            val favorite = favoriteDoc.toObject(favorite::class.java)
            return favorite?.restaurants.orEmpty().contains(restaurantId)
        }
        return false
    }
}
