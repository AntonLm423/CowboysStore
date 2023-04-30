package com.example.cowboysstore.data.model


data class Product(
    val badge: List<Badge> = emptyList(),
    val department: String = "",
    val description: String = "",
    val details: List<String> = emptyList(),
    val id: String = "",
    val images: List<String> = emptyList(),
    val preview: String = "",
    val price: Int = 0,
    val sizes: List<Size> = emptyList(),
    val title: String = ""
)