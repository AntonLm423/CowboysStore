package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("data") val responseBody: AuthResponseBody
)




