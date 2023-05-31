package com.example.cowboysstore.data.model

import com.example.cowboysstore.domain.entities.ChangedFields
import com.google.gson.annotations.SerializedName

data class ProfileChangeResponse(
    @SerializedName("data") var changedFields: ChangedFields
)