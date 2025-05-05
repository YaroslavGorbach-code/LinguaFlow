package com.example.yaroslavhorach.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.yaroslavhorach.designsystem.R

object LinguaTypography {
    val popinsBold = FontFamily(fonts = listOf(Font(resId = R.font.poppins_bold)))
    val popinsSemiBold = FontFamily(fonts = listOf(Font(resId = R.font.poppins_semi_bold)))
    val popinsMedium = FontFamily(fonts = listOf(Font(resId = R.font.poppins_medium)))
    val popinsRegular = FontFamily(fonts = listOf(Font(resId = R.font.poppins_regular)))

    val h2: TextStyle = TextStyle(fontFamily = popinsSemiBold, fontSize = 30.sp)
    val h3: TextStyle = TextStyle(fontFamily = popinsBold, fontSize = 24.sp)
    val h4: TextStyle = TextStyle(fontFamily = popinsBold, fontSize = 18.sp)
    val h5: TextStyle = TextStyle(fontFamily = popinsBold, fontSize = 15.sp)

    val subtitle2: TextStyle = TextStyle(fontFamily = popinsSemiBold, fontSize = 16.sp)
    val subtitle3: TextStyle = TextStyle(fontFamily = popinsSemiBold, fontSize = 12.sp)
    val subtitle4: TextStyle = TextStyle(fontFamily = popinsSemiBold, fontSize = 8.sp)

    val body3: TextStyle = TextStyle(fontFamily = popinsMedium, fontSize = 14.sp)
    val body4: TextStyle = TextStyle(fontFamily = popinsMedium, fontSize = 12.sp)
    val body5: TextStyle = TextStyle(fontFamily = popinsMedium, fontSize = 8.sp)
}
