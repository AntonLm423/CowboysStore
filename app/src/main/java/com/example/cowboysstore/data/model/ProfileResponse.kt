package com.example.cowboysstore.data.model

import ProfileResponseData
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("data") val responseData: ProfileResponseData
)


