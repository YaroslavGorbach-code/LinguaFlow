package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.yaroslavhorach.designsystem.theme.Black_35
import com.example.yaroslavhorach.designsystem.theme.OrangeDark
import com.example.yaroslavhorach.designsystem.theme.OrangeLight
import com.example.yaroslavhorach.designsystem.theme.White_10
import com.example.yaroslavhorach.ui.utils.conditional

@Composable
fun BoxWithLines(
    modifier: Modifier,
    stripeWidth: Dp = 60.dp,
    stripeSpacing: Dp = 150.dp,
    background: Color = OrangeLight,
    backgroundShadow: Color = OrangeDark,
    shadowOffset: Dp = (-3).dp,
    shape: Shape = RoundedCornerShape(12.dp),
    isActive: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = backgroundShadow)
            .offset(y = shadowOffset)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(color = background)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val rotationAngle = 30f
                val stripeLength = size.height * 3

                var startX = -stripeLength / 2
                while (startX < size.width + stripeLength) {
                    rotate(rotationAngle, pivot = Offset(startX, 0f)) {
                        drawRect(
                            color = White_10,
                            topLeft = Offset(startX, -stripeLength / 2),
                            size = Size(stripeWidth.toPx(), stripeLength)
                        )
                    }
                    startX += stripeSpacing.toPx()
                }
            }
        }
        content()
    }
}
