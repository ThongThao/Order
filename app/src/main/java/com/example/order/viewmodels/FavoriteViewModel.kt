package com.example.order.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.model.Restaurant
import com.example.order.model.favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FavoriteViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val favoritesCollection = db.collection("favorites")
    private val restaurantsCollection = db.collection("Restaurant")

    private val _favoriteRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val favoriteRestaurants: StateFlow<List<Restaurant>> get() = _favoriteRestaurants
    fun fetchFavoriteRestaurants(userId: String) {
        viewModelScope.launch {
            val favoriteDoc = favoritesCollection.document(userId).get().await()
            if (favoriteDoc.exists()) {
                val favorite = favoriteDoc.toObject(favorite::class.java)
                val restaurantIds = favorite?.restaurants.orEmpty()
                val restaurantDetails = restaurantIds.mapNotNull { id ->
                    restaurantsCollection.document(id).get().await().toObject(Restaurant::class.java)
                }
                _favoriteRestaurants.value = restaurantDetails
            }
        }
    }
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
