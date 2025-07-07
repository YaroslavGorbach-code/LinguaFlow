package com.korop.yaroslavhorach.designsystem.screens.premium

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.korop.yaroslavhorach.common.helpers.BillingManager.Companion.IN_6_MONTHLY_SUBSCRIPTION
import com.korop.yaroslavhorach.common.helpers.BillingManager.Companion.IN_MONTHLY_SUBSCRIPTION
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumAction
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumUiMessage
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumVariant
import com.korop.yaroslavhorach.designsystem.screens.premium.model.PremiumViewState
import com.korop.yaroslavhorach.designsystem.theme.Golden
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoSecondary
import com.korop.yaroslavhorach.ui.UiText

@Composable
internal fun PremiumRoute(
    viewModel: PremiumViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSuccess: () -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity

    LinguaBackground {
        PremiumScreen(
            state = viewState,
            actioner = { action ->
                when (action) {
                    is PremiumAction.OnBack -> {
                        onNavigateBack()
                    }
                    else -> viewModel.submitAction(action)
                }
            },
        )
    }

    viewState.uiMessage?.let { uiMessage ->
        when (uiMessage.message) {
            is PremiumUiMessage.NavigateToSuccess -> {
                onNavigateToSuccess()
                viewModel.clearMessage(uiMessage.id)
            }
            is PremiumUiMessage.ShowLifeTimeSubscriptionDialog -> {
                viewModel.billingManager.showOneTimeProduct(activity)
                viewModel.clearMessage(uiMessage.id)
            }
            is PremiumUiMessage.Show6MonthSubscriptionDialog -> {
                viewModel.billingManager.showSubscription(activity, subscriptionId = IN_6_MONTHLY_SUBSCRIPTION)
                viewModel.clearMessage(uiMessage.id)
            }
            is PremiumUiMessage.ShowMonthSubscriptionDialog -> {
                viewModel.billingManager.showSubscription(activity, subscriptionId = IN_MONTHLY_SUBSCRIPTION)
                viewModel.clearMessage(uiMessage.id)
            }
        }
    }
}

@Composable
internal fun PremiumScreen(
    state: PremiumViewState,
    actioner: (PremiumAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Premium))

        Image(
            modifier = Modifier
                .size(50.dp)
                .clickable { actioner(PremiumAction.OnBack) },
            painter = painterResource(LinguaIcons.CircleCloseDark),
            contentDescription = null
        )
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(200.dp),
            contentScale = ContentScale.Crop,
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.premium_title_text),
            style = LinguaTypography.h4,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
        Spacer(Modifier.height(20.dp))
        Column(Modifier.padding(horizontal = 14.dp)) {
            PremiumBenefit(stringResource(R.string.premium_benefit_1_text))
            Spacer(Modifier.height(14.dp))
            PremiumBenefit(stringResource(R.string.premium_benefit_2_text))
            Spacer(Modifier.height(14.dp))
            PremiumBenefit(stringResource(R.string.premium_benefit_3_text))
            Spacer(Modifier.height(14.dp))
            PremiumBenefit(stringResource(R.string.premium_benefit_4_text))
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.premium_variants_title_text),
            color = MaterialTheme.colorScheme.typoPrimary(),
            style = LinguaTypography.subtitle2
        )
        state.variants.forEach { variant ->
            when (variant) {
                is PremiumVariant.Month -> {
                    Spacer(Modifier.height(16.dp))
                }
                is PremiumVariant.SixMonth,
                is PremiumVariant.Forever -> {
                    Spacer(Modifier.height(20.dp))
                }
            }
            SubscriptionVariant(variant) { actioner(PremiumAction.OnVariantChosen(variant)) }
        }

        Spacer(Modifier.height(32.dp))
        PremiumButton(text = stringResource(R.string.premium_primary_btn_text)) {
            actioner(PremiumAction.OnGetPremiumClicked)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun SubscriptionVariant(
    variant: PremiumVariant,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (variant.isSelected) Golden else MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(9.dp)
                )
                .clip(RoundedCornerShape(9.dp))
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (variant.isSelected) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(R.drawable.ic_check_circle_checked),
                            contentDescription = null,
                            tint = Golden
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackgroundDark()
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.align(Alignment.Top)) {
                        Text(
                            text = variant.title.asString(),
                            color = MaterialTheme.colorScheme.typoPrimary(),
                            style = LinguaTypography.subtitle3
                        )
                        if (variant is PremiumVariant.Forever) {
                            Text(
                                text = stringResource(R.string.premium_screen_subscription_forever_subtitle_text),
                                color = MaterialTheme.colorScheme.typoSecondary(),
                                style = LinguaTypography.body6
                            )
                        }
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(end = 20.dp, top = 6.dp),
                    textAlign = TextAlign.End,
                    text = variant.priceFormated.asString(),
                    color = MaterialTheme.colorScheme.typoPrimary(),
                    style = LinguaTypography.subtitle3
                )
            }
        }

        if (variant.badgeText !is UiText.Empty) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-10).dp)
                    .background(
                        color = Golden,
                        shape = RoundedCornerShape(
                            bottomStart = 8.dp,
                            topEnd = 8.dp
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = variant.badgeText.asString(),
                    color = Color.White,
                    style = LinguaTypography.body5
                )
            }
        }
    }
}

@Composable
private fun PremiumBenefit(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.ic_premium_check_badge), null)
        Spacer(Modifier.width(8.dp))
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
private fun PremiumPreview() {
    LinguaBackground {
        LinguaTheme {
            PremiumScreen(
                PremiumViewState.Preview,
            ) {}
        }
    }
}