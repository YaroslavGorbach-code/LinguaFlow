package com.korop.yaroslavhorach.block_is_locked

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.korop.yaroslavhorach.block_is_locked.model.BlockIsLockedAction
import com.korop.yaroslavhorach.block_is_locked.model.BlockIsLockedViewState
import com.korop.yaroslavhorach.designsystem.extentions.blockTitle
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.korop.yaroslavhorach.designsystem.theme.components.TextButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.secondaryIcon
import com.korop.yaroslavhorach.designsystem.theme.typoDisabled
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoSecondary
import com.korop.yaroslavhorach.home.R

@Composable
internal fun BlockIsLockedRoute(
    viewModel: BlockIsLockedViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onGoToPremium: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LinguaBackground {
        BlockIsLockedScreen(
            state = state,
            actioner = { action ->
                when(action){
                    is BlockIsLockedAction.OnGoToPremiumClicked -> {
                        state.block?.let { onGoToPremium() }
                    }
                    is BlockIsLockedAction.OnBackClicked -> { onNavigateBack() }
                    else -> { viewModel.submitAction(action) }
                }
            },
        )
    }
}

@Composable
internal fun BlockIsLockedScreen(
    state:BlockIsLockedViewState,
    actioner: (BlockIsLockedAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        Spacer(Modifier.height(20.dp))
        Toolbar(actioner, state)
        Spacer(Modifier.height(20.dp))
        Spacer(Modifier.weight(0.8f))
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Lock))
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .size(300.dp),
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.FillHeight,
            composition = composition,
        )
        Spacer(Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.blok_is_locked_title_text),
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(10.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.blok_is_locked_subtitle_text),
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))
        TextButton(text = stringResource(R.string.block_is_locked_secondary_btn_text), textColor = MaterialTheme.colorScheme.typoDisabled(), onClick = {
            actioner(BlockIsLockedAction.OnBackClicked)
        })
        Spacer(Modifier.height(20.dp))
        PremiumButton(text = stringResource(R.string.block_is_locked_primary_btn_text)) {
            actioner(BlockIsLockedAction.OnGoToPremiumClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun Toolbar(
    actioner: (BlockIsLockedAction) -> Unit,
    state: BlockIsLockedViewState
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .size(34.dp)
                .clickable { actioner(BlockIsLockedAction.OnBackClicked) },
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
            BlockIsLockedScreen(
                BlockIsLockedViewState.Preview,
            ) {}
        }
    }
}