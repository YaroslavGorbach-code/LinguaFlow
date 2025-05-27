package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.yaroslavhorach.designsystem.theme.OrangeDark
import com.example.yaroslavhorach.designsystem.theme.OrangeLight
import com.example.yaroslavhorach.designsystem.theme.White_10

@Composable
fun BoxWithStripes(
    modifier: Modifier = Modifier,
    stripeColor: Color = White_10,
    stripeWidth: Dp = 60.dp,
    stripeSpacing: Dp = 150.dp,
    background: Color = OrangeLight,
    backgroundShadow: Color = OrangeDark,
    shadowOffset: Dp = (-3).dp,
    contentPadding: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(12.dp),
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
                .clip(shape)
                .background(color = background)
                .drawBehind {
                    val stripePxWidth = stripeWidth.toPx()
                    val stripePxSpacing = stripeSpacing.toPx()
                    val stripeLength = size.height * 3
                    var startX = -stripeLength / 2

                    while (startX < size.width + stripeLength) {
                        rotate(30f, pivot = Offset(startX, 0f)) {
                            drawRect(
                                color = stripeColor,
                                topLeft = Offset(startX, -stripeLength / 2),
                                size = Size(stripePxWidth, stripeLength)
                            )
                        }
                        startX += stripePxSpacing
                    }
                }
        ) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }
    }
}
