package com.example.order.model
import java.util.Date

data class Rate(
    var id: String? = "", // Unique ID for the rating
    var restaurantName: String? = "", // Name of the restaurant associated with the rating
    var customerId: String? = "",
    val customerName: String? =" ",// ID of the user who provided the rating
    var rating: Double? = 0.0, // Rating score (e.g., 1.0 to 5.0)
    var comment: String? = null, // Optional comment text
    var timestamp: Date = Date() // Timestamp for when the rating was given
)
