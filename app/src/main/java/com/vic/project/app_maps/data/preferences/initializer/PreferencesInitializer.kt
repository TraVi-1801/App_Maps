package com.vic.project.app_maps.data.preferences.initializer

import android.content.Context
import androidx.startup.Initializer
import com.vic.project.app_maps.data.preferences.Preferences
import com.vic.project.app_maps.di.preferences.PreferencesEntryPoint
import javax.inject.Inject

class PreferencesInitializer : Initializer<Unit> {

    @set:Inject
    internal lateinit var preferences: Preferences

    override fun create(context: Context) {
        PreferencesEntryPoint.resolve(context).inject(this)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf()
}