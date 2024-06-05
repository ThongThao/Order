package com.example.order.model

data class favorite(
    var favoriteId: String = "",
    var custometId: String = "",
    var restaurants: List<String>? = null,
)
