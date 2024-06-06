package com.example.order.model

data class User(
    var id: String?="",
    var fullName: String?="",
    var phoneNumber: String?="",
    var email: String?="",
    var password: String?="",
    var address: String? = null,
    var sex: String?="",
    var birthday: String? = null,
    var image: String?="",
    var role: String?="")