package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.yaroslavhorach.designsystem.theme.Gainsboro
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
import com.example.yaroslavhorach.designsystem.theme.Menthol

@Composable
fun LinguaProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Gainsboro,
    progressColor: Color = KellyGreen,
    progressShadow: Color = Menthol,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progress-animation"
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val barHeight = size.height

            drawRoundRect(
                color = backgroundColor,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = size
            )

            val progressWidth = size.width * animatedProgress.coerceAtLeast(0.1f)

            drawRoundRect(
                color = progressColor,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = Size(width = progressWidth, height = size.height)
            )

            drawRoundRect(
                color = progressShadow,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = Size(width = progressWidth - barHeight, height = size.height * 0.3f),
                topLeft = Offset(barHeight * 0.5f, barHeight * 0.2f)
            )
        }

        content()
    }
}