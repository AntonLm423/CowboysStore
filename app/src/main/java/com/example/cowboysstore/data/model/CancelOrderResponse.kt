package com.example.cowboysstore.data.model

import com.example.cowboysstore.domain.entities.Order
import com.google.gson.annotations.SerializedName

data class CancelOrderResponse(
    @SerializedName("data") val order: Order
)