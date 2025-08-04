package com.korop.yaroslavhorach.game_description

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
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
import com.korop.yaroslavhorach.designsystem.theme.Golden
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography.popinsMedium
import com.korop.yaroslavhorach.designsystem.theme.Red
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.korop.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaAnimations
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.primaryIcon
import com.korop.yaroslavhorach.designsystem.theme.secondaryIcon
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoSecondary
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.game_description.model.GameDescriptionAction
import com.korop.yaroslavhorach.game_description.model.GameDescriptionViewState
import com.korop.yaroslavhorach.game_description.model.GameUi
import com.korop.yaroslavhorach.games.R
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
internal fun GameDescriptionRoute(
    viewModel: GameDescriptionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToGame: (id: Long, name: Game.GameName) -> Unit,
    onNavigateToPremium: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LinguaBackground {
        GameDescriptionScreen(
            state = state,
            actioner = { action ->
                when (action) {
                    is GameDescriptionAction.OnBackClicked -> {
                        onNavigateBack()
                    }
                    is GameDescriptionAction.OnStartGameClicked -> {
                        viewModel.submitAction(action)
                        onNavigateToGame(
                            state.game?.game?.id ?: 0,
                            state.game?.game?.name ?: Game.GameName.RAVEN_LIKE_A_CHAIR
                        )
                    }
                    is GameDescriptionAction.OnPremiumBtnClicked -> {
                        onNavigateToPremium()
                    }
                    else -> {
                        viewModel.submitAction(action)
                    }
                }
            },
        )
    }
}

@Composable
internal fun GameDescriptionScreen(
    state: GameDescriptionViewState,
    actioner: (GameDescriptionAction) -> Unit
) {
    val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        Spacer(Modifier.height(20.dp))
        Toolbar(actioner, state)
        Spacer(Modifier.weight(0.2f))
        when {
            state.experience < (state.game?.game?.minExperienceRequired ?: 0) && state.isUserPremium.not() -> {
                state.game?.let { DescriptionNotEnable(state, it, lang, actioner) }
            }
            state.availableTokens <= 0 && state.useToken && state.isUserPremium.not() -> {
                DescriptionNoTokens(state, actioner)
            }
            else -> {
                state.game?.let { Description(state, it, lang, actioner) }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun Toolbar(
    actioner: (GameDescriptionAction) -> Unit,
    state: GameDescriptionViewState
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .size(34.dp)
                .clickable { actioner(GameDescriptionAction.OnBackClicked) },
            tint = MaterialTheme.colorScheme.secondaryIcon(),
            painter = painterResource(LinguaIcons.Close),
            contentDescription = null
        )
        Spacer(Modifier.weight(1f))

        FavoriteMark(state, actioner)
    }
}

@Composable
private fun FavoriteMark(
    state: GameDescriptionViewState,
    actioner: (GameDescriptionAction) -> Unit
) {
    if (state.favorites.contains(state.game?.game?.id)) {
        Icon(
            modifier = Modifier
                .size(28.dp)
                .clickable { actioner(GameDescriptionAction.OnRemoveFavoritesClicked) },
            painter = painterResource(LinguaIcons.FavoriteFilled),
            contentDescription = null,
            tint = Red
        )
    } else {
        Icon(
            modifier = Modifier
                .size(28.dp)
                .clickable { actioner(GameDescriptionAction.OnAddToFavoritesClicked) },
            painter = painterResource(LinguaIcons.Favorite),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primaryIcon()
        )
    }
}

@Composable
private fun ColumnScope.DescriptionNotEnable(
    state: GameDescriptionViewState,
    game: GameUi,
    lang: String,
    actioner: (GameDescriptionAction) -> Unit
) {
    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Image(
            modifier = Modifier
                .size(150.dp),
            painter = painterResource(game.iconResId),
            contentDescription = ""
        )
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = (-10).dp, x = 15.dp)
                .size(65.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .padding(6.dp),
            tint = MaterialTheme.colorScheme.primaryIcon(),
            painter = painterResource(com.korop.yaroslavhorach.designsystem.R.drawable.ic_lock),
            contentDescription = ""
        )
    }

    Spacer(Modifier.Companion.weight(1f))
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = game.game.getNameText(lang),
        style = LinguaTypography.h2,
        color = MaterialTheme.colorScheme.typoPrimary()
    )
    Spacer(Modifier.height(10.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = stringResource(R.string.game_description_locked_title_text),
        style = LinguaTypography.body3,
        color = MaterialTheme.colorScheme.typoPrimary()
    )
    Spacer(Modifier.weight(1f))
    Text(
        text = stringResource(
            R.string.game_description_locked_message_text,
            (game.game.minExperienceRequired - state.experience.toFloat()).toInt()
        ),
        color = Golden,
        style = LinguaTypography.body3.copy(fontFamily = popinsMedium)
    )
    LinguaProgressBar(
        progress = (state.experience.toFloat() / game.game.minExperienceRequired.toFloat()),
        progressBackgroundColor = MaterialTheme.colorScheme.onBackgroundDark(),
        progressColor = Golden,
        progressBarHeight = 22.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 14.dp)
            .height(40.dp),
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .offset(x = 15.dp, y = (-7).dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(LinguaIcons.LockColor),
            contentDescription = ""
        )
    }
    Spacer(Modifier.Companion.weight(1f))

    PremiumButton(text = stringResource(R.string.game_description_no_tokens_premium_btn_text)) {
        actioner(GameDescriptionAction.OnPremiumBtnClicked)
    }
}

@Composable
private fun ColumnScope.DescriptionNoTokens(
    state: GameDescriptionViewState,
    actioner: (GameDescriptionAction) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(LinguaAnimations.Clock))

    LottieAnimation(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()
            .size(200.dp),
        iterations = LottieConstants.IterateForever,
        contentScale = ContentScale.FillHeight,
        composition = composition)

    Spacer(Modifier.Companion.weight(0.5f))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.game_description_tokens_are_out_text),
        color = MaterialTheme.colorScheme.typoPrimary(),
        textAlign = TextAlign.Center,
        style = LinguaTypography.h2,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.game_desctiption_tokens_are_out_subtitle_text),
        color = MaterialTheme.colorScheme.typoSecondary(),
        textAlign = TextAlign.Center,
        style = LinguaTypography.body3
    )
    Spacer(Modifier.weight(1f))
    PremiumButton(text = stringResource(R.string.game_description_premium_btn_text)) {
        actioner(GameDescriptionAction.OnPremiumBtnClicked)
    }
}

@Composable
private fun ColumnScope.Description(
    state: GameDescriptionViewState,
    game : GameUi,
    lang: String,
    actioner: (GameDescriptionAction) -> Unit
) {
    Image(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(150.dp),
        painter = painterResource(game.iconResId),
        contentDescription = null,
    )
    AnimatedStarsRow(game)
    Spacer(Modifier.Companion.weight(1f))
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = game.game.getNameText(lang),
        style = LinguaTypography.h2,
        color = MaterialTheme.colorScheme.typoPrimary()
    )
    Spacer(Modifier.height(10.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Justify,
        text = game.game.getDescriptionText(lang),
        style = LinguaTypography.body3,
        color = MaterialTheme.colorScheme.typoPrimary()
    )

    Spacer(Modifier.weight(1f))

    if ((state.game?.game?.stars ?: 0) >= 3) {
        Text(
            text = stringResource(R.string.game_description_got_all_stars_text, state.game?.game?.getNameText(lang).toString()),
            color = MaterialTheme.colorScheme.primary,
            style = LinguaTypography.body3.copy(fontFamily = popinsMedium)
        )
    } else {
        val tries = game.game.triesLeftTillNextStar

        Text(
            text = pluralStringResource(R.plurals.complete_more_times_to_star, tries, tries),
            color = MaterialTheme.colorScheme.primary,
            style = LinguaTypography.body3.copy(fontFamily = popinsMedium)
        )
    }

    LinguaProgressBar(
        progress = game.game.progressTillNextStar,
        progressBackgroundColor = MaterialTheme.colorScheme.onBackgroundDark(),
        progressColor = MaterialTheme.colorScheme.primary,
        progressBarHeight = 22.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 14.dp)
            .height(62.dp),
    ) {
        Icon(
            modifier = Modifier
                .width(62.dp)
                .height(60.dp)
                .offset(x = 20.dp, y = (-2.5).dp)
                .align(Alignment.CenterEnd),
            tint = Golden,
            painter = painterResource(LinguaIcons.icStarFilled),
            contentDescription = ""
        )
    }
    Spacer(Modifier.Companion.weight(1f))

    val btnText = if (state.useToken) {
        stringResource(R.string.game_description_start_btn_text)
    } else {
        stringResource(R.string.game_desctiption_start_with_no_tokens_btn_text)
    }

    PrimaryButton(text = btnText) {
        actioner(GameDescriptionAction.OnStartGameClicked)
    }

}

@Composable
fun ColumnScope.AnimatedStarsRow(game: GameUi) {
    val scale1 = remember { Animatable(1f) }
    val scale2 = remember { Animatable(1f) }
    val scale3 = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)

            scale1.animateTo(1.2f, animationSpec = tween(300))
            scale1.animateTo(1f, animationSpec = tween(300))
            delay(300)

            scale2.animateTo(1.2f, animationSpec = tween(300))
            scale2.animateTo(1f, animationSpec = tween(300))
            delay(300)

            scale3.animateTo(1.2f, animationSpec = tween(300))
            scale3.animateTo(1f, animationSpec = tween(300))

            delay(3500)
        }
    }

    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        Icon(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale1.value
                    scaleY = scale1.value
                }
                .size(width = 72.dp, height = 70.dp),
            painter = painterResource(LinguaIcons.icStarFilled),
            contentDescription = null,
            tint = if (game.game.stars >= 1) Golden else MaterialTheme.colorScheme.secondaryIcon()
        )
        Spacer(Modifier.width(24.dp))
        Icon(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale2.value
                    scaleY = scale2.value
                }
                .offset(y = 36.dp)
                .size(width = 72.dp, height = 70.dp),
            painter = painterResource(LinguaIcons.icStarFilled),
            contentDescription = null,
            tint = if (game.game.stars >= 2) Golden else MaterialTheme.colorScheme.secondaryIcon()
        )
        Spacer(Modifier.width(24.dp))
        Icon(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale3.value
                    scaleY = scale3.value
                }
                .size(width = 72.dp, height = 70.dp),
            painter = painterResource(LinguaIcons.icStarFilled),
            contentDescription = null,
            tint = if (game.game.stars >= 3) Golden else MaterialTheme.colorScheme.secondaryIcon()
        )
    }
}

@Preview
@Composable
private fun GameDescriptionNoTokensPreview() {
    LinguaBackground {
        LinguaTheme {
            GameDescriptionScreen(
                GameDescriptionViewState.PreviewNoTokens,
            ) {}
        }
    }
}

@Preview
@Composable
private fun GameDescriptionLockedPreview() {
    LinguaBackground {
        LinguaTheme {
            GameDescriptionScreen(
                GameDescriptionViewState.PreviewLocked,
            ) {}
        }
    }
}

@Preview
@Composable
private fun PremiumSuccessPreview() {
    LinguaBackground {
        LinguaTheme {
            GameDescriptionScreen(
                GameDescriptionViewState.Preview,
            ) {}
        }
    }
}