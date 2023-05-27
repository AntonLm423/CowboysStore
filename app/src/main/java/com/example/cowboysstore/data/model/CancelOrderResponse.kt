package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class CancelOrderResponse(
    @SerializedName("data") val order: Order
)