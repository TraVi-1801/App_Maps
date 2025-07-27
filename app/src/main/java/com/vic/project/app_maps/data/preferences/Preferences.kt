package com.vic.project.app_maps.data.preferences

import android.content.SharedPreferences
import com.vic.project.app_maps.data.preferences.delegate.stringPreferences
import javax.inject.Inject

class Preferences @Inject constructor(
    val sharedPreferences: SharedPreferences
) {

    var listSearch by stringPreferences(
        key = LIST_SEARCH,
        defaultValue = ""
    )


    companion object {
        const val LIST_SEARCH: String = "key_list_search"
    }
}

val String.Companion.Empty
    inline get() = ""

val Boolean.Companion.BolDefault
    inline get() = false
