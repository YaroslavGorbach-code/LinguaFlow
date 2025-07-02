package com.example.yaroslavhorach.designsystem.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.yaroslavhorach.designsystem.R
import com.example.yaroslavhorach.designsystem.screens.onboarding.model.OnboardingAction
import com.example.yaroslavhorach.designsystem.screens.onboarding.model.OnboardingViewState
import com.example.yaroslavhorach.designsystem.theme.Avocado
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.example.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.example.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.example.yaroslavhorach.designsystem.theme.typoPrimary

@Composable
internal fun OnboardingRoute(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToAvatarChange: () -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    LinguaBackground {
        OnboardingScreen(
            state = viewState,
            actioner = { action ->
                when (action) {
                    OnboardingAction.OnStartClicked -> {
                        onNavigateToAvatarChange()
                    }
                    else -> viewModel.submitAction(action)
                }
            }
        )
    }
}

@Composable
internal fun OnboardingScreen(
    state: OnboardingViewState,
    actioner: (OnboardingAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        val visible = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible.value = true }

        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                animationSpec = tween(600),
                initialOffsetY = { fullHeight -> fullHeight / 10 }
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(20.dp))

                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Conversation))

                LottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    reverseOnRepeat = true
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Говорити впевнено — це навичка. І ти її прокачаєш тут 👇",
                    style = LinguaTypography.h4,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.height(24.dp))
                AppBenefit("🗣️ Скоромовки — чіткість, ритм, впевненість")
                Spacer(Modifier.height(12.dp))
                AppBenefit("🎯 Мовні ігри — швидка реакція, дотепність")
                Spacer(Modifier.height(12.dp))
                AppBenefit("🎬 Життєві сцени — як відповідати у реальних діалогах")
                Spacer(Modifier.height(12.dp))
                AppBenefit("📚 Нові слова — говори цікаво і різноманітно")
                Spacer(Modifier.height(12.dp))
                AppBenefit("🎙️ Імпровізації — впевненість без сценарію")
            }
        }
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(24.dp))

        PrimaryButton(text = "ПОЧАТИ") {
            actioner(OnboardingAction.OnStartClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun AppBenefit(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
//        Icon(painterResource(R.drawable.ic_premium_check_badge), null, tint = Avocado)
//        Spacer(Modifier.width(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = text,
            style = LinguaTypography.body3,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
    }
}

@Preview
@Composable
private fun OnboardingPreview() {
    LinguaTheme {
        OnboardingScreen(
            OnboardingViewState.Preview,
        ) {}
    }
}