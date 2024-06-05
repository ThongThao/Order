package com.example.order.model

data class User(
    var id: String?="",
    var fullName: String?="",
    var phoneNumber: String?="",
    var email: String?="",
    var password: String?="",
    var address: String? = null,
    var sex: String?="",
    var image: String?="",
    var role: String?="")