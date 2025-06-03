package com.example.yaroslavhorach.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.designsystem.theme.Black_3
import com.example.yaroslavhorach.designsystem.theme.Golden
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.White
import com.example.yaroslavhorach.designsystem.theme.White_40
import com.example.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.example.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.PremiumButton
import com.example.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.example.yaroslavhorach.designsystem.theme.components.StaticTooltip
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.example.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.designsystem.theme.typoSecondary
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.home.model.GameUi
import com.example.yaroslavhorach.home.model.GamesAction
import com.example.yaroslavhorach.home.model.GamesViewState

@Composable
internal fun GamesRoute(
    onNavigateToGame: (Exercise) -> Unit,
    viewModel: GamesViewModel = hiltViewModel(),
) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()

    GamesScreen(
        state = homeState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {
                else -> viewModel.submitAction(action)
            }
        })
}

@Composable
internal fun GamesScreen(
    state: GamesViewState,
    onMessageShown: (id: Long) -> Unit,
    actioner: (GamesAction) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(Modifier.fillMaxSize()) {
        TopBar(state, listState, actioner)
        LazyColumn(modifier = Modifier.padding(horizontal = 20.dp), state = listState) {
            itemsIndexed(state.games) { index, item ->
                Spacer(Modifier.height(20.dp))
                Game(state, item, listState, actioner)
            }
        }
    }
}

@Composable
private fun TopBar(screenState: GamesViewState, listState: LazyListState, actioner: (GamesAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(com.example.yaroslavhorach.designsystem.R.drawable.im_games_gradient),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 20.dp, top = 12.dp, end = 20.dp)
        ) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\uD83C\uDFB2 Мовні ігри",
                        color = MaterialTheme.colorScheme.typoPrimary(),
                        style = LinguaTypography.h2
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Тренуй мовлення в ігрових режимах: трохи гумору, трохи фентезі, і максимум користі",
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        style = LinguaTypography.body5
                    )
                }
                Spacer(Modifier.width(8.dp))
                Tokens(screenState)
            }

            Spacer(Modifier.height(20.dp))
            Challenge(listState)
        }
    }
}

@Composable
private fun Tokens(screenState: GamesViewState) {
    Row(
        modifier = Modifier
            .background(White_40, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(LinguaIcons.Token),
            contentDescription = null
        )
        Spacer(Modifier.width(6.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = screenState.availableTokens.toString() + "/" + screenState.maxTokens,
            color = MaterialTheme.colorScheme.typoPrimary(),
            style = LinguaTypography.subtitle3
        )
    }
}

@Composable
private fun Challenge(listState: LazyListState) {
    val isCollapsed = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    AnimatedVisibility(
        visible = isCollapsed.value.not(),
        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
        exit = shrinkVertically(tween(300)) + fadeOut(tween(200))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .background(color = White_40, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "\uD83C\uDFAF Виклик на сьогодні: Прокачай креативність",
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.subtitle2
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Порівняй непорівнюване, переверни звичайне, вигадуй нове. Покажи, на що здатен твій мозок!",
                color = MaterialTheme.colorScheme.typoSecondary(),
                style = LinguaTypography.body4
            )
            Spacer(Modifier.height(40.dp))
            PrimaryButton(text = "\uD83D\uDD25 ПРИЙНЯТИ") { }
        }
    }
}

@Composable
private fun Game(
    state: GamesViewState,
    game: GameUi,
    listState: LazyListState,
    actioner: (GamesAction) -> Unit
) {
    Column {
        GameDescription(state, game, actioner)

        BoxWithStripes(
            isEnabled = game.isEnable,
            rawShadowYOffset = 3.dp,
            contentPadding = 16.dp,
            background = MaterialTheme.colorScheme.surface,
            backgroundShadow = MaterialTheme.colorScheme.onBackgroundDark(),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClick = {
                if (!listState.isScrollInProgress) {
                    actioner(GamesAction.OnGameClicked(game))
                }
            },
            borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
            borderWidth = 1.dp,
            stripeWidth = 70.dp,
            stripeSpacing = 190.dp,
            stripeColor = Black_3
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = game.game.nameText,
                        color = MaterialTheme.colorScheme.typoPrimary(),
                        style = LinguaTypography.subtitle2
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Прокачує: " + game.skills.map { it.asString() }.joinToString(separator = ", "),
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        style = LinguaTypography.body4
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(60.dp),
                    painter = painterResource(game.iconResId),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.GameDescription(
    state: GamesViewState,
    game: GameUi,
    actioner: (GamesAction) -> Unit
) {
    AnimatedVisibility(
        visible = game.isDescriptionVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        when {
            game.isEnable.not() -> {
                StaticTooltip(
                    enableFloatAnimation = true,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
                    triangleAlignment = Alignment.Start,
                    contentPadding = 20.dp,
                    cornerRadius = 12.dp,
                    paddingHorizontal = 0.dp
                ) {
                    Text(
                        text = "Збери ще трохи досвіду або стань Premium і грай без обмежень!",
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        textAlign = TextAlign.Center,
                        style = LinguaTypography.body3
                    )
                    Spacer(Modifier.height(20.dp))

                    LinguaProgressBar(
                        modifier = Modifier.fillMaxWidth(),
                        progress = (state.experience.toFloat() / game.game.minExperienceRequired.toFloat()),
                        progressColor = Golden,
                        progressShadow = Color(0xFFE8BC02)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = state.experience.toString() + "/" + game.game.minExperienceRequired.toString() + " xp.",
                            color = White,
                            textAlign = TextAlign.Center,
                            style = LinguaTypography.body5
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    PremiumButton(text = "\uD83D\uDC51 ВІДКРИТИ З PREMIUM") {
                        actioner(GamesAction.OnStartGameClicked(game))
                    }
                }
            }
            state.availableTokens <= 0 -> {
                StaticTooltip(
                    enableFloatAnimation = true,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
                    triangleAlignment = Alignment.Start,
                    contentPadding = 20.dp,
                    cornerRadius = 12.dp,
                    paddingHorizontal = 0.dp
                ) {
                    Text(
                        text = "\uD83D\uDD12 Упс! Жетони на сьогодні закінчились",
                        color = MaterialTheme.colorScheme.typoPrimary(),
                        textAlign = TextAlign.Center,
                        style = LinguaTypography.subtitle2
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Нові з’являться після 00:00 ⏳\nАбо продовжуй гру вже зараз — без обмежень!",
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        textAlign = TextAlign.Center,
                        style = LinguaTypography.body4
                    )
                    Spacer(Modifier.height(20.dp))

                    PremiumButton(text = "\uD83D\uDC51 ГРАТИ БЕЗ ОБМЕЖЕНЬ") {
                        actioner(GamesAction.OnStartGameClicked(game))
                    }
                }
            }
            else -> {
                StaticTooltip(
                    enableFloatAnimation = true,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
                    triangleAlignment = Alignment.Start,
                    contentPadding = 20.dp,
                    cornerRadius = 12.dp,
                    paddingHorizontal = 0.dp
                ) {
                    Text(
                        text = game.game.nameText,
                        color = MaterialTheme.colorScheme.typoPrimary(),
                        style = LinguaTypography.subtitle2
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = game.game.descriptionText,
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        style = LinguaTypography.body4
                    )
                    Spacer(Modifier.height(20.dp))

                    PrimaryButton(text = "ПОЧАТИ (1 ТОКЕН)") {
                        actioner(GamesAction.OnPremiumBtnClicked)
                    }
                }
            }
        }
    }

    if (game.isDescriptionVisible) Spacer(Modifier.height(14.dp))
}

@Preview
@Composable
private fun HomePreview() {
    Column {
        LinguaTheme {
            GamesScreen(
                GamesViewState.Preview,
                {},
                actioner = { _ -> },
            )
        }
    }
}

