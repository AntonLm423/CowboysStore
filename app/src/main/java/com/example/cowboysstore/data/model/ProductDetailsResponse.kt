package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class ProductDetailsResponse(
   @SerializedName("data") val product: Product
)

