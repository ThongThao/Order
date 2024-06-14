package com.example.order.model
import java.util.Date

data class Rate(
    var id: String? =" ", // Unique ID for the rating
    val restaurantName: String? =" ", // ID of the order associated with the rating
    val customerId: String? =" ",
    val customerName: String? =" ",
    val rating: Double?, // Rating score (e.g., 1.0 to 5.0)
    val comment: String? = null, // Optional comment text
    val timestamp: Date = Date() // Timestamp for when the rating was given
)
