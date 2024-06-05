package com.example.admin.model

data class User(
    var id: String?="",
    var fullName: String?="",
    var phoneNumber: String?="",
    var email: String?="",
    var password: String?="",
    var address: String? = null,
    var sex: String?="",
    var image: String?=null,
    var role: String?="")