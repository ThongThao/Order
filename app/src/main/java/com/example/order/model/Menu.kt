package com.example.order.model

data class Menu (
    var itemId: String? = "",
    var  itemName: String? = "",
    var  itemDescription: String? = "",
    var itemPrice: Int? = 0,
    var  itemType: String? = "",
    var  itemRestaurant: String? = "",
    var  itemImage: String?="",
)