package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun FloatingTooltip(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    borderSize: Dp = 2.dp,
    cornerRadius: Dp = 8.dp,
    triangleHeight: Dp = 10.dp,
    triangleWidth: Dp = 20.dp,
    contentPadding: Dp = 12.dp,
    bottomPadding: Dp = 12.dp,
    paddingHorizontal: Dp = 20.dp,
    enableFloatAnimation: Boolean = false,
    appearPosition: Offset?,
    onGloballyPositioned: (Rect, IntOffset) -> Unit = { _, _ -> },
    onRequireRootTopPadding: (Dp) -> Unit = {},
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    val triangleHeightPx = with(density) { triangleHeight.toPx() }
    val triangleWidthPx = with(density) { triangleWidth.toPx() }
    val descriptionTooltipSize = remember { mutableStateOf(IntSize.Zero) }
    val triangleStart = remember { mutableFloatStateOf(0f) }
    val fillMaxsize = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val safeTopInset = WindowInsets.statusBars.getTop(density).toFloat()
    val visible = appearPosition != null

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "tooltip_alpha"
    )

    val targetX = remember(visible, appearPosition, descriptionTooltipSize.value) {
        with(density) {
            val screenWidth = configuration.screenWidthDp.dp.toPx()
            val tooltipWidth = descriptionTooltipSize.value.width.toFloat()

            fillMaxsize.value = tooltipWidth > screenWidth * 0.9

            if (fillMaxsize.value) {
                0f
            } else ((appearPosition?.x ?: 0f) - (tooltipWidth + paddingHorizontal.toPx() * 2) / 2f)
        }
    }

    val targetY = remember(visible, appearPosition, descriptionTooltipSize.value) {
        with(density) {
            var y = (appearPosition?.y ?: 0f) - descriptionTooltipSize.value.height - bottomPadding.toPx()

            if (y < safeTopInset) {
                if (visible) onRequireRootTopPadding((safeTopInset + abs(y)).toDp())

                y = (safeTopInset + y).coerceAtLeast(safeTopInset)
            } else {
                onRequireRootTopPadding(0.dp)
            }

            triangleStart.floatValue = appearPosition?.x ?: 0f
            y
        }
    }

    val animatedX by animateFloatAsState(
        targetValue = targetX,
        animationSpec = tween(durationMillis = 100),
        label = "tooltip_x"
    )

    val animatedY by animateFloatAsState(
        targetValue = targetY,
        animationSpec = tween(durationMillis = 100),
        label = "tooltip_y"
    )

    val offsetY by rememberInfiniteTransition(label = "FloatTooltip")
        .animateFloat(
            initialValue = 0f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "YOffset"
        )


    val animatedModifier = if (enableFloatAnimation)
        modifier.offset { IntOffset(0, offsetY.roundToInt()) }
    else modifier

    Box(
        modifier = animatedModifier
            .padding(horizontal = 20.dp)
            .wrapContentSize()
            .zIndex(1f)
            .onGloballyPositioned {
                descriptionTooltipSize.value = it.size
            }
            .absoluteOffset {
                val offset = IntOffset(animatedX.roundToInt(), animatedY.roundToInt())
                onGloballyPositioned(Rect(offset.toOffset(), descriptionTooltipSize.value.toSize()), offset)
                offset
            }
            .graphicsLayer {
                this.alpha = alpha
            }
            .drawBehind {
                val width = size.width
                val height = size.height
                val rectBottom = height - triangleHeightPx

                // Calculate start point of triangle
                val triangleStartPx =
                    if (fillMaxsize.value) triangleStart.floatValue else (width - triangleWidthPx) / 2f

                val triangleEnd = triangleStartPx + triangleWidthPx


                val path = Path().apply {
                    moveTo(cornerRadiusPx, 0f)

                    // Top
                    lineTo(width - cornerRadiusPx, 0f)
                    quadraticTo(width, 0f, width, cornerRadiusPx)

                    // Right
                    lineTo(width, rectBottom - cornerRadiusPx)
                    quadraticTo(width, rectBottom, width - cornerRadiusPx, rectBottom)

                    // Bottom till triangle
                    lineTo(triangleEnd, rectBottom)
                    lineTo(triangleStartPx + triangleWidthPx / 2f, height)
                    lineTo(triangleStartPx, rectBottom)

                    // left bottom
                    lineTo(cornerRadiusPx, rectBottom)
                    quadraticTo(0f, rectBottom, 0f, rectBottom - cornerRadiusPx)

                    // left
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
