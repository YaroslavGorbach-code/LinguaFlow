package com.example.yaroslavhorach.exercises.speaking

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.common.helpers.PermissionManager
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.designsystem.theme.Avocado
import com.example.yaroslavhorach.designsystem.theme.Black_3
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.Red
import com.example.yaroslavhorach.designsystem.theme.SonicSilver
import com.example.yaroslavhorach.designsystem.theme.UeRed
import com.example.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.example.yaroslavhorach.designsystem.theme.components.InactiveButton
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.example.yaroslavhorach.designsystem.theme.components.RealtimeWaveform
import com.example.yaroslavhorach.designsystem.theme.components.SecondaryButton
import com.example.yaroslavhorach.designsystem.theme.components.StaticTooltip
import com.example.yaroslavhorach.designsystem.theme.components.TextButton
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.example.yaroslavhorach.designsystem.theme.typoDisabled
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseAction
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseUiMessage
import com.example.yaroslavhorach.exercises.speaking.model.SpeakingExerciseViewState
import com.example.yaroslavhorach.ui.UiText

@Composable
internal fun SpeakingExerciseRoute(
    viewModel: SpeakingExerciseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit
) {
    val speakingExerciseViewState by viewModel.state.collectAsStateWithLifecycle()

    SpeakingExerciseScreen(
        screenState = speakingExerciseViewState,
        onMessageShown = viewModel::clearMessage,
        permissionManager = viewModel.permissionManager,
        actioner = { action ->
            when (action) {
                SpeakingExerciseAction.OnBackClicked -> onNavigateBack()
                else -> viewModel.submitAction(action)
            }
        },
        onNavigateToExerciseResult = onNavigateToExerciseResult
    )
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun SpeakingExerciseScreen(
    screenState: SpeakingExerciseViewState,
    permissionManager: PermissionManager,
    onMessageShown: (id: Long) -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit,
    actioner: (SpeakingExerciseAction) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        TopBar(screenState, actioner)
        Box(
            modifier = Modifier
                .padding(top = 150.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
        ) {
             when (val mode = screenState.mode) {
                    is SpeakingExerciseViewState.ScreenMode.IntroTest -> {
                        AnimatedContent(
                            targetState = mode.test.id,
                            transitionSpec = {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(durationMillis = 500)
                                ) togetherWith slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth },
                                    animationSpec = tween(durationMillis = 500)
                                )
                            },
                            label = "ScreenContentAnimation"
                        ) {
                            TestContent(mode, actioner)
                        }
                    }
                    is SpeakingExerciseViewState.ScreenMode.Speaking -> {
                        AnimatedContent(
                            targetState = mode.situation.id,
                            transitionSpec = {
                                slideInHorizontally(
                                    initialOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(durationMillis = 500)
                                ) togetherWith slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth },
                                    animationSpec = tween(durationMillis = 500)
                                )
                            },
                            label = "ScreenContentAnimation"
                        ) {
                            SpeakingContent(screenState.btnTooltipText, mode, actioner)
                            SpeakingResult(state = mode, actioner)
                        }
                    }
                    null -> {}
                }
                screenState.uiMessage?.let { uiMessage ->
                    when (val message = uiMessage.message) {
                        is SpeakingExerciseUiMessage.RequestRecordAudio -> {
                            permissionManager.AskPermission(Manifest.permission.RECORD_AUDIO) { isGranted ->
                                if (isGranted) {
                                    actioner(SpeakingExerciseAction.OnStartSpikingClicked)
                                }
                            }
                            onMessageShown(uiMessage.id)
                        }
                        is SpeakingExerciseUiMessage.ShowCorrectAnswerExplanation -> {
                            CorrectTestAnswer(uiMessage, message, onMessageShown, actioner)
                        }
                        is SpeakingExerciseUiMessage.ShowWrongAnswerExplanation -> {
                            WrongTestAnswer(uiMessage, message, onMessageShown)
                        }
                        is SpeakingExerciseUiMessage.NavigateToExerciseResult -> {
                            onNavigateToExerciseResult(message.time, message.experience)
                            onMessageShown(uiMessage.id)
                        }
                    }
                }
            }
        }
    }

@Composable
private fun TopBar(screenState: SpeakingExerciseViewState, actioner: (SpeakingExerciseAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(screenState.topBarBgRes),
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
                    .clickable { actioner(SpeakingExerciseAction.OnBackClicked) },
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

@Composable
private fun SpeakingContent(
    tooltipText: UiText,
    speakingMode: SpeakingExerciseViewState.ScreenMode.Speaking,
    actioner: (SpeakingExerciseAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .fillMaxSize()
    ) {
        Text(
            "\uD83E\uDDE0 Уяви ситуацію:",
            style = LinguaTypography.h5,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            speakingMode.situation.situationText ?: "",
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "\uD83C\uDFAF Твоя задача:",
            style = LinguaTypography.h5,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            speakingMode.situation.taskText,
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.Companion.weight(0.5f))
        RealtimeWaveform(
            speakingMode.amplitude ?: 0,
            speakingMode.isSpeaking,
            modifier = Modifier.Companion
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(Modifier.Companion.weight(1f))
        if (speakingMode.isRecording) {
            if (speakingMode.isStopRecordingBtnVisible){
                TextButton(
                    textColor = MaterialTheme.colorScheme.typoDisabled(),
                    text = "Закінчити Говорити",
                    onClick = {
                    actioner(SpeakingExerciseAction.OnStopSpeakingClicked)
                })
                Spacer(Modifier.height(16.dp))
            }

            if (speakingMode.secondsTillFinish > 0) {
                InactiveButton(text = "Кінець вправи через: ${speakingMode.secondsTillFinish}")
            } else {
                InactiveButton(text = "Говори...")
            }
        } else {
            if (tooltipText.asString().isEmpty().not()) {
                StaticTooltip(
                    enableFloatAnimation = true,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
                    triangleAlignment = Alignment.Start,
                    paddingHorizontal = 20.dp,
                    contentPadding = 16.dp,
                    cornerRadius = 12.dp,
                ) {
                    Text(
                        text = tooltipText.asString(),
                        color = MaterialTheme.colorScheme.typoDisabled(),
                        style = LinguaTypography.subtitle4
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            PrimaryButton(text = "Говорити") {
                actioner(SpeakingExerciseAction.OnStartSpikingClicked)
            }
        }
        Spacer(Modifier.padding(20.dp))
    }
}

@Composable
private fun TestContent(
    testMode: SpeakingExerciseViewState.ScreenMode.IntroTest,
    actioner: (SpeakingExerciseAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .fillMaxSize()
    ) {
        Text(
            "\uD83E\uDDE0 Уяви ситуацію:",
            style = LinguaTypography.h5,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            testMode.test.situationText ?: "",
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "\uD83C\uDFAF Твоя задача:",
            style = LinguaTypography.h5,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            testMode.test.taskText,
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.weight(1f))
        LazyColumn(
            Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(testMode.test.variants) { index, variant ->
                if (index > 0) Spacer(Modifier.height(20.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp, if (variant == testMode.chosenVariant) {
                                KellyGreen
                            } else {
                                MaterialTheme.colorScheme.onBackgroundDark()
                            }, RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                        .clickable { actioner(SpeakingExerciseAction.OnVariantChosen(variant)) },
                    text = variant.variantText,
                    color = MaterialTheme.colorScheme.typoPrimary(),
                    style = LinguaTypography.subtitle3
                )
            }
        }
        Spacer(Modifier.weight(1f))
        if (testMode.chosenVariant != null) {
            PrimaryButton(text = "Перевірити") {
                actioner(SpeakingExerciseAction.OnCheckTestVariantClicked)
            }
        } else {
            InactiveButton(text = "Перевірити")
        }

        Spacer(Modifier.padding(20.dp))
    }
}


@Composable
private fun BoxScope.WrongTestAnswer(
    uiMessage: UiMessage<SpeakingExerciseUiMessage>,
    message: SpeakingExerciseUiMessage.ShowWrongAnswerExplanation,
    onMessageShown: (id: Long) -> Unit
) {
    val isVisible = remember(uiMessage.id) { mutableStateOf(false) }

    LaunchedEffect(uiMessage.id) {
        isVisible.value = true
    }

    AnimatedVisibility(
        visible = isVisible.value,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 1000)
        ),
        modifier = Modifier.Companion.align(Alignment.BottomCenter)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(color = Red, shape = RoundedCornerShape(14.dp))
                    .border(2.dp, color = UeRed, shape = RoundedCornerShape(14.dp))
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = message.text,
                    style = LinguaTypography.subtitle3,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                SecondaryButton(text = "СПРОБУВАТИ ЗНОВУ", textColor = Red) {
                    isVisible.value = false

                    onMessageShown(uiMessage.id)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BoxScope.CorrectTestAnswer(
    uiMessage: UiMessage<SpeakingExerciseUiMessage>,
    message: SpeakingExerciseUiMessage.ShowCorrectAnswerExplanation,
    onMessageShown: (id: Long) -> Unit,
    actioner: (SpeakingExerciseAction) -> Unit
) {
    val isVisible = remember(uiMessage.id) { mutableStateOf(false) }

    LaunchedEffect(uiMessage.id) {
        isVisible.value = true
    }

    AnimatedVisibility(
        visible = isVisible.value,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 1000)
        ),
        modifier = Modifier.Companion.align(Alignment.BottomCenter)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(color = KellyGreen, shape = RoundedCornerShape(14.dp))
                    .border(2.dp, color = Avocado, shape = RoundedCornerShape(14.dp))
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = message.text,
                    style = LinguaTypography.subtitle3,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                SecondaryButton(text = "ДАЛІ", textColor = KellyGreen) {
                    isVisible.value = false
                    onMessageShown(uiMessage.id)
                    actioner(SpeakingExerciseAction.OnNextTestClicked)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BoxScope.SpeakingResult(
    state: SpeakingExerciseViewState.ScreenMode.Speaking,
    actioner: (SpeakingExerciseAction) -> Unit
) {
    AnimatedVisibility(
        visible = state.result.isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 1000)
        ),
        modifier = Modifier.Companion.align(Alignment.BottomCenter)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(color = KellyGreen, shape = RoundedCornerShape(14.dp))
                    .border(2.dp, color = Avocado, shape = RoundedCornerShape(14.dp))
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Класна спроба!",
                    style = LinguaTypography.h5,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Послухай себе — і якщо хочеш, зроби ще краще \uD83D\uDE09",
                    style = LinguaTypography.subtitle3,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                BoxWithStripes(
                    shadowOffset = 0.dp,
                    stripeColor = Black_3,
                    background = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackgroundDark(),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when{
                            state.result.isPlayingRecordPaused -> {
                                Icon(
                                    painter = painterResource(LinguaIcons.PlayCircle),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable { actioner(SpeakingExerciseAction.OnPlayRecordClicked) },
                                    contentDescription = "",
                                    tint = SonicSilver
                                )
                            }
                            state.result.isPlaying -> {
                                Icon(
                                    painter = painterResource(LinguaIcons.PauseCircle),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable { actioner(SpeakingExerciseAction.OnPauseRecordClicked) },
                                    contentDescription = "",
                                    tint = SonicSilver
                                )
                            }
                            else -> {
                                Icon(
                                    painter = painterResource(LinguaIcons.PlayCircle),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable { actioner(SpeakingExerciseAction.OnPlayRecordClicked) },
                                    contentDescription = "",
                                    tint = SonicSilver
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))
                        LinguaProgressBar(
                            state.result.playProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(18.dp),
                            progressColor = SonicSilver,
                            progressShadow = SonicSilver,
                            minValue = 0f
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                TextButton(text = "СПРОБУВАТИ ЗНОВУ", textColor = Color.White, onClick = {
                    actioner(SpeakingExerciseAction.OnTryAgainSituationClicked)
                })
                Spacer(Modifier.height(20.dp))
                SecondaryButton(text = "ДАЛІ", textColor = KellyGreen) {
                    actioner(SpeakingExerciseAction.OnNextSituationClicked)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun SpeakingExercisePreview() {
    LinguaBackground {
        LinguaTheme {
            Row {
                SpeakingExerciseScreen(
                    SpeakingExerciseViewState.PreviewSpeaking,
                    PermissionManager(LocalContext.current),
                    {},
                    {_, _ ->},
                    {})
            }
        }
    }
}