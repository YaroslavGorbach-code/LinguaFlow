package com.korop.yaroslavhorach.designsystem.screens.rate

import android.app.Activity
import android.content.ActivityNotFoundException
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.korop.yaroslavhorach.common.utill.buildRateAppIntent
import com.korop.yaroslavhorach.common.utill.buildRateAppWebIntent
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.screens.rate.model.RateAppAction
import com.korop.yaroslavhorach.designsystem.screens.rate.model.RateAppUiMessage
import com.korop.yaroslavhorach.designsystem.screens.rate.model.RateAppViewState
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.components.TextButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary

@Composable
internal fun RateAppRoute(
    viewModel: RateAppViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity

    viewState.uiMessage?.let { uiMessage ->
        when (uiMessage.message) {
            is RateAppUiMessage.RateApp -> {
                try {
                    activity.startActivity(buildRateAppIntent())
                } catch (e: ActivityNotFoundException) {
                    activity.startActivity(buildRateAppWebIntent())
                }
                onNavigateBack()
            }
            is RateAppUiMessage.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    LinguaBackground {
        RateAppScreen(
            state = viewState,
            actioner = { action ->
                viewModel.submitAction(action)
            },
        )
    }
}

@Composable
internal fun RateAppScreen(
    state: RateAppViewState,
    actioner: (RateAppAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Star))
        Spacer(Modifier.weight(1f))
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .size(200.dp),
            contentScale = ContentScale.Crop,
            composition = composition,
        )
        Spacer(Modifier.height(40.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.rate_app_title_text),
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.rate_app_message_text),
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )

        Spacer(Modifier.weight(1f))
        TextButton(stringResource(R.string.rate_app_letter_btn_text), onClick = {
            actioner(RateAppAction.OnLetterClicked)
        })
        Spacer(Modifier.height(16.dp))
        PrimaryButton(text = stringResource(R.string.rate_app_rate_btn_text)) {
            actioner(RateAppAction.OnRateClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun PremiumSuccessPreview() {
    LinguaBackground {
        LinguaTheme {
            RateAppScreen(
                RateAppViewState.Preview,
            ) {}
        }
    }
}