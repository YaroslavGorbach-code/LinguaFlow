package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    percentage: Float,
    radius: Dp = 45.dp,
    strokeSize: Dp = 9.dp,
    color: Color,
    backgroundColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius * 2) // diameter
    ) {

        Canvas(modifier = Modifier.size(radius * 2)) {
            drawArc(
                brush = SolidColor(backgroundColor),
                startAngle = 90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    strokeSize.toPx(),
                    cap = StrokeCap.Round,
                ),
            )

            drawArc(
                brush = SolidColor(color),
                startAngle = 90f,
                sweepAngle = 360 * percentage,
                useCenter = false,
                style = Stroke(
                    strokeSize.toPx(),
                    cap = StrokeCap.Round,
                ),
            )
        }
    }
}