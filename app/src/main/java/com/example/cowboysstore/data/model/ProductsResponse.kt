package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("data") val productsList: List<Product>
)



