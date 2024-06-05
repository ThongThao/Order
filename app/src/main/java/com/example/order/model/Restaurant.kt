package com.example.order.model

data class Restaurant (
    var restaurantId: String? = "",
    var restaurantName: String? = "",
    var restaurantAdd: String? = "",
    var restaurantType: String? = "",
    var restaurantRate: Double=0.0,
    var restaurantImage: String?="",
    var menu: List<String>? = null
)