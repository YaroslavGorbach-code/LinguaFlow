package com.korop.yaroslavhorach.exercises.exercise_completed

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.components.StaticTooltip
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.typoDisabled
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoSecondary
import com.korop.yaroslavhorach.exercises.R
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedAction
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedUiMessage
import com.korop.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedViewState
import com.korop.yaroslavhorach.ui.SpeakingLevel
import com.korop.yaroslavhorach.ui.UiText

@Composable
internal fun ExerciseCompletedRoute(
    viewModel: ExerciseCompletedViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToRateApp: () -> Unit,
    onNavigateToGameUnlocked: (gameId: Long) -> Unit
) {
    val activity = LocalContext.current as Activity

    val state by viewModel.state.collectAsStateWithLifecycle()

    state.uiMessage?.let { uiMessage ->
        when (val message = uiMessage.message) {
            is ExerciseCompletedUiMessage.ShowAd -> {
                LaunchedEffect(uiMessage.id) {
                    viewModel.adManager.showInterstitial(activity)
                    viewModel.clearMessage(uiMessage.id)
                }
                onNavigateBack()
            }
            is ExerciseCompletedUiMessage.NavigateToGameUnlocked -> {
                onNavigateToGameUnlocked(message.gameId)
            }
            is ExerciseCompletedUiMessage.NavigateToRateApp -> onNavigateToRateApp()
        }
    }

    LinguaBackground {
        ExerciseCompletedScreen(
            state = state,
            actioner = { action ->
                when (action) {
                    else -> viewModel.submitAction(action)
                }
            },
        )
    }

    BackHandler { }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun ExerciseCompletedScreen(
    state: ExerciseCompletedViewState,
    actioner: (ExerciseCompletedAction) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(Modifier.height(40.dp))
        MainAnimation()
        Spacer(Modifier.weight(1f))
        TitleWithProgress(state)
        Spacer(Modifier.weight(0.7f))
        TimeAndXp(state)
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = stringResource(R.string.exercise_success_primary_btn_text),
        ) {
            actioner(ExerciseCompletedAction.OnContinueClicked)
        }
    }
}

@Composable
private fun TimeAndXp(state: ExerciseCompletedViewState) {
    val animatedXP by animateIntAsState(
        targetValue = state.experience,
        animationSpec = tween(durationMillis = 800, delayMillis = 1000),
        label = "xpAnimation"
    )

    val timeAlpha = remember { Animatable(0f) }
    val xpAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        timeAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 0)
        )
        xpAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 0)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(modifier = Modifier.alpha(timeAlpha.value)) {
            InfoBlock(
                title = stringResource(R.string.exercise_succcess_time_title_text),
                icon = LinguaIcons.Chronometer,
                value = state.time
            )
        }

        Box(modifier = Modifier.alpha(xpAlpha.value)) {
            InfoBlock(
                title = stringResource(R.string.exercise_success_xp_title_text),
                icon = LinguaIcons.LightingThunder,
                value = "+$animatedXP"
            )
        }
    }
}

@Composable
private fun TitleWithProgress(state: ExerciseCompletedViewState) {
    var visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.exercise_completed_title_text),
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.h2
            )

            Spacer(Modifier.height(20.dp))

            StaticTooltip(
                enableFloatAnimation = true,
                backgroundColor = MaterialTheme.colorScheme.surface,
                borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
                triangleAlignment = Alignment.Start,
                paddingHorizontal = 20.dp,
                contentPadding = 16.dp,
                cornerRadius = 12.dp,
                borderSize = 1.5.dp
            ) {
                Text(
                    text = UiText.RandomFromResourceArray(R.array.exercise_success_messages).asString(),
                    color = MaterialTheme.colorScheme.typoSecondary(),
                    style = LinguaTypography.body4
                )
            }

            val level = SpeakingLevel.fromExperience(state.allUserExperience)
            val progress = (state.allUserExperience - level.experienceRequired.first).toFloat() /
                    (level.experienceRequired.last - level.experienceRequired.first).toFloat()

            LinguaProgressBar(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                progress = progress,
                progressColor = MaterialTheme.colorScheme.primary,
                progressBarHeight = 22.dp
            )
        }
    }
}

@Composable
private fun MainAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.congrats))
    LottieAnimation(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 40.dp),
        contentScale = ContentScale.Crop,
        composition = composition
    )
}

@Composable
private fun InfoBlock(title: String, icon: Int, value: String) {
    Column(
        modifier = Modifier
            .defaultMinSize(minWidth = 120.dp)
            .border(
                1.5.dp,
                color = MaterialTheme.colorScheme.onBackgroundDark(),
                RoundedCornerShape(9.dp)
            )
            .padding(vertical = 16.dp, horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.typoDisabled(),
            style = LinguaTypography.body4
        )
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = null
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.typoSecondary(),
                style = LinguaTypography.subtitle4
            )
        }
    }
}

@Preview
@Composable
private fun SpeakingExercisePreview() {
    LinguaBackground {
        LinguaTheme {
            ExerciseCompletedScreen(
                ExerciseCompletedViewState.Preview,
            ) {}
        }
    }
}