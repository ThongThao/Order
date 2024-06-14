package com.example.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.model.Rate
import com.example.order.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RatingViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _ratings = MutableStateFlow<List<Rate>>(emptyList())
    val ratings: StateFlow<List<Rate>> get() = _ratings

    fun submitRating(rate: Rate, restaurantName: String) {
        viewModelScope.launch {
            val docRef = db.collection("ratings").document()
            rate.id = docRef.id
            docRef.set(rate)
                .addOnSuccessListener {
                    updateRestaurantRating(restaurantName, rate.rating)
                }
                .addOnFailureListener { e ->
                    // Handle the error
                }
        }
    }

    private fun updateRestaurantRating(restaurantName: String?, newRating: Double?) {
        if (restaurantName == null || newRating == null) return

        db.collection("Restaurant")
            .whereEqualTo("restaurantName", restaurantName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val restaurantRef = document.reference
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(restaurantRef)
                        val restaurant = snapshot.toObject(Restaurant::class.java)
                        if (restaurant != null) {
                            val currentRating = restaurant.restaurantRate ?: 0.0
                            val newRestaurantRate = if (currentRating == 0.0) {
                                newRating
                            } else {
                                (currentRating + newRating) / 2 // Simplified averaging
                            }
                            transaction.update(restaurantRef, "restaurantRate", newRestaurantRate)
                        }
                    }.addOnSuccessListener {
                        updateRatingCount(restaurantName)
                    }.addOnFailureListener { e ->
                        // Handle failure
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }

    private fun updateRatingCount(restaurantName: String) {
        db.collection("ratings")
            .whereEqualTo("restaurantName", restaurantName)
            .get()
            .addOnSuccessListener { documents ->
                val count = documents.size()
                db.collection("Restaurant")
                    .whereEqualTo("restaurantName", restaurantName)
                    .get()
                    .addOnSuccessListener { restaurantDocs ->
                        for (restaurantDoc in restaurantDocs) {
                            val restaurantRef = restaurantDoc.reference
                            restaurantRef.update("ratingCount", count)
                                .addOnSuccessListener {
                                    // Handle success
                                }
                                .addOnFailureListener { e ->
                                    // Handle failure
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                    }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    fun getRatingsForRestaurant(restaurantName: String) {
        viewModelScope.launch {
            db.collection("ratings")
                .whereEqualTo("restaurantName", restaurantName)
                .get()
                .addOnSuccessListener { documents ->
                    val ratingsList = documents.map { it.toObject(Rate::class.java) }
                    _ratings.value = ratingsList
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }
}
