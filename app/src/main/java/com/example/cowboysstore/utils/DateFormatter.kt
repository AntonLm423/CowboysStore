package com.example.cowboysstore.utils

import java.text.SimpleDateFormat

class DateFormatter {
    companion object {
        fun formatDate(inputDate: String, inputDateFormat: String, outputDateFormat: String): String? {
                val formattedInputDate = SimpleDateFormat(inputDateFormat)
                val formattedOutput = SimpleDateFormat(outputDateFormat)
                val date = formattedInputDate.parse(inputDate)
                return formattedOutput.format(requireNotNull(date))
        }
    }
}
