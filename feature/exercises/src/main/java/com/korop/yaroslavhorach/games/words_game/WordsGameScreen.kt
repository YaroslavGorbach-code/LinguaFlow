package com.korop.yaroslavhorach.games.words_game

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korop.yaroslavhorach.designsystem.extentions.topBarBgRes
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.White
import com.korop.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.exercises.R
import com.korop.yaroslavhorach.games.words_game.model.WordsGameAction
import com.korop.yaroslavhorach.games.words_game.model.WordsGameUiMessage
import com.korop.yaroslavhorach.games.words_game.model.WordsGameViewState
import java.util.Locale

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
    val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .padding(top = 24.dp)
            .fillMaxSize()
    ) {
        Column(Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    stringResource(R.string.words_game_task_title_text),
                    style = LinguaTypography.h5,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    state.game?.getTaskText(lang) ?: "",
                    style = LinguaTypography.body3,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.words_game_example_title_text),
                    style = LinguaTypography.h5,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    state.game?.getExampleText(lang) ?: "",
                    style = LinguaTypography.body3,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
            }

            when(val mode = state.screenMode){
                is WordsGameViewState.ScreenMode.Sentence -> {
                    SentenceContent(mode, allowAnimate)
                }
                is WordsGameViewState.ScreenMode.Words -> {
                    WordsContent(mode, allowAnimate)
                }
            }
        }

        PrimaryButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = if (state.isLastExercise) stringResource(R.string.words_game_finish_btn_text) else stringResource(R.string.words_game_next_btn_text)
        ) {
            allowAnimate.value = true
            actioner(WordsGameAction.OnNextClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun ColumnScope.SentenceContent(
    mode: WordsGameViewState.ScreenMode.Sentence,
    allowAnimate: MutableState<Boolean>
) {
    AnimatedContent(
        modifier = Modifier.Companion
            .weight(1f)
            .fillMaxWidth(),
        targetState = mode.sentence,
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
    ) { sentence ->
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.weight(1f))
            Sentence(mode.copy(sentence = sentence))
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun ColumnScope.WordsContent(
    mode: WordsGameViewState.ScreenMode.Words,
    allowAnimate: MutableState<Boolean>
) {
    AnimatedContent(
        modifier = Modifier.Companion
            .weight(1f)
            .fillMaxWidth(),
        targetState = mode.words,
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
            Spacer(Modifier.weight(1f))

            when (words.size) {
                1 -> OneWord(mode.copy(words = words))
                2 -> TwoWords(mode.copy(words = words))
//                3 -> ThreeWords(mode.copy(words = words))
//                4 -> FourWords(mode.copy(words = words))
                else -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        text = words.joinToString(", "),
                        style = LinguaTypography.body1.copy(fontSize = 28.sp),
                        color = MaterialTheme.colorScheme.typoPrimary()
                    )
                }
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun OneWord(state: WordsGameViewState.ScreenMode.Words) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        BoxWithStripes(
            borderColor = MaterialTheme.colorScheme.secondary,
            rawShadowYOffset = 0.dp,
            borderWidth = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
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
    }
}

@Composable
private fun Sentence(state: WordsGameViewState.ScreenMode.Sentence) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = state.sentence,
            style = LinguaTypography.body1.copy(fontSize = 24.sp),
            color = MaterialTheme.colorScheme.typoPrimary()
        )
    }
}

@Composable
private fun TwoWords(mode: WordsGameViewState.ScreenMode.Words) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
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
                text = mode.words[0],
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
                text = mode.words[1],
                textAlign = TextAlign.Center,
                style = LinguaTypography.body1.copy(fontSize = 24.sp),
                color = White
            )
        }
    }
}

@Composable
private fun FourWords(mode: WordsGameViewState.ScreenMode.Words) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
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
                    text = mode.words[0],
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
                    text = mode.words[1],
                    textAlign = TextAlign.Center,
                    style = LinguaTypography.body1.copy(fontSize = 24.sp),
                    color = White
                )
            }
        }
        Spacer(Modifier.height(16.dp))
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
                    text = mode.words[2],
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
                    text = mode.words[3],
                    textAlign = TextAlign.Center,
                    style = LinguaTypography.body1.copy(fontSize = 24.sp),
                    color = White
                )
            }
        }
    }
}

@Composable
private fun ThreeWords(mode: WordsGameViewState.ScreenMode.Words) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
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
                    text = mode.words[0],
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
                    text = mode.words[1],
                    textAlign = TextAlign.Center,
                    style = LinguaTypography.body1.copy(fontSize = 24.sp),
                    color = White
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BoxWithStripes(
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
                    text = mode.words[2],
                    textAlign = TextAlign.Center,
                    style = LinguaTypography.body1.copy(fontSize = 24.sp),
                    color = White
                )
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
                WordsGameScreen(
                    screenState = WordsGameViewState.PreviewSpeaking,
                    {},
                    { _, _ -> },
                    {})
            }
        }
    }
}
