package com.korop.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.korop.yaroslavhorach.designsystem.theme.ChineseGold
import com.korop.yaroslavhorach.designsystem.theme.Golden
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.White
import com.korop.yaroslavhorach.designsystem.theme.White_20
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.typoDisabled
import kotlin.math.roundToInt

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    val rawShadowYOffset = 10.dp
    val shadowYOffset = remember { mutableStateOf(rawShadowYOffset) }
    val boxSize = remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onSizeChanged { boxSize.value = it }
            .height(45.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touch = event.changes.first()

                        val position = touch.position
                        val isInside = position.x in 0f..boxSize.value.width.toFloat() &&
                                position.y in 0f..boxSize.value.height.toFloat()

                        when {
                            touch.changedToDown() && isInside -> {
                                shadowYOffset.value = 0.dp
                            }

                            touch.changedToUp() -> {
                                shadowYOffset.value = rawShadowYOffset
                                if (isInside) {
                                    onClick()
                                }
                            }

                            !isInside && touch.pressed -> {
                                shadowYOffset.value = rawShadowYOffset
                            }
                        }
                    }
                }
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.onBackgroundDark())
        )

        Spacer(
            modifier = Modifier
                .offset { IntOffset(x = 0, -shadowYOffset.value.value.roundToInt()) }
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.surface)
        )

        Text(
            modifier = Modifier
                .offset { IntOffset(x = 0, -shadowYOffset.value.value.roundToInt()) }
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = LinguaTypography.subtitle3,
            color = textColor
        )
    }
}

@Composable
fun PrimaryButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    val rawShadowYOffset = 10.dp
    val shadowYOffset = remember { mutableStateOf(rawShadowYOffset) }
    val boxSize = remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onSizeChanged { boxSize.value = it }
            .height(45.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touch = event.changes.first()

                        val position = touch.position
                        val isInside = position.x in 0f..boxSize.value.width.toFloat() &&
                                position.y in 0f..boxSize.value.height.toFloat()

                        when {
                            touch.changedToDown() && isInside -> {
                                shadowYOffset.value = 0.dp
                            }

                            touch.changedToUp() -> {
                                shadowYOffset.value = rawShadowYOffset
                                if (isInside) {
                                    onClick()
                                }
                            }

                            !isInside && touch.pressed -> {
                                shadowYOffset.value = rawShadowYOffset
                            }
                        }
                    }
                }
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.secondary)
        )

        Spacer(
            modifier = Modifier
                .offset { IntOffset(x = 0, -shadowYOffset.value.value.roundToInt()) }
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.primary)
        )

        Text(
            modifier = Modifier
                .offset { IntOffset(x = 0, -shadowYOffset.value.value.roundToInt()) }
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = LinguaTypography.subtitle3,
            color = White
        )
    }
}

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pressOffset: Dp = 2.dp,
    textColor: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle =  LinguaTypography.subtitle3,
    alignment: Alignment = Alignment.Center
) {
    val isPressed = remember { mutableStateOf(false) }
    val boxSize = remember { mutableStateOf(IntSize.Zero) }

    val offsetY = animateDpAsState(
        targetValue = if (isPressed.value) pressOffset else 0.dp,
        animationSpec = tween(durationMillis = 100),
        label = "PressOffsetAnim"
    )

    Box(
        modifier = modifier
            .onSizeChanged { boxSize.value = it }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touch = event.changes.first()

                        val position = touch.position
                        val isInside = position.x in 0f..boxSize.value.width.toFloat() &&
                                position.y in 0f..boxSize.value.height.toFloat()

                        when {
                            touch.changedToDown() && isInside -> {
                                isPressed.value = true
                            }

                            touch.changedToUp() -> {
                                if (isInside) {
                                    onClick()
                                }
                                isPressed.value = false
                            }

                            !isInside && touch.pressed -> {
                                isPressed.value = false
                            }
                        }
                    }
                }
            }
            .offset(y = offsetY.value)
            .fillMaxWidth()
            .height(45.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            style = style,
            modifier = Modifier.align(alignment)
        )
    }
}

@Composable
fun PremiumButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    BoxWithStripes(
        contentPadding = 1.dp,
        stripeColor = White_20,
        rawShadowYOffset = 4.dp,
        modifier = modifier
            .height(45.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        backgroundShadow = ChineseGold,
        background = Golden,
        content = {
            Text(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                text = text,
                textAlign = TextAlign.Center,
                style = LinguaTypography.subtitle3,
                color = White
            )
        }
    )
}


@Composable
fun InactiveButton(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .height(45.dp)
            .background(color = MaterialTheme.colorScheme.onBackgroundDark(), shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = LinguaTypography.subtitle3,
            color = MaterialTheme.colorScheme.typoDisabled()
        )
    }
}
