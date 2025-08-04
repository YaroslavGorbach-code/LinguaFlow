package com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.runtime.LaunchedEffect
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
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model.GameUnlockedSuccessAction
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model.GameUnlockedSuccessUiMessage
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.model.GameUnlockedSuccessViewState
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.components.TextButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.domain.game.model.Game
import java.util.Locale

@Composable
internal fun GameUnlockedSuccessRoute(
    viewModel: GameUnlockedSuccessViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToGame: (id: Long, name: Game.GameName) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity

    viewState.uiMessage?.let { uiMessage ->
        when (val message = uiMessage.message) {
            is GameUnlockedSuccessUiMessage.NavigateToGame -> {
                LaunchedEffect(uiMessage.id) {
                    viewModel.adManager.showInterstitial(activity)
                    viewModel.clearMessage(uiMessage.id)
                }
                viewState.game?.name?.let { onNavigateToGame(message.id, it) }
            }
            is GameUnlockedSuccessUiMessage.NavigateBack -> {
                LaunchedEffect(uiMessage.id) {
                    viewModel.adManager.showInterstitial(activity)
                    viewModel.clearMessage(uiMessage.id)
                }
                onNavigateBack()
            }
        }
    }
    LinguaBackground {
        GameUnlockedSuccessScreen(
            state = viewState,
            actioner = { action ->
                viewModel.submitAction(action)
            },
        )
    }
}

@Composable
internal fun GameUnlockedSuccessScreen(
    state: GameUnlockedSuccessViewState,
    actioner: (GameUnlockedSuccessAction) -> Unit
) {
    val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.CupGameUnlocked))
        Spacer(Modifier.weight(1f))
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
            text = stringResource(R.string.game_unlocked_title_text),
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.game_unlocked_message_format, state.game?.getNameText(lang) ?: ""),
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )

        Spacer(Modifier.weight(1f))
        TextButton(stringResource(R.string.game_unlocked_continue_btn_text), onClick = {
            actioner(GameUnlockedSuccessAction.OnContinueBtnClicked)
        })
        Spacer(Modifier.height(16.dp))
        PrimaryButton(text = stringResource(R.string.game_unlocked_try_btn_text)) {
            state.game?.id?.let { actioner(GameUnlockedSuccessAction.OnTryGameBtnClicked(it)) }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun PremiumSuccessPreview() {
    LinguaBackground {
        LinguaTheme {
            GameUnlockedSuccessScreen(
                GameUnlockedSuccessViewState.Preview,
            ) {}
        }
    }
}