package com.example.cowboysstore.utils

import android.util.Patterns

class Validator {
    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val MAX_APARTMENT_VALUE = 5000
        private const val MAX_NAME_LENGTH = 15
        private const val MIN_NAME_LENGTH = 2
        private const val MAX_SURNAME_LENGTH = 20
        private const val MIN_SURNAME_LENGTH = 2

        fun isEmailValid(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= PASSWORD_MIN_LENGTH
        }

        fun isApartmentValid(apartment: String) : Boolean {
            return when {
                apartment.isEmpty() -> false
                apartment.toInt() !in 0 .. MAX_APARTMENT_VALUE -> false
                else -> true
            }
        }

        fun isNameValid(name : String) =
            name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH

        fun isSurnameValid(surname : String) =
            surname.length in MIN_SURNAME_LENGTH..MAX_SURNAME_LENGTH
    }
}