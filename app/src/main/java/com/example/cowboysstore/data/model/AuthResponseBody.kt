package com.example.cowboysstore.data.model

import com.example.cowboysstore.domain.entities.Profile

data class AuthResponseBody(
    val accessToken: String,
    val profile: Profile
)