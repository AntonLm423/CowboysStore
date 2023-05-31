package com.example.cowboysstore.domain.entities

data class Profile(
    var name: String = "",
    var surname: String = "",
    var occupation: String = "",
    val avatarId: String = ""
)