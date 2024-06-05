package com.example.admin.model

data class Category(
    var categoryId: String? = null,
    var categoryName: String? = null,
    var categoryImage: String? = null,
    var restaurants: List<String>? = null,
    var menu: List<String>? = null // Danh sách các nhà hàng thuộc danh mục
)
