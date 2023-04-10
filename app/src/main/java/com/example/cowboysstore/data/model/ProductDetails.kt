package com.example.cowboysstore.data.model

data class ProductDetails(
    val id : String,
    val title : String,
    val images : List<String>,
    val price : String,
    val category : String,
    val availableSizes : List<String>,
    val description : String,
    val structure : List<String>,
    val hit : Boolean
)