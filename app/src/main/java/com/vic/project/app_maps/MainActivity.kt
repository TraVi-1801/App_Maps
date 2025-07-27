package com.vic.project.app_maps

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.vic.project.app_maps.presentation.screens.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val orientation = resources.configuration.orientation

            DisposableEffect(orientation) {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT, Color.TRANSPARENT
                        ), navigationBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT, Color.TRANSPARENT
                        )
                    )
                } else {
                    WindowCompat.setDecorFitsSystemWindows(window, true)
                }
                onDispose {}
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                HomeScreen()
            }
        }
    }
}
