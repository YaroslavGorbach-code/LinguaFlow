package com.korop.yaroslavhorach.common.helpers

import android.app.Activity
import android.app.Application
import android.view.WindowManager
import androidx.core.view.WindowCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    private val app: Application,
    private val prefsRepository: PrefsRepository
) {
    companion object {
        private const val INTERSTITIAL_AD_ID = "ca-app-pub-6043694180023070/5305542775"
        private const val INTERSTITIAL_TEST_AD_ID = "ca-app-pub-3940256099942544/1033173712"
    }

    private var _interstitialAd: InterstitialAd? = null

    suspend fun loadInterstitial() {
        withContext(Dispatchers.Main) {
            if (prefsRepository.getUserData().first().isPremium.not()) {
                InterstitialAd.load(
                    app,
                    INTERSTITIAL_AD_ID,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            _interstitialAd = interstitialAd
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            _interstitialAd = null
                        }
                    })
            }
        }
    }

    suspend fun showInterstitial(activity: Activity) {
        withContext(Dispatchers.Main) {
            prefsRepository.getUserData()
                .onEach { userData ->
                    if (userData.isPremium.not()) {
                        _interstitialAd?.apply {
                            show(activity)
                            fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        _interstitialAd = null
                                    }
                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        _interstitialAd = null
                                    }
                                    override fun onAdShowedFullScreenContent() {

                                    }
                                }
                        }

                        loadInterstitial()
                    }
                }.collect()
        }
    }
}