package com.example.cowboysstore.utils

import com.example.cowboysstore.data.model.ErrorResponse
import com.google.gson.Gson

class GsonErrorParser {

    companion object {
        fun parseError(errorJsonMessage: String?): String {
            val gson = Gson()
            return gson.fromJson(errorJsonMessage, ErrorResponse::class.java).error.message
        }
    }
}