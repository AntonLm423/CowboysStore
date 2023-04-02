package com.example.cowboysstore.utils

class LoadException(
    val errorResId : Int? = null,
    val messageResId : Int? = null
    ) : Exception() {
}