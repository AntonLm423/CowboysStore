package com.example.cowboysstore.data.model

data class Order(
    val createdAt: String,
    val deliveryAddress: String,
    val etd: String,
    val id: String,
    val number: Int,
    var productId: String,
    val productPreview: String,
    val productQuantity: Int,
    val productSize: String,
    val status: String
)