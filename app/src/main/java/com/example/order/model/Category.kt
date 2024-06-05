package com.example.order.model

data class Category(
    var categoryId: String = "",
    var categoryName: String = "",
    var categoryImage: String = "",
    var restaurants: List<String>? = null,
    var menu: List<String>? = null // Danh sách các nhà hàng thuộc danh
)