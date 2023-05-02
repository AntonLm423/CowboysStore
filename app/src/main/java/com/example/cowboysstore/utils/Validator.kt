package com.example.cowboysstore.utils

import android.util.Patterns

class Validator {
    companion object {
        private const val PASSWORD_MIN_LENGTH = 8

        fun isEmailValid(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= PASSWORD_MIN_LENGTH
        }
    }
}