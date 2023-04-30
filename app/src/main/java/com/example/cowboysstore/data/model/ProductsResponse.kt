package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ProductsResponse(
    @SerializedName("data") val productsList: List<Product>
)



