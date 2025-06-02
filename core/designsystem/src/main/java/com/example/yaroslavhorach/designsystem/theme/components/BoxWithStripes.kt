package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.yaroslavhorach.designsystem.theme.BlockOneDark
import com.example.yaroslavhorach.designsystem.theme.BlockOneLight
import com.example.yaroslavhorach.designsystem.theme.White_10

@Composable
fun BoxWithStripes(
    modifier: Modifier = Modifier,
    stripeColor: Color = White_10,
    stripeWidth: Dp = 60.dp,
    stripeSpacing: Dp = 150.dp,
    background: Color = BlockOneLight,
    backgroundShadow: Color = BlockOneDark,
    rawShadowYOffset: Dp = 3.dp,
    contentPadding: Dp = 16.dp,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    offsetX: Dp = 0.dp,
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
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touch = event.changes.firstOrNull() ?: continue

                        val pos = touch.position
                        val isInside = pos.x in 0f..boxSize.value.width.toFloat() &&
                                pos.y in 0f..boxSize.value.height.toFloat()

                        when {
                            touch.changedToDown() && isInside -> {
                                shadowYOffset.value = 0.dp
                            }

                            touch.changedToUp() -> {
                                shadowYOffset.value = rawShadowYOffset
                                if (isInside) onClick()
                            }

                            !isInside && touch.pressed -> {
                                shadowYOffset.value = rawShadowYOffset
                            }
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(color = backgroundShadow)
        )

        Box(
            modifier = modifier
                .offset(y = -shadowYOffset.value)
                .clip(shape)
                .border(borderWidth, borderColor, shape = shape)
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
    }
}