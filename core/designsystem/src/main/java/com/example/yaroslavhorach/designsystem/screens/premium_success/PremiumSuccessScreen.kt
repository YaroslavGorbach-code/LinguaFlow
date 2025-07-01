package com.example.yaroslavhorach.designsystem.screens.premium_success

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessAction
import com.example.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessViewState
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.example.yaroslavhorach.designsystem.theme.typoPrimary

@Composable
internal fun PremiumSuccessRoute(
    viewModel: PremiumSuccessViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    LinguaBackground {
        PremiumSuccessScreen(
            state = viewState,
            actioner = { action ->
                when (action) {
                    PremiumSuccessAction.OnPrimaryBtnClicked -> {
                        onNavigateBack()
                    }
                    else -> viewModel.submitAction(action)
                }
            },
        )
    }
}

@Composable
internal fun PremiumSuccessScreen(
    state: PremiumSuccessViewState,
    actioner: (PremiumSuccessAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Cup))
        Spacer(Modifier.weight(0.5f))
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .size(200.dp),
            contentScale = ContentScale.Crop,
            composition = composition,
            restartOnPlay = false
        )
        Spacer(Modifier.height(40.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Вітаємо в Premium!",
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Тепер тобі доступні всі ігри, необмежені токени та жодної реклами!",
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )

        Spacer(Modifier.weight(1f))
        PremiumButton(text = "ДО ТРЕНУВАНЬ") {
            actioner(PremiumSuccessAction.OnPrimaryBtnClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun PremiumSuccessPreview() {
    LinguaBackground {
        LinguaTheme {
            PremiumSuccessScreen(
                PremiumSuccessViewState.Preview,
            ) {}
        }
    }
}