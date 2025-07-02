package com.example.yaroslavhorach.linguaflow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.domain.prefs.model.UserData
import com.example.yaroslavhorach.linguaflow.ui.LingoApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefsRepository: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
       installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()
            val primaryColor = remember { mutableStateOf<Color?>(null) }
            val secondaryColor = remember { mutableStateOf<Color?>(null) }
            val userData = prefsRepository.getUserData().collectAsState(null)

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            if (userData.value != null){
                LinguaTheme(darkTheme = darkTheme, primaryColor = primaryColor.value, secondaryColor = secondaryColor.value) {
                    LingoApp(isOnboarding = userData.value!!.isOnboarding, onChangeColorScheme = { primary, secondary ->
                        primaryColor.value = primary
                        secondaryColor.value = secondary
                    })
                }
            }
        }
    }
}