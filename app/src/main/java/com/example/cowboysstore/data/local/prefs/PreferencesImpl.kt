package com.example.cowboysstore.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesImpl @Inject constructor(@ApplicationContext val context: Context) : Preferences {

    companion object {
        private const val SHARED_PREFERENCES_NAME = "default"

        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
    }

    private val preferences : SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override var accessToken: String by PreferencesDelegate(preferences, KEY_ACCESS_TOKEN, "")

    private class PreferencesDelegate<T>(
        private val preferences: SharedPreferences,
        private val name : String,
        private val defValue : T
    ) : ReadWriteProperty<Any, T> {

        /* Temporarily only for string(accessToken) */

        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getPreference(name, defValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            setPreference(name, value ?: defValue)

        private fun getPreference(name : String, default : T) : T = with(preferences) {
            val res : Any = when(default) {
                is String -> getString(name, default).orEmpty()
                else -> throw IllegalArgumentException("This type cannot be saved into preferences")
            }
            res as T
        }

        private fun setPreference(name: String, value : T) = with(preferences.edit()) {
            when(value) {
                is String -> putString(name, value)
                else -> throw IllegalArgumentException("This type cannot be saved into preferences")
            }
        }.apply()
    }
}