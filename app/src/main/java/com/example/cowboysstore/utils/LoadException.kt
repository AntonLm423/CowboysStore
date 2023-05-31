package com.example.cowboysstore.utils

class LoadException(
    val errorResId : Int? = null,
    val detailedErrorResId : Int? = null,
    val errorMessage : String? = null
    ) : Exception() { }