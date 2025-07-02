package com.example.yaroslavhorach.exercises.vocabulary

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.common.utill.UiMessage
import com.example.yaroslavhorach.designsystem.theme.Avocado
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.Red
import com.example.yaroslavhorach.designsystem.theme.UeRed
import com.example.yaroslavhorach.designsystem.theme.components.InactiveButton
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.example.yaroslavhorach.designsystem.theme.components.SecondaryButton
import com.example.yaroslavhorach.designsystem.theme.components.SectionedLinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.StaticTooltip
import com.example.yaroslavhorach.designsystem.theme.components.TextButton
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.example.yaroslavhorach.designsystem.theme.typoDisabled
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.designsystem.extentions.topBarBgRes
import com.example.yaroslavhorach.exercises.R
import com.example.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseAction
import com.example.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseUiMessage
import com.example.yaroslavhorach.exercises.vocabulary.model.VocabularyExerciseViewState
import com.example.yaroslavhorach.ui.utils.conditional
import kotlinx.coroutines.delay

@Composable
internal fun VocabularyExerciseRoute(
    viewModel: VocabularyExerciseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit
) {
    val vocabularyExerciseViewState by viewModel.state.collectAsStateWithLifecycle()

    VocabularyExerciseScreen(
        screenState = vocabularyExerciseViewState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {
                VocabularyExerciseAction.OnBackClicked -> onNavigateBack()
                else -> viewModel.submitAction(action)
            }
        },
        onNavigateToExerciseResult = onNavigateToExerciseResult
    )
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun VocabularyExerciseScreen(
    screenState: VocabularyExerciseViewState,
    onMessageShown: (id: Long) -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit,
    actioner: (VocabularyExerciseAction) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        TopBar(screenState, actioner)
        Box(
            modifier = Modifier
                .padding(top = 150.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
        ) {
            VocabularyContent(screenState, actioner)

            screenState.uiMessage?.let { uiMessage ->
                when (val message = uiMessage.message) {
                    is VocabularyExerciseUiMessage.ShowCorrectResult -> {
                        GoodResult(uiMessage, message, onMessageShown, actioner)
                    }
                    is VocabularyExerciseUiMessage.ShowNotBadResult -> {
                        NotBadResult(uiMessage, message, onMessageShown, actioner)
                    }
                    is VocabularyExerciseUiMessage.ShowBadResult -> {
                        BadResult(uiMessage, message, onMessageShown, actioner)
                    }
                    is VocabularyExerciseUiMessage.NavigateToExerciseResult -> {
                        onNavigateToExerciseResult(message.time, message.experience)
                        onMessageShown(uiMessage.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(screenState: VocabularyExerciseViewState, actioner: (VocabularyExerciseAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(screenState.exerciseBlock.topBarBgRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
        Row(
            Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp, end = 36.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .clickable { actioner(VocabularyExerciseAction.OnBackClicked) },
                painter = painterResource(LinguaIcons.CircleClose),
                contentDescription = null
            )
            Spacer(Modifier.width(18.dp))

            SectionedLinguaProgressBar(
                currentValue = screenState.wordsAmount,
                sections = screenState.vocabulary?.getProgressRangeValues() ?: emptyList()
            )
        }
    }
}

@Composable
private fun VocabularyContent(
    screenState: VocabularyExerciseViewState,
    actioner: (VocabularyExerciseAction) -> Unit
) {
    if (screenState.vocabulary != null) {
        Column(
            modifier = Modifier
                .conditional(screenState.isExerciseActive) {
                    clickable { actioner(VocabularyExerciseAction.OnScreenClicked) }
                }
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp)
                .fillMaxSize()
        ) {
            Text(
                stringResource(R.string.vocabulary_exercise_task_title_text),
                style = LinguaTypography.h5,
                color = MaterialTheme.colorScheme.typoPrimary()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                screenState.vocabulary.taskText,
                style = LinguaTypography.body3,
                color = MaterialTheme.colorScheme.typoPrimary()
            )
            Spacer(Modifier.height(24.dp))
            Text(
                stringResource(R.string.vocabulary_exercise_example_title_text),
                style = LinguaTypography.h5,
                color = MaterialTheme.colorScheme.typoPrimary()
            )
            Spacer(Modifier.height(10.dp))
            Text(
                screenState.vocabulary.exampleText,
                style = LinguaTypography.body3,
                color = MaterialTheme.colorScheme.typoPrimary()
            )
            Spacer(Modifier.Companion.weight(0.8f))

            if (screenState.isExerciseActive) {
                Box(Modifier.fillMaxWidth()) {
                    CircularTimer(modifier = Modifier.align(Alignment.Center), isRunning = true, onFinished = {
                        actioner(VocabularyExerciseAction.OnTimerFinished)
                    })
                }
            }

            Spacer(Modifier.Companion.weight(1f))

            if (screenState.isExerciseActive) {
                InactiveButton(text = stringResource(R.string.vocabulary_exercise_next_btn_text))
            } else {
                StaticTooltip(
                    enableFloatAnimation = true,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
                    triangleAlignment = Alignment.Start,
                    paddingHorizontal = 20.dp,
                    contentPadding = 16.dp,
                    cornerRadius = 12.dp,
                    borderSize = 1.5.dp
                ) {
                    Text(
                        text = stringResource(R.string.vocabulary_exercise_motivation_text),
                        color = MaterialTheme.colorScheme.typoPrimary(),
                        style = LinguaTypography.body4
                    )
                }
                Spacer(Modifier.height(16.dp))
                PrimaryButton(text = stringResource(R.string.vocabulary_exercise_start_text)) {
                    actioner(VocabularyExerciseAction.OnStartClicked)
                }
            }
            Spacer(Modifier.padding(20.dp))
        }
    }
}

@Composable
private fun CircularTimer(
    modifier: Modifier = Modifier,
    totalTime: Int = 60,
    isRunning: Boolean,
    strokeWidth: Dp = 18.dp,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    bgColor: Color = MaterialTheme.colorScheme.onBackgroundDark(),
    onFinished: () -> Unit = {}
) {
    val timeLeft = remember { mutableIntStateOf(totalTime) }
    val internalRunning = remember { mutableStateOf(false) }

    val progress by animateFloatAsState(
        targetValue = timeLeft.intValue / totalTime.toFloat(),
        animationSpec = tween(1000),
        label = "TimerProgress"
    )

    LaunchedEffect(isRunning) {
        if (isRunning && !internalRunning.value && timeLeft.intValue > 0) {
            internalRunning.value = true
            while (timeLeft.intValue > 0) {
                delay(1000)
                timeLeft.intValue--
            }
            if (timeLeft.intValue == 0) {
                onFinished()
            }
            internalRunning.value = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = strokeWidth.toPx()
            drawArc(
                color = bgColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = strokeWidth.toPx()
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = -360f * progress,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        Text(
            text = "${timeLeft.intValue}s",
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
    }
}

@Composable
private fun BoxScope.BadResult(
    uiMessage: UiMessage<VocabularyExerciseUiMessage>,
    message: VocabularyExerciseUiMessage.ShowBadResult,
    onMessageShown: (id: Long) -> Unit,
    actioner: (VocabularyExerciseAction) -> Unit
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
                    text = stringResource(R.string.vocabulary_exercise_result_title_text) + message.amountWords,
                    style = LinguaTypography.h4,
                    color = Color.White
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = message.text,
                    style = LinguaTypography.subtitle3,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                SecondaryButton(text = stringResource(R.string.vocabulary_exercise_result_try_again_btn_text), textColor = Red) {
                    isVisible.value = false
                    actioner(VocabularyExerciseAction.OnTryAgainClicked)
                    onMessageShown(uiMessage.id)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BoxScope.GoodResult(
    uiMessage: UiMessage<VocabularyExerciseUiMessage>,
    message: VocabularyExerciseUiMessage.ShowCorrectResult,
    onMessageShown: (id: Long) -> Unit,
    actioner: (VocabularyExerciseAction) -> Unit
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
                    text = stringResource(R.string.voacabulary_exercise_result_title_text) + message.amountWords,
                    style = LinguaTypography.h4,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = message.text,
                    style = LinguaTypography.subtitle3,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                SecondaryButton(text = stringResource(R.string.vocabulary_exercise_result_next_btn_text), textColor = KellyGreen) {
                    isVisible.value = false
                    onMessageShown(uiMessage.id)
                    actioner(VocabularyExerciseAction.OnNextClicked)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BoxScope.NotBadResult(
    uiMessage: UiMessage<VocabularyExerciseUiMessage>,
    message: VocabularyExerciseUiMessage.ShowNotBadResult,
    onMessageShown: (id: Long) -> Unit,
    actioner: (VocabularyExerciseAction) -> Unit
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
                    text = stringResource(R.string.voacabulary_exercise_result_title_text) + message.amountWords,
                    style = LinguaTypography.h4,
                    color = Color.White
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = message.text,
                    style = LinguaTypography.subtitle3,
                    color = Color.White
                )
                Spacer(Modifier.height(20.dp))
                TextButton(text = stringResource(R.string.vocabulary_exercise_result_try_again_btn_text), textColor = Color.White, onClick = {
                    isVisible.value = false
                    onMessageShown(uiMessage.id)
                    actioner(VocabularyExerciseAction.OnTryAgainClicked)
                })
                Spacer(Modifier.height(20.dp))
                SecondaryButton(text = stringResource(R.string.vocabulary_exercise_next_btn_text), textColor = KellyGreen) {
                    isVisible.value = false
                    onMessageShown(uiMessage.id)
                    actioner(VocabularyExerciseAction.OnNextClicked)
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
                VocabularyExerciseScreen(
                    VocabularyExerciseViewState.Preview,
                    {},
                    { _, _ -> },
                    {}
                )
            }
        }
    }
}