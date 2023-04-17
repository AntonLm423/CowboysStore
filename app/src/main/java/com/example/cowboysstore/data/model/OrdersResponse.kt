package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class OrdersResponse(
    @SerializedName("data") val orders: List<Order>
)

