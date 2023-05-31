package com.example.cowboysstore.domain.entities

data class Order(
    val createdAt: String = "",
    val deliveryAddress: String = "",
    val etd: String = "",
    val id: String = "",
    val number: Int = 0,
    var productId: String = "",
    val productPreview: String = "",
    val productQuantity: Int = 0,
    val productSize: String = "",
    val status: String = ""
)