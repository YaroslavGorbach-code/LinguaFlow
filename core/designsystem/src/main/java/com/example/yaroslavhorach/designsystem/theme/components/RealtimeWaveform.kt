package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
import com.example.yaroslavhorach.designsystem.theme.LightSilver
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.exp
import kotlin.random.Random

@Composable
fun RealtimeWaveform(
    amplitude: Int,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier,
    barWidth: Dp = 8.dp,
    barSpacing: Dp = 4.dp
) {
    val amplitudeBoost = 13f
    val normalizedAmplitude = (amplitude / 32767f * amplitudeBoost).coerceIn(0f, 1f)

    val waveformColor by animateColorAsState(
        targetValue = if (isSpeaking) KellyGreen else LightSilver,
        animationSpec = tween(durationMillis = 600),
        label = "waveColor"
    )

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val barWidthPx = with(density) { barWidth.toPx() }
        val initialBarSpacingPx = with(density) { barSpacing.toPx() }
        val canvasWidthPx = constraints.maxWidth.toFloat()

        val estimatedTotalStep = barWidthPx + initialBarSpacingPx
        val estimatedBarCount = (canvasWidthPx / estimatedTotalStep).toInt().coerceAtLeast(1)

        val totalBarWidths = estimatedBarCount * barWidthPx
        val newBarSpacingPx = if (estimatedBarCount > 1) {
            (canvasWidthPx - totalBarWidths) / (estimatedBarCount - 1)
        } else {
            0f
        }

        val noise = remember(estimatedBarCount) { List(estimatedBarCount) { Random.nextFloat() * 0.15f - 0.075f } }
        val targetHeights = remember(estimatedBarCount) { List(estimatedBarCount) { Animatable(0f) } }

        LaunchedEffect(amplitude, estimatedBarCount) {
            val peakIndex = (estimatedBarCount * 0.4f).toInt()

            for (i in 0 until estimatedBarCount) {
                val distance = abs(i - peakIndex).toFloat()
                val fade = 0.10f + exp(-distance * 0.25f)
                val minHeight = 0.05f

                val height = (normalizedAmplitude * fade + noise[i])
                    .coerceIn(minHeight, 1f)

                launch {
                    targetHeights[i].animateTo(
                        targetValue = height,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerY = size.height / 2
            val maxBarHeight = size.height * 0.9f

            targetHeights.forEachIndexed { i, anim ->
                val x = i * (barWidthPx + newBarSpacingPx) + barWidthPx / 2f
                val barHeight = anim.value * maxBarHeight
                val yStart = centerY - barHeight / 2
                val yEnd = centerY + barHeight / 2

                drawLine(
                    color = waveformColor,
                    start = Offset(x, yStart),
                    end = Offset(x, yEnd),
                    strokeWidth = barWidthPx,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
