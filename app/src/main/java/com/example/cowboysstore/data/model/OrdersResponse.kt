package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class OrdersResponse(
    @SerializedName("data") val orders: List<Order>
)

