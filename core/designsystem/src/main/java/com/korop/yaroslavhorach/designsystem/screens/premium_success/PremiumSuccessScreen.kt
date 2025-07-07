package com.korop.yaroslavhorach.designsystem.screens.premium_success

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessAction
import com.korop.yaroslavhorach.designsystem.screens.premium_success.model.PremiumSuccessViewState
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary

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
            text = stringResource(R.string.premium_success_title_text),
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.premium_success_subtitle_text),
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )

        Spacer(Modifier.weight(1f))
        PremiumButton(text = stringResource(R.string.premium_success_primary_btn_text)) {
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