package com.example.yaroslavhorach.games.words_game

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.yaroslavhorach.designsystem.extentions.topBarBgRes
import com.example.yaroslavhorach.designsystem.theme.White
import com.example.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.example.yaroslavhorach.games.words_game.model.WordsGameAction
import com.example.yaroslavhorach.games.words_game.model.WordsGameUiMessage
import com.example.yaroslavhorach.games.words_game.model.WordsGameViewState

@Composable
internal fun WordsGameRoute(
    viewModel: WordsGameViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit
) {
    val wordsGameViewState by viewModel.state.collectAsStateWithLifecycle()

    WordsGameScreen(
        screenState = wordsGameViewState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {
                WordsGameAction.OnBackClicked -> onNavigateBack()
                else -> viewModel.submitAction(action)
            }
        },
        onNavigateToExerciseResult = onNavigateToExerciseResult
    )
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun WordsGameScreen(
    screenState: WordsGameViewState,
    onMessageShown: (id: Long) -> Unit,
    onNavigateToExerciseResult: (time: Long, experience: Int) -> Unit,
    actioner: (WordsGameAction) -> Unit
) {
    screenState.uiMessage?.let { uiMessage ->
        when (val message = uiMessage.message) {
            is WordsGameUiMessage.NavigateToExerciseResult -> {
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
            GameContent(screenState, actioner)
        }
    }
}


@Composable
private fun TopBar(screenState: WordsGameViewState, actioner: (WordsGameAction) -> Unit) {
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
                    .clickable { actioner(WordsGameAction.OnBackClicked) },
                painter = painterResource(LinguaIcons.CircleClose),
                contentDescription = null
            )
            Spacer(Modifier.width(18.dp))
            LinguaProgressBar(
                screenState.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ){
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = 10.dp)
                        .align(Alignment.CenterEnd),
                    painter = painterResource(LinguaIcons.LightingThunder),
                    contentDescription = ""
                )
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
private fun GameContent(
    state: WordsGameViewState,
    actioner: (WordsGameAction) -> Unit
) {
    val allowAnimate = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "\uD83C\uDFAF Твоя задача:",
                    style = LinguaTypography.h5,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    state.game?.taskText ?: "",
                    style = LinguaTypography.body3,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    "\uD83D\uDCCB Наприклад:",
                    style = LinguaTypography.h5,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    state.game?.exampleText ?: "",
                    style = LinguaTypography.body3,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
            }

            AnimatedContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                targetState = state.words,
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
                        EnterTransition.None togetherWith ExitTransition.None
                    }
                },
                label = "TwistTextAnimation"
            ) { words ->
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(Modifier.weight(0.6f))

                    when (words.size) {
                        1 -> { /* TODO */ }
                        2 -> { TwoWords(state.copy(words = words)) }
                        3 -> { /* TODO */ }
                        4 -> { /* TODO */ }
                        else -> {
                            Text(
                                text = words.joinToString(","),
                                style = LinguaTypography.body1.copy(fontSize = 24.sp),
                                color = MaterialTheme.colorScheme.typoPrimary()
                            )
                        }
                    }

                    Spacer(Modifier.weight(1f))
                }
            }
        }

        PrimaryButton(
            modifier = Modifier, text = if (state.isLastExercise) "Закінчити" else "Далі"
        ) {
            allowAnimate.value = true
            actioner(WordsGameAction.OnNextClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun TwoWords(state: WordsGameViewState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BoxWithStripes(
            rotation = -15f,
            borderColor = MaterialTheme.colorScheme.secondary,
            rawShadowYOffset = 0.dp,
            borderWidth = 1.dp,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                text = state.words[0],
                textAlign = TextAlign.Center,
                style = LinguaTypography.body1.copy(fontSize = 24.sp),
                color = White
            )
        }
        BoxWithStripes(
            rotation = 15f,
            borderColor = MaterialTheme.colorScheme.secondary,
            rawShadowYOffset = 0.dp,
            borderWidth = 1.dp,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                text = state.words[1],
                textAlign = TextAlign.Center,
                style = LinguaTypography.body1.copy(fontSize = 24.sp),
                color = White
            )
        }
    }
}

@Preview
@Composable
private fun SpeakingExercisePreview() {
    LinguaBackground {
        LinguaTheme {
            Row {
                WordsGameScreen(
                    screenState = WordsGameViewState.PreviewSpeaking,
                    {},
                    { _, _ -> },
                    {})
            }
        }
    }
}