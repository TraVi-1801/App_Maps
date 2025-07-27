package com.vic.project.app_maps.di.preferences

import android.content.Context
import com.vic.project.app_maps.data.preferences.initializer.PreferencesInitializer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface PreferencesEntryPoint {

    fun inject(preferencesInitializer : PreferencesInitializer)


    companion object {

        fun resolve(context: Context): PreferencesEntryPoint {
            val appContext = context.applicationContext ?: throw IllegalStateException(
                "applicationContext was not found in PreferencesEntryPoint"
            )
            return EntryPointAccessors.fromApplication(
                appContext,
                PreferencesEntryPoint::class.java
            )
        }
    }
}