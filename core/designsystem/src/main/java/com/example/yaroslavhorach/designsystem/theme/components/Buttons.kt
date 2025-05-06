package com.example.yaroslavhorach.designsystem.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.yaroslavhorach.designsystem.theme.Gainsboro
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.disabledText
import com.example.yaroslavhorach.designsystem.theme.onBackgroundDark
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SecondaryButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    val rawShadowYOffset = 10.dp
    val shadowYOffset = remember { mutableStateOf(rawShadowYOffset) }

    Box(
        modifier = modifier
            .height(45.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val touch = awaitPointerEvent().changes.first()

                        if (touch.changedToDown()) {
                            shadowYOffset.value = 0.dp
                        }

                        if (touch.changedToUp()) {
                            shadowYOffset.value = rawShadowYOffset
                            onClick()
                        }
                    }
                }
            }

    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = Gainsboro)
        )

        Spacer(
            modifier = Modifier
                .offset { IntOffset(x = 0, -shadowYOffset.value.value.roundToInt()) }
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.surface)
        )

        Text(
            modifier = Modifier
                .offset { IntOffset(x = 0, -shadowYOffset.value.value.roundToInt()) }
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = LinguaTypography.subtitle2,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Composable
fun InactiveButton(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier
            .height(45.dp)
            .background(color = MaterialTheme.colorScheme.onBackgroundDark(), shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = LinguaTypography.subtitle2,
            color = MaterialTheme.colorScheme.disabledText()
        )
    }
}
