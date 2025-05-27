package com.example.yaroslavhorach.exercises.tongue_twisters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.exercises.extentions.topBarBgRes
import com.example.yaroslavhorach.exercises.tongue_twisters.model.TongueTwisterExerciseAction
import com.example.yaroslavhorach.exercises.tongue_twisters.model.TongueTwisterExerciseUiMessage
import com.example.yaroslavhorach.exercises.tongue_twisters.model.TongueTwisterExerciseViewState

@Composable
internal fun TongueTwisterExerciseRoute(
    viewModel: TongueTwisterExerciseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit
) {
    val tongueTwisterExerciseViewState by viewModel.state.collectAsStateWithLifecycle()

    TongueTwisterExerciseScreen(
        screenState = tongueTwisterExerciseViewState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {
                TongueTwisterExerciseAction.OnBackClicked -> onNavigateBack()
                else -> viewModel.submitAction(action)
            }
        },
        onNavigateToExerciseResult = onNavigateToExerciseResult
    )
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun TongueTwisterExerciseScreen(
    screenState: TongueTwisterExerciseViewState,
    onMessageShown: (id: Long) -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit,
    actioner: (TongueTwisterExerciseAction) -> Unit
) {
    screenState.uiMessage?.let { uiMessage ->
        when (val message = uiMessage.message) {
            is TongueTwisterExerciseUiMessage.NavigateToExerciseResult -> {
                onNavigateToExerciseResult(message.time, message.experience)
                onMessageShown(uiMessage.id)
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        TopBar(screenState, actioner)
        Box(
            modifier = Modifier
                .padding(top = 150.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
        ) {
            TwisterContent(screenState, actioner)
        }
    }
}


@Composable
private fun TopBar(screenState: TongueTwisterExerciseViewState, actioner: (TongueTwisterExerciseAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(screenState.block.topBarBgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
        Row(
            Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .clickable { actioner(TongueTwisterExerciseAction.OnBackClicked) },
                painter = painterResource(LinguaIcons.CircleClose),
                contentDescription = null
            )
            Spacer(Modifier.width(18.dp))
            LinguaProgressBar(
                screenState.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
private fun TwisterContent(
    state: TongueTwisterExerciseViewState,
    actioner: (TongueTwisterExerciseAction) -> Unit
) {
    val allowAnimate = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .fillMaxSize()
    ) {
        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = state.twist,
            transitionSpec = {
                if (allowAnimate.value) {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 500)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = 500)
                    )
                } else {
                    (EnterTransition.None) togetherWith ExitTransition.None
                }

            },
            label = "TwistTextAnimation"
        ) {
            Column {
                Text(
                    state.title.asString(),
                    style = LinguaTypography.h5,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    state.description.asString(),
                    style = LinguaTypography.body3,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.Companion.weight(0.7f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.twist?.textString ?: "",
                    color = MaterialTheme.colorScheme.typoPrimary(),
                    style = LinguaTypography.body1.copy(fontSize = 24.sp)
                )
                Spacer(Modifier.Companion.weight(1f))
            }

        }

        PrimaryButton(
            modifier = Modifier, text = "Далі"
        ) {
            allowAnimate.value = true
            actioner(TongueTwisterExerciseAction.OnNextClicked)
        }
        Spacer(Modifier.height(20.dp))
    }


}


@Preview
@Composable
private fun SpeakingExercisePreview() {
    LinguaBackground {
        LinguaTheme {
            Row {
                TongueTwisterExerciseScreen(
                    screenState = TongueTwisterExerciseViewState.PreviewSpeaking,
                    {},
                    { _, _ -> },
                    {})
            }
        }
    }
}