package com.example.cowboysstore.data.model

import com.google.gson.annotations.SerializedName

data class ProfileChangeResponse(
    @SerializedName("data") var changedFields: ChangedFields
)