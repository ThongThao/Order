package com.example.order.model
data class Restaurant(
    var restaurantId: String? = "",
    var restaurantName: String? = "",
    var restaurantAdd: String? = "",
    var restaurantType: String? = "",
    var restaurantRate: Double = 0.0,
    var ratingCount: Int? = null,
    var restaurantImage: String? = "",
    var menu: List<String>? = null
) : Comparable<Restaurant> {

    override fun compareTo(other: Restaurant): Int {
        return when {
            this.restaurantRate > other.restaurantRate -> -1
            this.restaurantRate < other.restaurantRate -> 1
            else -> 0
        }
    }
}
