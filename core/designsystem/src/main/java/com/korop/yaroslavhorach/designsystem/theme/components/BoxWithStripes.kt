package com.korop.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.korop.yaroslavhorach.designsystem.theme.Black_30
import com.korop.yaroslavhorach.designsystem.theme.Black_35
import com.korop.yaroslavhorach.designsystem.theme.White_10
import com.korop.yaroslavhorach.ui.utils.conditional

@Composable
fun BoxWithStripes(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    stripeColor: Color = White_10,
    stripeWidth: Dp = 60.dp,
    stripeSpacing: Dp = 150.dp,
    background: Color = MaterialTheme.colorScheme.primary,
    backgroundShadow: Color =  MaterialTheme.colorScheme.secondary,
    rawShadowYOffset: Dp = 3.dp,
    contentPadding: Dp = 16.dp,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    offsetX: Dp = 0.dp,
    rotation: Float = 0f,
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val shadowYOffset = remember { mutableStateOf(rawShadowYOffset) }
    val boxSize = remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .offset(x = offsetX)
            .onSizeChanged { boxSize.value = it }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val startPos = down.position

                    val isDownInside = startPos.x in 0f..boxSize.value.width.toFloat() &&
                            startPos.y in 0f..boxSize.value.height.toFloat()

                    if (isDownInside) {
                        shadowYOffset.value = 0.dp
                    }

                    val up = waitForUpOrCancellation()

                    if (up == null) {
                        shadowYOffset.value = rawShadowYOffset
                        return@awaitEachGesture
                    }

                    shadowYOffset.value = rawShadowYOffset

                    val endPos = up.position
                    val isUpInside = endPos.x in 0f..boxSize.value.width.toFloat() &&
                            endPos.y in 0f..boxSize.value.height.toFloat()
                    val moved = (endPos - startPos).getDistance() > 10f

                    if (!moved && isUpInside) {
                        onClick()
                    }
                }
            }
    ) {
        if (isEnabled) {
            Spacer(
                modifier = Modifier
                    .rotate(rotation)
                    .matchParentSize()
                    .clip(shape)
                    .background(color = backgroundShadow)
            )
        } else {
            Spacer(
                modifier = Modifier
                    .rotate(rotation)
                    .matchParentSize()
                    .clip(shape)
                    .background(color = Black_35, shape = shape)
            )
        }

        Box(
            modifier = modifier
                .rotate(rotation)
                .offset(y = -shadowYOffset.value)
                .clip(shape)
                .conditional(isEnabled) {
                    border(borderWidth, borderColor, shape = shape)
                }
                .background(color = background)
                .drawBehind {
                    val stripePxWidth = stripeWidth.toPx()
                    val stripePxSpacing = stripeSpacing.toPx()
                    val stripeLength = size.height * 3
                    var startX = -stripeLength / 2

                    while (startX < size.width + stripeLength) {
                        rotate(30f, pivot = Offset(startX, 0f)) {
                            drawRect(
                                color = stripeColor,
                                topLeft = Offset(startX, -stripeLength / 2),
                                size = Size(stripePxWidth, stripeLength)
                            )
                        }
                        startX += stripePxSpacing
                    }
                }
        ) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }

        if (isEnabled.not()) {
            Spacer(
                modifier = Modifier
                    .offset(y = -shadowYOffset.value)
                    .matchParentSize()
                    .background(color = Black_30, shape = shape)
            )
        }

    }
}