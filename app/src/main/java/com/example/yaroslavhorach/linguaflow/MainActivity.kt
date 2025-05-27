package com.example.yaroslavhorach.linguaflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.linguaflow.ui.LingoApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
       installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()
            val primaryColor = remember { mutableStateOf<Color?>(null) }
            val secondaryColor = remember { mutableStateOf<Color?>(null) }

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            LinguaTheme(darkTheme = darkTheme, primaryColor = primaryColor.value, secondaryColor = secondaryColor.value) {
                LingoApp(onChangeColorScheme = { primary, secondary ->
                    primaryColor.value = primary
                    secondaryColor.value = secondary
                })
            }
        }
    }
}