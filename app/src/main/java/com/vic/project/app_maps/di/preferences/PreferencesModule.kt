package com.vic.project.app_maps.di.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.vic.project.app_maps.data.preferences.Preferences
import com.vic.project.app_maps.utils.AppConstant.App_Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PreferencesModule {

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): Preferences {
        val sharedPreferences =
            context.getSharedPreferences(App_Preferences, MODE_PRIVATE)
        return Preferences(sharedPreferences)
    }
}
