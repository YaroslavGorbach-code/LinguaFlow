package com.korop.yaroslavhorach.linguaflow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.korop.yaroslavhorach.common.helpers.AdManager
import com.korop.yaroslavhorach.common.helpers.BillingManager
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.korop.yaroslavhorach.linguaflow.ui.LingoApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val appScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Inject
    lateinit var prefsRepository: PrefsRepository

    @Inject
    lateinit var addManager: AdManager

    @Inject
    lateinit var billingManager: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        appScope.launch {
            addManager.loadInterstitial()
        }
        appScope.launch {
            try {
                if (billingManager.getAllActivePurchases().isEmpty()) {
                    prefsRepository.deactivatePremium()
                } else {
                    prefsRepository.activatePremium()
                }
            } catch (e: Exception){ }

            // FOR TEST ONLY
//            billingManager.billingClient?.consumeAsync(
//                ConsumeParams.newBuilder()
//                    .setPurchaseToken(billingManager.getAllActivePurchases().get(0).purchaseToken)
//                    .build(),
//                { _, _ -> })
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = false
            val primaryColor = remember { mutableStateOf<Color?>(null) }
            val secondaryColor = remember { mutableStateOf<Color?>(null) }
            val userData = prefsRepository.getUserData().collectAsState(null)

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            if (userData.value != null) {
                LinguaTheme(
                    darkTheme = darkTheme,
                    primaryColor = primaryColor.value,
                    secondaryColor = secondaryColor.value
                ) {
                    LingoApp(isOnboarding = userData.value!!.isOnboarding, onChangeColorScheme = { primary, secondary ->
                        primaryColor.value = primary
                        secondaryColor.value = secondary
                    })
                }
            }
        }
    }
}