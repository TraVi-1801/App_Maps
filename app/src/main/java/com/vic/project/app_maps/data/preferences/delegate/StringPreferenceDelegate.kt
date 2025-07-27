package com.vic.project.app_maps.data.preferences.delegate

import androidx.core.content.edit
import com.vic.project.app_maps.data.preferences.Preferences
import kotlin.reflect.KProperty

fun stringPreferences(key: String, defaultValue: String) =
    StringPreferenceDelegate(key, defaultValue)

class StringPreferenceDelegate(
    private val key: String,
    private val defaultValue: String
) {
    operator fun getValue(preferences: Preferences, property: KProperty<*>): String {
        return preferences.sharedPreferences.getString(key, null) ?: let {
            setValue(preferences, property, defaultValue)
            defaultValue
        }
    }

    operator fun setValue(preferences: Preferences, property: KProperty<*>, value: String) {
        preferences.sharedPreferences.edit { putString(key, value) }
    }
}


