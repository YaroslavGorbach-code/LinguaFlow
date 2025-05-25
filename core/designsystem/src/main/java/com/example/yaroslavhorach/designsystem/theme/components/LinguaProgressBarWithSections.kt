package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.yaroslavhorach.designsystem.theme.Gainsboro
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
import com.example.yaroslavhorach.designsystem.theme.Menthol

@Composable
fun SectionedLinguaProgressBar(
    currentValue: Int,
    maxValue: Int = 30,
    sections: List<Int> = listOf(10, 20, 30),
    modifier: Modifier = Modifier,
    progressBarHeight: Dp = 20.dp,
    circleSize: Dp = 36.dp,
    backgroundColor: Color = Gainsboro,
    progressColor: Color = KellyGreen,
    progressShadow: Color = Menthol,
    textColor: Color = Color.Black,
    activeTextColor: Color = Color.White
) {
    val progress = currentValue.coerceIn(0, maxValue) / maxValue.toFloat()
    val animatedProgress = remember { Animatable(progress) }

    LaunchedEffect(progress) {
        if (animatedProgress.value == 1f) {
            animatedProgress.snapTo(0f)
        } else {
            animatedProgress.animateTo(
                progress,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(circleSize)
    ) {
        val totalWidth = constraints.maxWidth.toFloat()
        val density = LocalDensity.current
        val circleSizePx = with(density) { circleSize.toPx() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(circleSize)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(progressBarHeight)
                    .align(Alignment.Center)
            ) {
                val progressWidth = size.width * animatedProgress.value

                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(size.height / 2),
                    size = size
                )

                drawRoundRect(
                    color = progressColor,
                    cornerRadius = CornerRadius(size.height / 2),
                    size = Size(width = progressWidth, height = size.height)
                )

                val shadowPaddingX = size.height * 0.5f
                drawRoundRect(
                    color = progressShadow,
                    cornerRadius = CornerRadius(size.height / 2),
                    size = Size(
                        width = (progressWidth - shadowPaddingX * 2).coerceAtLeast(0f),
                        height = size.height * 0.3f
                    ),
                    topLeft = Offset(x = shadowPaddingX, y = size.height * 0.2f)
                )
            }

            sections.forEachIndexed { index, sectionValue ->
                val sectionProgress = sectionValue / maxValue.toFloat()
                val xOffsetPx = totalWidth * sectionProgress
                val isActive = currentValue >= sectionValue

                Box(
                    modifier = Modifier
                        .absoluteOffset {
                            IntOffset(
                                x = (xOffsetPx - circleSizePx / 2).toInt(),
                                y = 0
                            )
                        }
                        .size(circleSize)
                        .zIndex(1f)
                        .background(
                            color = if (isActive) progressColor else backgroundColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val text = if (index == sections.lastIndex){
                        "$sectionValue+"
                    } else {
                        sectionValue.toString()
                    }

                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isActive) activeTextColor else textColor
                    )
                }
            }
        }
    }
}
