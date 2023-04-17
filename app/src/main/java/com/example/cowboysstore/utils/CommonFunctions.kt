package com.example.cowboysstore.utils

import android.content.Context

fun getAccessToken(context: Context) : String {
    val sharedPref = context.getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE)
    return sharedPref.getString(Constants.PREFS_AUTH_KEY, "Invalid Token")!!
}