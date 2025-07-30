package com.korop.yaroslavhorach.block_practice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.korop.yaroslavhorach.block_practice.model.BlockPracticeAction
import com.korop.yaroslavhorach.block_practice.model.BlockPracticeViewState
import com.korop.yaroslavhorach.designsystem.extentions.blockPracticeMessage
import com.korop.yaroslavhorach.designsystem.extentions.blockTitle
import com.korop.yaroslavhorach.designsystem.theme.Golden
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.secondaryIcon
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoSecondary
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.home.R

@Composable
internal fun BlockPracticeRoute(
    viewModel: BlockPracticeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateExercises: (ExerciseBlock) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LinguaBackground {
        BlockPracticeRouteScreen(
            state = state,
            actioner = { action ->
                when(action){
                    is BlockPracticeAction.OnStartTrainingClicked -> {
                        state.block?.let { onNavigateExercises(it) }
                    }
                    is BlockPracticeAction.OnBackClicked -> { onNavigateBack() }
                    else -> { viewModel.submitAction(action) }
                }
            },
        )
    }
}

@Composable
internal fun BlockPracticeRouteScreen(
    state:BlockPracticeViewState,
    actioner: (BlockPracticeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        Spacer(Modifier.height(20.dp))
        Toolbar(actioner, state)
        Spacer(Modifier.height(20.dp))
        StarCounter(state.stars, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.weight(0.4f))
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Star))
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .size(200.dp),
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop,
            composition = composition,
        )
        Spacer(Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.block_practice_title_text),
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = state.block?.blockPracticeMessage()?.asString() ?: "",
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))
        PrimaryButton(text = stringResource(R.string.block_practice_primary_btn_text)) {
            actioner(BlockPracticeAction.OnStartTrainingClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun StarCounter(
    starCount: Int,
    modifier: Modifier = Modifier,
) {
    val animatedCount by animateIntAsState(
        targetValue = starCount,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "starCountAnim"
    )

    val starColor = if (animatedCount == 0) {
        MaterialTheme.colorScheme.secondaryIcon()
    } else {
        Golden
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(90.dp),
            tint = starColor,
            painter = painterResource(LinguaIcons.icStarFilled),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = animatedCount.toString(),
            style = LinguaTypography.subtitle1.copy(fontSize = 28.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = starColor
        )
    }
}

@Composable
private fun Toolbar(
    actioner: (BlockPracticeAction) -> Unit,
    state: BlockPracticeViewState
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .size(34.dp)
                .clickable { actioner(BlockPracticeAction.OnBackClicked) },
            tint = MaterialTheme.colorScheme.secondaryIcon(),
            painter = painterResource(LinguaIcons.Close),
            contentDescription = null
        )
        Spacer(Modifier.width(20.dp))
        Text(
            text = state.block?.blockTitle()?.asString() ?: "",
            style = LinguaTypography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.typoSecondary()
        )
    }
}

@Preview
@Composable
private fun PremiumSuccessPreview() {
    LinguaBackground {
        LinguaTheme {
            BlockPracticeRouteScreen(
                BlockPracticeViewState.Preview,
            ) {}
        }
    }
}