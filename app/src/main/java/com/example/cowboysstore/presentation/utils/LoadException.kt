package com.example.cowboysstore.presentation.utils

class LoadException(
    val errorResId : Int? = null,
    val messageResId : Int? = null
    ) : Exception() {
}