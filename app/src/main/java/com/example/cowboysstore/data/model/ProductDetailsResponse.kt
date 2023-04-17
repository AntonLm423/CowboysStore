package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("data")val product: Product
)

