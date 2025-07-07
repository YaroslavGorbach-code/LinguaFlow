package com.korop.yaroslavhorach.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.korop.yaroslavhorach.designsystem.theme.KellyGreen
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExerciseProgressContainer(
    moveElementRight: Boolean,
    size: Dp = 80.dp,
    paddingBetween: Dp = 24.dp,
    progress: Float,
    content: @Composable BoxScope.() -> Unit,
) {
    val totalSize = size + paddingBetween * 2

    val offset = if (moveElementRight) size / 1.3f else -size / 1.3f

    Box(
        modifier = Modifier
            .size(totalSize)
            .offset(x = offset),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(totalSize)) {
            val strokeWidth = 12.dp.toPx()
            val cornerRadius = 16.dp.toPx()

            val innerSize = this.size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val rectSize = Size(innerSize, innerSize)

            drawRoundRect(
                color = Color.LightGray.copy(alpha = 0.3f),
                topLeft = topLeft,
                size = rectSize,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(width = strokeWidth)
            )

            val side = innerSize - 2 * cornerRadius
            val arcLength = (Math.PI * cornerRadius / 2f).toFloat()
            val totalLength = 4 * side + 4 * arcLength
            val targetLength = totalLength * progress

            val path = Path()
            var drawnLength = 0f

            fun addSegment(from: Offset, to: Offset): Offset {
                val segmentLength = (to - from).getDistance()
                val remaining = targetLength - drawnLength
                if (remaining <= 0f) return from

                return if (remaining >= segmentLength) {
                    path.lineTo(to.x, to.y)
                    drawnLength += segmentLength
                    to
                } else {
                    val direction = to - from
                    val length = direction.getDistance()
                    val unit = Offset(direction.x / length, direction.y / length)
                    val partial = Offset(from.x + unit.x * remaining, from.y + unit.y * remaining)
                    path.lineTo(partial.x, partial.y)
                    drawnLength = targetLength
                    partial
                }
            }

            fun addArc(rect: Rect, startAngle: Float): Offset {
                val remaining = targetLength - drawnLength
                if (remaining <= 0f) return path.getLastPoint()

                val sweepAngle = (remaining / arcLength).coerceAtMost(1f) * 90f
                path.arcTo(rect, startAngle, sweepAngle, false)
                drawnLength += (sweepAngle / 90f) * arcLength

                val radians = Math.toRadians((startAngle + sweepAngle).toDouble())
                val center = Offset(rect.left + rect.width / 2f, rect.top + rect.height / 2f)
                val rx = rect.width / 2f
                val ry = rect.height / 2f
                return Offset(
                    (center.x + rx * cos(radians)).toFloat(),
                    (center.y + ry * sin(radians)).toFloat()
                )
            }

            var current = Offset(topLeft.x + cornerRadius, topLeft.y)
            path.moveTo(current.x, current.y)

            current = addSegment(current, Offset(topLeft.x + innerSize - cornerRadius, topLeft.y))

            current = addArc(
                Rect(
                    topLeft.x + innerSize - 2 * cornerRadius,
                    topLeft.y,
                    topLeft.x + innerSize,
                    topLeft.y + 2 * cornerRadius
                ),
                270f
            )

            current = addSegment(current, Offset(topLeft.x + innerSize, topLeft.y + innerSize - cornerRadius))

            current = addArc(
                Rect(
                    topLeft.x + innerSize - 2 * cornerRadius,
                    topLeft.y + innerSize - 2 * cornerRadius,
                    topLeft.x + innerSize,
                    topLeft.y + innerSize
                ),
                0f
            )

            current = addSegment(current, Offset(topLeft.x + cornerRadius, topLeft.y + innerSize))

            current = addArc(
                Rect(
                    topLeft.x,
                    topLeft.y + innerSize - 2 * cornerRadius,
                    topLeft.x + 2 * cornerRadius,
                    topLeft.y + innerSize
                ),
                90f
            )

            current = addSegment(current, Offset(topLeft.x, topLeft.y + cornerRadius))

            addArc(
                Rect(
                    topLeft.x,
                    topLeft.y,
                    topLeft.x + 2 * cornerRadius,
                    topLeft.y + 2 * cornerRadius
                ),
                180f
            )

            drawPath(
                path = path,
                color = KellyGreen,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        content()
    }
}

private fun Path.getLastPoint(): Offset {
    return try {
        val pathMeasure = android.graphics.PathMeasure(asAndroidPath(), false)
        val pos = FloatArray(2)
        pathMeasure.getPosTan(pathMeasure.length, pos, null)
        Offset(pos[0], pos[1])
    } catch (_: Exception) {
        Offset.Zero
    }
}
