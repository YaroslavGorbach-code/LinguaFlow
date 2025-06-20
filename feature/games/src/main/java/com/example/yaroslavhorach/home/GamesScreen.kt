package com.example.yaroslavhorach.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.designsystem.theme.Black_3
import com.example.yaroslavhorach.designsystem.theme.Golden
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
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
import com.example.yaroslavhorach.designsystem.theme.primaryIcon
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.designsystem.theme.typoSecondary
import com.example.yaroslavhorach.domain.game.model.Challenge
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.home.model.GameUi
import com.example.yaroslavhorach.home.model.GamesAction
import com.example.yaroslavhorach.home.model.GamesViewState
import com.example.yaroslavhorach.home.model.getText

@Composable
internal fun GamesRoute(
    onNavigateToGame: (gameId: Long, gameName: Game.GameName) -> Unit,
    viewModel: GamesViewModel = hiltViewModel(),
) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()

    GamesScreen(
        state = homeState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {
                is GamesAction.OnStartGameClicked -> {
                    onNavigateToGame(action.gameUi.game.id, action.gameUi.game.name)
                }
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
        Spacer(Modifier.height(20.dp))
        LazyRow {
            itemsIndexed(state.sorts) { index, item ->
                if (index == 0) { Spacer(Modifier.width(20.dp)) }
                Text(
                    modifier = Modifier
                        .clickable { actioner(GamesAction.OnSortSelected(item)) }
                        .border(
                            1.dp,
                            if (item == state.selectedSort) KellyGreen else MaterialTheme.colorScheme.onBackgroundDark(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp),
                    text = item.getText().asString(),
                    style = LinguaTypography.body4,
                    color = MaterialTheme.colorScheme.typoPrimary()
                )
                Spacer(Modifier.width(20.dp))
            }
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn(modifier = Modifier.padding(horizontal = 20.dp), state = listState) {
            itemsIndexed(state.games, key = { _, game -> game.game.id }) { index, item ->
                if (index == 0) Spacer(Modifier.height(8.dp))
                Game(state, item, listState, actioner)
                Spacer(Modifier.height(20.dp))
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

        Image(
            painter = painterResource(LinguaIcons.Brain),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .rotate(-18f)
                .offset { IntOffset(x = 100, y = 100) }
                .size(200.dp)
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
            Challenge(screenState, listState, actioner)
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
private fun Challenge(state: GamesViewState, listState: LazyListState, actioner: (GamesAction) -> Unit) {
    val isCollapsed = remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }.collect { atTop ->
            isCollapsed.value = !atTop
        }
    }

    AnimatedVisibility(
        visible = !isCollapsed.value,
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
            when {
                state.challenge?.status?.started?.not() == true -> {
                    ChallengeNotStarted(state.challenge, actioner)
                }
                state.challenge?.status?.started == true && state.challenge.status.completed.not() -> {
                    ChallengeStarted(state.challenge, actioner)
                }
                state.challenge?.status?.completed == true -> {
                    ChallengeCompleted(state.challenge)
                }
            }
        }
    }
}

@Composable
private fun ChallengeCompleted(challenge: Challenge) {
    Text(
        text = "\uD83C\uDF89 Виклик виконано!",
        color = MaterialTheme.colorScheme.typoPrimary(),
        style = LinguaTypography.subtitle2
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = challenge.completeMessage,
        color = MaterialTheme.colorScheme.typoSecondary(),
        style = LinguaTypography.body4
    )
    Spacer(Modifier.height(20.dp))
    StaticTooltip(
        modifier = Modifier.fillMaxWidth(),
        enableFloatAnimation = false,
        backgroundColor = Color.Transparent,
        borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
        borderSize = 1.dp,
        paddingHorizontal = 0.dp,
        triangleAlignment = Alignment.Start,
        contentPadding = 16.dp,
        cornerRadius = 12.dp,
    ) {
        Text(
            text = "Виклик дня — виконано! \uD83C\uDFAF\n +${challenge.bonusOnComplete} досвіду, рівень підвищено! \uD83D\uDD25",
            color = MaterialTheme.colorScheme.typoSecondary(),
            textAlign = TextAlign.Center,
            style = LinguaTypography.body4
        )
    }

    // TODO: implement in the future
    LinguaProgressBar(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        progress = 0.5f,
        progressBarHeight = 22.dp
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .offset(x = 22.dp)
                .padding(bottom = 12.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(LinguaIcons.Confetti),
            contentDescription = ""
        )
    }
}

@Composable
private fun ChallengeStarted(
    challenge: Challenge,
    actioner: (GamesAction) -> Unit
) {
    LinguaProgressBar(
        challenge.progressInMinutes.toFloat() / challenge.durationMinutes.toFloat(),
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = challenge.progressInMinutes.toString() + " хв.",
            color = White,
            textAlign = TextAlign.Center,
            style = LinguaTypography.body5
        )

        Image(
            modifier = Modifier
                .size(32.dp)
                .offset(x = 10.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(LinguaIcons.LightingThunder),
            contentDescription = ""
        )
    }
    Spacer(Modifier.height(12.dp))
    Text(
        text = challenge.acceptMessage,
        color = MaterialTheme.colorScheme.typoSecondary(),
        style = LinguaTypography.body4
    )
    Spacer(Modifier.height(40.dp))
    PrimaryButton(text = "ПЕРЕЙТИ ДО ВПРАВ") { actioner(GamesAction.OnGoToDailyChallengeExercises) }
}

@Composable
private fun ChallengeNotStarted(
    challenge: Challenge,
    actioner: (GamesAction) -> Unit
) {
    Text(
        text = challenge.title,
        color = MaterialTheme.colorScheme.typoPrimary(),
        style = LinguaTypography.subtitle2
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = challenge.description,
        color = MaterialTheme.colorScheme.typoSecondary(),
        style = LinguaTypography.body4
    )
    Spacer(Modifier.height(40.dp))
    PrimaryButton(text = "\uD83D\uDD25 ПРИЙНЯТИ (1 ТОКЕН)") { actioner(GamesAction.OnStartDailyChallengeClicked) }
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
                GameDescriptionNotEnable(state, game, actioner)
            }
            state.availableTokens <= 0 -> {
                DameDescriptionNoTokens(actioner)
            }
            else -> {
                GameDescriptionEnable(game, state, actioner)
            }
        }
    }

    if (game.isDescriptionVisible) Spacer(Modifier.height(14.dp))
}

@Composable
private fun GameDescriptionEnable(
    game: GameUi,
    state: GamesViewState,
    actioner: (GamesAction) -> Unit
) {
    StaticTooltip(
        enableFloatAnimation = true,
        backgroundColor = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
        triangleAlignment = Alignment.Start,
        contentPadding = 20.dp,
        cornerRadius = 12.dp,
        paddingHorizontal = 0.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = game.game.nameText,
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.subtitle2
            )
            if (state.favorites.contains(game.game.id)) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { actioner(GamesAction.OnRemoveFavoritesClicked(game)) },
                    painter = painterResource(LinguaIcons.icStarFilled),
                    contentDescription = null,
                    tint = Golden
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { actioner(GamesAction.OnAddToFavoritesClicked(game)) },
                    painter = painterResource(LinguaIcons.icStar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryIcon()
                )
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(
            text = game.game.descriptionText,
            color = MaterialTheme.colorScheme.typoSecondary(),
            style = LinguaTypography.body4
        )
        Spacer(Modifier.height(20.dp))

        val useToken = game.game.skills.contains(state.challenge?.theme)
                && state.challenge?.status?.completed?.not() == true

        val btnText = if (useToken) {
            "ПОЧАТИ"
        } else {
            "ПОЧАТИ (1 ТОКЕН)"
        }
        PrimaryButton(text = btnText) {
            actioner(GamesAction.OnStartGameClicked(game, useToken))
        }
    }
}

@Composable
private fun DameDescriptionNoTokens(actioner: (GamesAction) -> Unit) {
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
            actioner(GamesAction.OnPremiumBtnClicked)
        }
    }
}

@Composable
private fun GameDescriptionNotEnable(
    state: GamesViewState,
    game: GameUi,
    actioner: (GamesAction) -> Unit
) {
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
            actioner(GamesAction.OnPremiumBtnClicked)
        }
    }
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

