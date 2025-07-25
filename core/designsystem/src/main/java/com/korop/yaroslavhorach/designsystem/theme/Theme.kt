package com.korop.yaroslavhorach.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    background = Cultured,
    onBackground = BrightGray,
    surface = White,
    secondary = BlockOneDark,
    primary = BlockOneLight,
    primaryContainer = Calamansi,
    onPrimaryContainer = BlockOneLight
)

@Composable
fun ColorScheme.typoPrimary() = if (isSystemInDarkTheme()) DarkLiver else DarkLiver
@Composable
fun ColorScheme.typoSecondary() = if (isSystemInDarkTheme()) DavysGrey else DavysGrey
@Composable
fun ColorScheme.typoDisabled() = if (isSystemInDarkTheme()) DarkGray else DarkGray
@Composable
fun ColorScheme.typoControlPrimary() = if (isSystemInDarkTheme()) White else White
@Composable
fun ColorScheme.typoControlSecondary() = if (isSystemInDarkTheme()) White_90 else White_90
@Composable
fun ColorScheme.divider() = if (isSystemInDarkTheme()) Black10 else Black10
@Composable
fun ColorScheme.secondaryIcon() = if (isSystemInDarkTheme()) AmericanSilver else AmericanSilver
@Composable
fun ColorScheme.primaryIcon() = if (isSystemInDarkTheme()) DarkSilver else DarkSilver
@Composable
fun ColorScheme.alert() = if (isSystemInDarkTheme()) Alert else Alert
@Composable
fun ColorScheme.onBackgroundDark() = if (isSystemInDarkTheme()) Gainsboro else Gainsboro

/**
 * Dark default theme color scheme
 */
@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    background = Cultured,
    onBackground = BrightGray,
    surface = White,
    secondary = BlockOneDark,
    primary = BlockOneLight
)

@Composable
fun LinguaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    primaryColor: Color? = null,
    secondaryColor: Color? = null,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            if (darkTheme) {
                dynamicDarkColorScheme(LocalContext.current)
            } else {
                dynamicLightColorScheme(LocalContext.current)
            }
        }
        darkTheme -> DarkDefaultColorScheme.copy(
            primary = primaryColor ?: DarkDefaultColorScheme.primary,
            secondary = secondaryColor ?: DarkDefaultColorScheme.secondary
        )
        else -> LightDefaultColorScheme.copy(
            primary = primaryColor ?: LightDefaultColorScheme.primary,
            secondary = secondaryColor ?: LightDefaultColorScheme.secondary
        )
    }

    MaterialTheme(colorScheme = colorScheme, content = content)
}
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
