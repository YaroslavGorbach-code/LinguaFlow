package com.korop.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.korop.yaroslavhorach.designsystem.theme.BrightGray
import com.korop.yaroslavhorach.designsystem.theme.KellyGreen
import com.korop.yaroslavhorach.designsystem.theme.White_20

@Composable
fun LinguaProgressBar(
    progress: Float,
    progressBarHeight: Dp = 20.dp,
    modifier: Modifier = Modifier,
    progressBackgroundColor: Color = BrightGray,
    progressColor: Color = KellyGreen,
    progressShadow: Color = White_20,
    minValue: Float = 0f,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val animatedProgress = remember { Animatable(progress.coerceIn(0f, 1f)) }

    LaunchedEffect(progress) {
        val target = progress.coerceIn(0f, 1f)
        val current = animatedProgress.value

        if (current == 1f && target < 0.1f) {
            animatedProgress.snapTo(0f)
        } else {
            animatedProgress.animateTo(
                target,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(progressBarHeight)
            .align(Alignment.CenterStart)
        ) {
            val barHeight = size.height

            drawRoundRect(
                color = progressBackgroundColor,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = size
            )

            val progressWidth = size.width * animatedProgress.value.coerceAtLeast(minValue)

            drawRoundRect(
                color = progressColor,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = Size(width = progressWidth, height = size.height)
            )

            val shadowPaddingX = barHeight * 0.5f

            drawRoundRect(
                color = progressShadow,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = Size(
                    width = (progressWidth - shadowPaddingX * 2).coerceAtLeast(0f),
                    height = size.height * 0.3f
                ),
                topLeft = Offset(
                    x = shadowPaddingX,
                    y = size.height * 0.2f
                )
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .zIndex(1f)
        ) {
            content()
        }
    }
}