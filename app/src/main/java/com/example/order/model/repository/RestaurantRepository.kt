package com.example.order.model.repository


import com.example.order.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class RestaurantRepository {
    private val db = FirebaseFirestore.getInstance().collection("Restaurant")
    suspend fun getRestaurants(): List<Restaurant> {
        return try {
            val snapshot = db.get().await()
            snapshot.documents.mapNotNull { it.toObject(Restaurant::class.java)?.apply { restaurantId = it.id } }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun getRestaurantById(restaurantName: String?): Flow<Restaurant?> {
        return flow {
            emit(getRestaurants().find { it.restaurantName == restaurantName })
        }
    }
}