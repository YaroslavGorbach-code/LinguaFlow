package com.example.yaroslavhorach.exercises.exercise_completed

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.example.yaroslavhorach.designsystem.theme.components.StaticTooltip
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.example.yaroslavhorach.designsystem.theme.typoDisabled
import com.example.yaroslavhorach.designsystem.theme.typoSecondary
import com.example.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedAction
import com.example.yaroslavhorach.exercises.exercise_completed.model.ExerciseCompletedViewState
import com.example.yaroslavhorach.ui.SpeakingLevel

@Composable
internal fun ExerciseCompletedRoute(
    viewModel: ExerciseCompletedViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val exerciseCompletedViewState by viewModel.state.collectAsStateWithLifecycle()

    LinguaBackground {
        ExerciseCompletedScreen(
            state = exerciseCompletedViewState,
            actioner = { action ->
                when (action) {
                    is ExerciseCompletedAction.OnContinueClicked -> {
                        onNavigateBack()
                    }
                    else -> viewModel.submitAction(action)
                }
            },
        )
    }

    BackHandler {  }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
internal fun ExerciseCompletedScreen(
    state: ExerciseCompletedViewState,
    actioner: (ExerciseCompletedAction) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(Modifier.weight(0.6f))
            Column {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.StarInGlasses))

                LottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    contentScale = ContentScale.Crop,
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
                Spacer(Modifier.height(80.dp))
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
                        text = "Твій рівень вільного мовлення зріс! Так тримати \uD83D\uDC4F",
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        style = LinguaTypography.body4
                    )
                }
                val level = SpeakingLevel.fromExperience(state.allUserExperience)
                val progress = (state.allUserExperience - level.experienceRequired.first).toFloat() /
                        (level.experienceRequired.last - level.experienceRequired.first).toFloat()

                LinguaProgressBar(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .height(60.dp)
                        .fillMaxWidth(),
                    progress = progress,
                    progressBarHeight = 22.dp
                ) {
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .offset(x = 25.dp)
                            .padding(bottom = 15.dp)
                            .align(Alignment.CenterEnd),
                        painter = painterResource(LinguaIcons.Confetti),
                        contentDescription = ""
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 120.dp)
                            .border(
                                1.5.dp,
                                color = MaterialTheme.colorScheme.onBackgroundDark(),
                                RoundedCornerShape(9.dp)
                            )
                            .padding(vertical = 16.dp, horizontal = 18.dp)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Час",
                            color = MaterialTheme.colorScheme.typoDisabled(),
                            style = LinguaTypography.body4
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(LinguaIcons.Chronometer),
                                contentDescription = ""
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = state.time,
                                color = MaterialTheme.colorScheme.typoSecondary(),
                                style = LinguaTypography.subtitle4
                            )
                        }
                    }

                    Spacer(Modifier.weight(0.6f))

                    Column(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 120.dp)
                            .border(
                                1.5.dp,
                                color = MaterialTheme.colorScheme.onBackgroundDark(),
                                RoundedCornerShape(9.dp)
                            )
                            .padding(vertical = 16.dp, horizontal = 18.dp)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Досвід",
                            color = MaterialTheme.colorScheme.typoDisabled(),
                            style = LinguaTypography.body4
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(LinguaIcons.LightingThunder),
                                contentDescription = ""
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "+" + state.experience.toString(),
                                color = MaterialTheme.colorScheme.typoSecondary(),
                                style = LinguaTypography.subtitle4
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }

            }

            Spacer(Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
                text = "ПРОДОВЖИТИ",
            ) {
                actioner(ExerciseCompletedAction.OnContinueClicked)
            }
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