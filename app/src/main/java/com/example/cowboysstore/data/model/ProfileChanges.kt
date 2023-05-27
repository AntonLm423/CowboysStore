package com.example.cowboysstore.data.model

data class ProfileChanges(
    val path : String,
    val op : String = "replace",
    val value : String
)
