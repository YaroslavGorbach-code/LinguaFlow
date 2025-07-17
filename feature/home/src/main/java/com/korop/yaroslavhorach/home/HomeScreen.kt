package com.korop.yaroslavhorach.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korop.yaroslavhorach.designsystem.extentions.blockColorPrimary
import com.korop.yaroslavhorach.designsystem.extentions.blockColorSecondary
import com.korop.yaroslavhorach.designsystem.extentions.blockDescription
import com.korop.yaroslavhorach.designsystem.extentions.blockTitle
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.White
import com.korop.yaroslavhorach.designsystem.theme.White_70
import com.korop.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.korop.yaroslavhorach.designsystem.theme.components.FloatingTooltip
import com.korop.yaroslavhorach.designsystem.theme.components.InactiveButton
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.korop.yaroslavhorach.designsystem.theme.components.SecondaryButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.secondaryIcon
import com.korop.yaroslavhorach.designsystem.theme.typoControlPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoDisabled
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.designsystem.theme.typoSecondary
import com.korop.yaroslavhorach.domain.exercise.model.Exercise
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.home.model.ExerciseUi
import com.korop.yaroslavhorach.home.model.HomeAction
import com.korop.yaroslavhorach.home.model.HomeViewState
import com.korop.yaroslavhorach.ui.utils.conditional
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.Locale

@Composable
internal fun HomeRoute(
    onNavigateToExercise: (Exercise) -> Unit,
    onNavigateToAvatarChange: () -> Unit,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        screenState = homeState,
        onMessageShown = viewModel::clearMessage,
        onChangeColorScheme = onChangeColorScheme,
        actioner = { action ->
            when (action) {
                is HomeAction.OnStartExerciseClicked -> {
                    viewModel.submitAction(HomeAction.OnHideDescription)
                    onNavigateToExercise(action.exercise)
                }

                is HomeAction.OnAvatarClicked -> {
                    onNavigateToAvatarChange()
                }

                else -> viewModel.submitAction(action)
            }
        })
}

@Composable
internal fun HomeScreen(
    screenState: HomeViewState,
    onMessageShown: (id: Long) -> Unit,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit,
    actioner: (HomeAction) -> Unit,
) {
    val density = LocalDensity.current
    val exercisesState = rememberLazyListState()
    val descriptionBlockBounds = remember { mutableStateOf<IntRect?>(null) }
    val animatedTopPadding by animateDpAsState(
        targetValue = screenState.descriptionState.listTopExtraPadding,
        animationSpec = tween(durationMillis = 300),
        label = "list_padding"
    )

    HandleOnScrollBlockChange(screenState, exercisesState, onChangeColorScheme, actioner)

    LaunchedEffect(exercisesState) {
        snapshotFlow { exercisesState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    actioner(HomeAction.OnHideDescription)
                }
            }
    }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touch = event.changes.first()

                        if (touch.changedToDown()) {
                            actioner(HomeAction.OnTouchOutside(touch.position))
                        }
                    }
                }
            }
    ) {
        val visible = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visible.value = true
        }

        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(animationSpec = tween(300))
        ) {
            Column {
                TopBar(screenState, modifier = Modifier.onGloballyPositioned {
                    descriptionBlockBounds.value = it.boundsInRoot().roundToIntRect()
                }, actioner)
                Spacer(Modifier.height(animatedTopPadding))
                Exercises(exercisesState, screenState, actioner, density)
            }
        }

        DescriptionTooltip(
            exercise = screenState.descriptionState.exercise,
            position = screenState.descriptionState.position,
            onGloballyPositioned = { position, _ ->
                actioner(HomeAction.OnDescriptionBoundsChanged(position))
            },
            onRequireRootTopPadding = { actioner(HomeAction.OnDescriptionListTopPaddingChanged(it)) },
            onStartExerciseClicked = { actioner(HomeAction.OnStartExerciseClicked(it.exercise)) }
        )
    }
}

@Composable
private fun Exercises(
    exercisesState: LazyListState,
    screenState: HomeViewState,
    actioner: (HomeAction) -> Unit,
    density: Density
) {
    val lastActiveIndex = remember(screenState.exercises) {
        screenState.exercises.indexOfFirst { it.isLastActive }
    }

    LaunchedEffect(lastActiveIndex) {
        if (lastActiveIndex > 1) {
            exercisesState.scrollToItem(lastActiveIndex)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = exercisesState
    ) {
        itemsIndexed(screenState.exercises) { index, exercise ->
            val block = exercise.exercise.block
            val previousBlock = screenState.exercises.getOrNull(index - 1)?.exercise?.block

            Spacer(Modifier.size(20.dp))

            if (block != previousBlock) {
                BlockTitle(block)
            }

            ExerciseProgressContainer(
                size = 80.dp,
                moveElementRight = (index % 2) != 0,
                progress = exercise.progressPercent,
                content = {
                    Exercise(
                        state = screenState,
                        exercise = exercise,
                        exerciseSize = 80.dp,
                        onClickWithCoordinates = { offset ->
                            actioner(
                                HomeAction.OnExerciseClicked(
                                    exercise,
                                    offset.copy(
                                        y = offset.y - with(density) {
                                            screenState.descriptionState.listTopExtraPadding.toPx()
                                        }
                                    )
                                )
                            )
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun BlockTitle(block: ExerciseBlock) {
    Row(
        modifier = Modifier
            .padding(bottom = 40.dp, top = 4.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            Modifier
                .height(2.dp)
                .weight(0.2f)
                .background(
                    color = MaterialTheme.colorScheme.onBackgroundDark(),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Text(
            text = block.blockTitle().asString(),
            style = LinguaTypography.h6,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.typoDisabled(),
            modifier = Modifier
                .weight(1f)
        )
        Spacer(
            Modifier
                .height(2.dp)
                .weight(0.2f)
                .background(
                    color = MaterialTheme.colorScheme.onBackgroundDark(),
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}

@Composable
private fun HandleOnScrollBlockChange(
    screenState: HomeViewState,
    exercisesState: LazyListState,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit,
    actioner: (HomeAction) -> Unit
) {
    val currentBlock = remember { mutableStateOf<ExerciseBlock?>(null) }

    LaunchedEffect(screenState.exercises) {
        if (screenState.exercises.isNotEmpty()) {
            snapshotFlow { exercisesState.layoutInfo.visibleItemsInfo }
                .map { visibleItems ->
                    val visibleIndexes = visibleItems.map { it.index }
                    val visibleExercises = visibleIndexes.mapNotNull { screenState.exercises.getOrNull(it) }

                    val blockCounts = visibleExercises
                        .groupingBy { it.exercise.block }
                        .eachCount()

                    val sorted = blockCounts.entries.sortedByDescending { it.value }

                    val first = sorted.getOrNull(0)
                    val second = sorted.getOrNull(1)

                    val significantAdvantage = 2

                    if (first != null && (second == null || (first.value - second.value) >= significantAdvantage)) {
                        first.key
                    } else {
                        null
                    }
                }
                .distinctUntilChanged()
                .collect { mostFrequentBlock ->
                    if (mostFrequentBlock != null && mostFrequentBlock != currentBlock.value) {
                        currentBlock.value = mostFrequentBlock
                        onChangeColorScheme(
                            mostFrequentBlock.blockColorPrimary(),
                            mostFrequentBlock.blockColorSecondary()
                        )
                        actioner(HomeAction.OnExercisesBlockChanged(mostFrequentBlock))
                    }
                }
        }
    }
}

@Composable
private fun TopBar(state: HomeViewState, modifier: Modifier, actioner: (HomeAction) -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxHeight = screenHeight / 1.8f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(LinguaIcons.Microphone2),
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
                        text = stringResource(R.string.home_title_text),
                        color = MaterialTheme.colorScheme.typoPrimary(),
                        style = LinguaTypography.h3
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.home_subtitle_text),
                        color = MaterialTheme.colorScheme.typoSecondary(),
                        style = LinguaTypography.body4
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                LinguaProgressBar(
                    progress = state.blockProgress,
                    progressBackgroundColor = MaterialTheme.colorScheme.onBackgroundDark(),
                    progressColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 14.dp)
                        .height(40.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = 15.dp)
                            .align(Alignment.CenterEnd),
                        painter = painterResource(LinguaIcons.Cup),
                        contentDescription = ""
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    modifier = Modifier,
                    text = state.exerciseBlock.blockTitle().asString(),
                    color = White,
                    style = LinguaTypography.h5
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    modifier = Modifier,
                    text = state.exerciseBlock.blockDescription().asString(),
                    color = White,
                    style = LinguaTypography.body4
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun Exercise(
    state: HomeViewState,
    exercise: ExerciseUi,
    exerciseSize: Dp,
    onClickWithCoordinates: (Offset) -> Unit,
) {
    val shadow = 0.dp
    val layoutCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "premium_border")
    val scale = infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    Box(
        modifier = Modifier
            .conditional(exercise.isEnable && exercise.isFinished.not()) {
                graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
            }
    ) {
        BoxWithStripes(
            stripeColor = Color.Transparent,
            contentPadding = 8.dp,
            background = if (exercise.isEnable) exercise.exercise.block.blockColorPrimary() else MaterialTheme.colorScheme.onBackground,
            backgroundShadow = if (exercise.isEnable) exercise.exercise.block.blockColorSecondary() else MaterialTheme.colorScheme.onBackgroundDark(),
            modifier = Modifier
                .width(exerciseSize)
                .height(exerciseSize - shadow)
                .onGloballyPositioned { coordinates ->
                    layoutCoordinates.value = coordinates
                }
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    layoutCoordinates.value?.let { coordinates ->
                        val center = coordinates.localToWindow(Offset.Zero)

                        onClickWithCoordinates(Offset(center.x + (exerciseSize.value), center.y))
                    }
                },
            shape = RoundedCornerShape(16.dp),
            stripeWidth = 20.dp,
            stripeSpacing = 90.dp,
            rawShadowYOffset = shadow
        ) {
            Box(modifier = Modifier.size(exerciseSize)) {
                val iconColor = when {
//                        exercise.isFinished -> Golden
                    exercise.isEnable.not() -> Color(0xFFAFAFAF)
                    exercise.isEnable -> White_70
                    else -> MaterialTheme.colorScheme.secondaryIcon()
                }
                Icon(
                    modifier = Modifier
                        .size(38.dp)
                        .rotate(30f)
                        .align(Alignment.Center)
                        .padding(top = 3.dp),
                    tint = iconColor,
                    painter = painterResource(exercise.iconResId),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun DescriptionTooltip(
    modifier: Modifier = Modifier,
    exercise: ExerciseUi?,
    position: Offset?,
    onGloballyPositioned: (Rect, IntOffset) -> Unit,
    onRequireRootTopPadding: (Dp) -> Unit,
    onStartExerciseClicked: (ExerciseUi) -> Unit
) {
    val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

    FloatingTooltip(
        modifier = modifier,
        backgroundColor = if (exercise?.isEnable == true) MaterialTheme.colorScheme.primary else if (exercise != null) MaterialTheme.colorScheme.onBackground else Color.Transparent,
        borderColor = if (exercise?.isEnable == true) MaterialTheme.colorScheme.secondary else if (exercise != null) MaterialTheme.colorScheme.onBackgroundDark() else Color.Transparent,
        contentPadding = 20.dp,
        appearPosition = position,
        borderSize = 0.dp,
        onGloballyPositioned = onGloballyPositioned,
        onRequireRootTopPadding = onRequireRootTopPadding
    ) {
        if (exercise?.isEnable == true) {
            Column {
                Row {
                    Icon(
                        modifier = modifier.size(35.dp),
                        painter = painterResource(exercise.iconResId),
                        contentDescription = null,
                        tint = White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(exercise.skillNameResId),
                        style = LinguaTypography.body4,
                        color = MaterialTheme.colorScheme.typoControlPrimary()
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = exercise.progress,
                        style = LinguaTypography.subtitle2,
                        color = MaterialTheme.colorScheme.typoControlPrimary()
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = exercise.exercise.getNameText(lang),
                    style = LinguaTypography.h4,
                    color = MaterialTheme.colorScheme.typoControlPrimary()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = exercise.exercise.getDescriptionText(lang),
                    style = LinguaTypography.body4,
                    color = MaterialTheme.colorScheme.typoControlPrimary()
                )
                Spacer(Modifier.height(16.dp))
                val startBtnText =
                    if (exercise.isFinished) stringResource(R.string.home_repeat_exercise_btn_text) else stringResource(
                        R.string.home_start_exercise_btn_text
                    )
                SecondaryButton(text = startBtnText, onClick = {
                    onStartExerciseClicked(exercise)
                })
            }
        } else if (exercise != null) {
            Column {
                Row {
                    Icon(
                        modifier = modifier.size(35.dp),
                        painter = painterResource(exercise.iconResId),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.typoDisabled()
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(exercise.skillNameResId),
                        style = LinguaTypography.body4,
                        color = MaterialTheme.colorScheme.typoDisabled()
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = exercise.exercise.getNameText(lang),
                    style = LinguaTypography.subtitle2,
                    color = MaterialTheme.colorScheme.typoDisabled()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.not_active_exercise_description_text),
                    style = LinguaTypography.body4,
                    color = MaterialTheme.colorScheme.typoDisabled()
                )
                Spacer(Modifier.height(16.dp))
                InactiveButton(text = stringResource(R.string.not_active_exercise_btn_text))
            }
        }
    }
}

@Composable
private fun UserGreeting(screenState: HomeViewState, actioner: (HomeAction) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = stringResource(id = R.string.home_user_grating_text, screenState.userName),
            style = LinguaTypography.h2,
            color = MaterialTheme.colorScheme.typoControlPrimary()
        )
        Spacer(Modifier.width(20.dp))
        screenState.userAvatar?.let {
            Image(
                modifier = Modifier
                    .clickable { actioner(HomeAction.OnAvatarClicked) }
                    .size(45.dp),
                painter = painterResource(it),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
private fun HomePreview() {

    LinguaTheme {
        Surface {
            HomeScreen(
                HomeViewState.Preview,
                {},
                actioner = { _ -> },
                onChangeColorScheme = { _, _ -> })
        }
    }
}

