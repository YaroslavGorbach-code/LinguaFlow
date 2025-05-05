package com.example.yaroslavhorach.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.designsystem.theme.Black_35
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.OrangeDark
import com.example.yaroslavhorach.designsystem.theme.White
import com.example.yaroslavhorach.designsystem.theme.components.BoxWithLines
import com.example.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.SecondaryButton
import com.example.yaroslavhorach.designsystem.theme.components.Tooltip
import com.example.yaroslavhorach.designsystem.theme.controlPrimaryTypo
import com.example.yaroslavhorach.designsystem.theme.controlSecondaryTypo
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons.Cup
import com.example.yaroslavhorach.designsystem.theme.graphics.LinguaIcons.Microphone
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.home.model.ExerciseUi
import com.example.yaroslavhorach.home.model.HomeAction
import com.example.yaroslavhorach.home.model.HomeViewState

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(screenState = homeState, onMessageShown = viewModel::clearMessage, actioner = { action ->
        when (action) {
            else -> viewModel.submitAction(action)
        }
    })
}

@Composable
internal fun HomeScreen(
    screenState: HomeViewState,
    onMessageShown: (id: Long) -> Unit,
    actioner: (HomeAction) -> Unit,
) {
    val selectedExerciseForDescription = remember { mutableStateOf<ExerciseUi?>(null) }
    val descriptionTooltipPosition = remember { mutableStateOf<Offset?>(null) }
    val descriptionTooltipBounds = remember { mutableStateOf<Rect?>(null) }
    val startExerciseTooltipPosition = remember { mutableStateOf<Offset?>(null) }
    val listTopPadding = remember { mutableFloatStateOf(0f) }
    val exercisesState = rememberLazyListState()

    val animatedTopPadding by animateDpAsState(
        targetValue = listTopPadding.floatValue.dp,
        animationSpec = tween(durationMillis = 300),
        label = "list_padding"
    )

    LaunchedEffect(exercisesState) {
        snapshotFlow { exercisesState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling && selectedExerciseForDescription.value != null) {
                    selectedExerciseForDescription.value = null
                    descriptionTooltipPosition.value = null
                    listTopPadding.floatValue = 0f
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
                            val position = touch.position

                            descriptionTooltipBounds.value?.let { bounds ->
                                if (bounds
                                        .contains(position)
                                        .not()
                                ) {
                                    selectedExerciseForDescription.value = null
                                    descriptionTooltipPosition.value = null
                                    listTopPadding.floatValue = 0f
                                }
                            }
                        }
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
        ) {
            UserGreeting(screenState)
            BlockDescription()
            Spacer(
                Modifier.height(animatedTopPadding)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = exercisesState
            ) {
                itemsIndexed(screenState.exercises) { index, exercise ->
                    if (index > 0) Spacer(Modifier.size(20.dp))
                    if (index == 0) Spacer(Modifier.size(40.dp))

                    Exercise(exercise, (index % 2) != 0,
                        onClickWithCoordinates = { offset ->
                            if (selectedExerciseForDescription.value != null) {
                                selectedExerciseForDescription.value = null
                                descriptionTooltipPosition.value = null
                                listTopPadding.floatValue = 0f
                            } else {
                                selectedExerciseForDescription.value = exercise
                                descriptionTooltipPosition.value = offset.copy(x = offset.x)
                            }
                        }, onGloballyPositioned = {
                            if (exercise.isLastActive && selectedExerciseForDescription.value != exercise) {
                                startExerciseTooltipPosition.value = it
                            }
                        }
                    )
                }
            }
        }


        startExerciseTooltipPosition.value?.let { position ->
            StartTooltip(position)
        }
        DescriptionTooltip(
            modifier = Modifier.align(Alignment.TopStart),
            exercise = selectedExerciseForDescription.value,
            position = descriptionTooltipPosition.value,
            onGloballyPositioned = { position, _ ->
                descriptionTooltipBounds.value = position
            },
            onRequireRootTopPadding = {
                listTopPadding.floatValue = it
            },
        )
    }
}

@Composable
private fun StartTooltip(position: Offset) {
    Tooltip(
        bottomPadding = 0.dp,
        enableFloatAnimation = true,
        appearPosition = position
    ) {
        Text(
            "ПОЧАТИ",
            style = LinguaTypography.subtitle2,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BlockDescription(modifier: Modifier = Modifier) {
    BoxWithLines(
        modifier = modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(12.dp))
            Text(
                modifier = Modifier,
                text = "Блок 1: Small Talk & Знайомство",
                color = MaterialTheme.colorScheme.controlPrimaryTypo(),
                style = LinguaTypography.h4
            )
            Spacer(Modifier.height(4.dp))
            Text(
                modifier = Modifier,
                text = "Навчись легко починати розмову, підтримувати бесіду і знайомитись з новими людьми",
                color = MaterialTheme.colorScheme.controlSecondaryTypo(),
                style = LinguaTypography.subtitle3
            )
            Spacer(Modifier.height(14.dp))
            LinguaProgressBar(
                0.2f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 10.dp, y = (-3).dp),
                    painter = painterResource(Cup),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun Exercise(
    exercise: ExerciseUi,
    moveElementRight: Boolean,
    onClickWithCoordinates: (Offset) -> Unit,
    onGloballyPositioned: (Offset) -> Unit,
) {
    val exerciseSize = 90.dp
    val layoutCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }

    val offset = if (moveElementRight) exerciseSize / 1.5f else -exerciseSize / 1.5f
    Row {
        Box {
            BoxWithLines(
                modifier = Modifier
                    .offset(x = offset)
                    .size(exerciseSize)
                    .border(2.dp, OrangeDark, shape = RoundedCornerShape(12.dp))
                    .onGloballyPositioned { coordinates ->
                        val center = coordinates.boundsInRoot().topCenter
                        onGloballyPositioned(Offset(center.x, center.y))

                        layoutCoordinates.value = coordinates
                    }
                    .clickable {
                        layoutCoordinates.value?.let { coordinates ->
                            val center = coordinates.boundsInRoot().topLeft

                            onClickWithCoordinates(Offset(center.x + (exerciseSize.value / 2), center.y))
                        }
                    },
                stripeWidth = 20.dp,
                stripeSpacing = 45.dp,
                shadowOffset = 0.dp
            ) {
                if (exercise.isStarted) {
                    LinguaProgressBar(
                        0.2f,
                        modifier = Modifier
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth()
                            .height(6.dp)
                    )
                }

                Icon(
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.Center)
                        .padding(top = 3.dp),
                    tint = White,
                    painter = painterResource(Microphone),
                    contentDescription = ""
                )
            }

            if (exercise.isEnable.not()) {
                Spacer(
                    modifier = Modifier
                        .offset(x = offset)
                        .size(exerciseSize)
                        .background(color = Black_35, shape = RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

@Composable
private fun DescriptionTooltip(
    modifier: Modifier,
    exercise: ExerciseUi?,
    position: Offset?,
    onGloballyPositioned: (Rect, IntOffset) -> Unit,
    onRequireRootTopPadding: (Float) -> Unit
) {
    Tooltip(
        modifier = modifier,
        backgroundColor = exercise?.colorLight ?: Color.Transparent,
        borderColor = exercise?.colorDark ?: Color.Transparent,
        contentPadding = 20.dp,
        appearPosition = position,
        onGloballyPositioned = onGloballyPositioned,
        onRequireRootTopPadding = onRequireRootTopPadding
    ) {
        if (exercise != null) {
            Column() {
                Row {
                    Icon(painter = painterResource(exercise.iconResId), null, tint = White)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(exercise.skillNameResId),
                        style = LinguaTypography.body4,
                        color = MaterialTheme.colorScheme.controlPrimaryTypo()
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = exercise.progress,
                        style = LinguaTypography.subtitle2,
                        color = MaterialTheme.colorScheme.controlPrimaryTypo()
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(exercise.nameResId),
                    style = LinguaTypography.h4,
                    color = MaterialTheme.colorScheme.controlPrimaryTypo()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(exercise.descriptionResId),
                    style = LinguaTypography.body4,
                    color = MaterialTheme.colorScheme.controlPrimaryTypo()
                )
                Spacer(Modifier.height(16.dp))
                SecondaryButton(text = "ПОЧАТИ", onClick = {})
            }
        }

    }
}

@Composable
private fun UserGreeting(screenState: HomeViewState) {
    Text(
        text = stringResource(id = R.string.home_user_grating_text, screenState.userName),
        style = LinguaTypography.h2,
        color = MaterialTheme.colorScheme.typoPrimary()
    )
}

@Preview
@Composable
private fun HomePreview() {
    Column {
        LinguaTheme { HomeScreen(HomeViewState.Preview, {}, {}) }

    }
}

