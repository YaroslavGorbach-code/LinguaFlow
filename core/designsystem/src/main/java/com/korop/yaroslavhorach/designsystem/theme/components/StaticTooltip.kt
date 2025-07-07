package com.korop.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun StaticTooltip(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    borderSize: Dp = 2.dp,
    cornerRadius: Dp = 8.dp,
    triangleHeight: Dp = 10.dp,
    triangleWidth: Dp = 20.dp,
    triangleAlignment: Alignment.Horizontal? = Alignment.CenterHorizontally,
    contentPadding: Dp = 12.dp,
    paddingHorizontal: Dp = 20.dp,
    enableFloatAnimation: Boolean = false,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    val triangleHeightPx = with(density) { triangleHeight.toPx() }
    val triangleWidthPx = with(density) { triangleWidth.toPx() }

    val offsetY by rememberInfiniteTransition(label = "FloatTooltip")
        .animateFloat(
            initialValue = 0f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "YOffset"
        )

    val animatedModifier = if (enableFloatAnimation)
        modifier.offset { IntOffset(0, offsetY.roundToInt()) }
    else modifier

    Box(
        modifier = animatedModifier
            .padding(horizontal = paddingHorizontal)
            .wrapContentSize()
            .drawBehind {
                val width = size.width
                val height = size.height
                val rectBottom = height - triangleHeightPx

                // Calculate triangle horizontal position based on alignment
                val triangleStartPx = when (triangleAlignment) {
                    Alignment.Start -> (16.dp).toPx()
                    Alignment.End -> (width - triangleWidthPx) - (16.dp).toPx()
                    else -> (width - triangleWidthPx) / 2f // center by default
                }

                val triangleEnd = triangleStartPx + triangleWidthPx

                val path = Path().apply {
                    moveTo(cornerRadiusPx, 0f)

                    // Top edge
                    lineTo(width - cornerRadiusPx, 0f)
                    quadraticTo(width, 0f, width, cornerRadiusPx)

                    // Right edge
                    lineTo(width, rectBottom - cornerRadiusPx)
                    quadraticTo(width, rectBottom, width - cornerRadiusPx, rectBottom)

                    // Bottom edge till triangle
                    lineTo(triangleEnd, rectBottom)
                    lineTo(triangleStartPx + triangleWidthPx / 2f, height)
                    lineTo(triangleStartPx, rectBottom)

                    // Left bottom
                    lineTo(cornerRadiusPx, rectBottom)
                    quadraticTo(0f, rectBottom, 0f, rectBottom - cornerRadiusPx)

                    // Left edge
                    lineTo(0f, cornerRadiusPx)
                    quadraticTo(0f, 0f, cornerRadiusPx, 0f)

                    close()
                }

                drawPath(path, backgroundColor)
                drawPath(path, borderColor, style = Stroke(width = borderSize.toPx()))
            }
            .graphicsLayer {
                this.alpha = alpha
            }
            .padding(bottom = triangleHeight)
    ) {
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}