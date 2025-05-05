package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    Box {
        Canvas(modifier = modifier) {
            val barHeight = size.height

            drawRoundRect(
                color = backgroundColor,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = size
            )

            val progressSize = size.width * progress
            drawRoundRect(
                color = progressColor,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = Size(width = progressSize, height = size.height)
            )

            drawRoundRect(
                color = progressShadow,
                cornerRadius = CornerRadius(barHeight / 2, barHeight / 2),
                size = Size(width = progressSize - barHeight, height = size.height * 0.3f),
                topLeft = Offset(barHeight * 0.5f, barHeight * 0.2f)
            )
        }

        content()
    }
}