package com.example.cowboysstore.utils

class LoadException(
    val errorResId : Int,
    val messageResId : Int
    ) : Exception() {
}