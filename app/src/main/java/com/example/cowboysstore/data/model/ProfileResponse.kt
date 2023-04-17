package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("data") val responseData: ProfileResponseData
)

data class ProfileResponseData(
    val profile: Profile
)
