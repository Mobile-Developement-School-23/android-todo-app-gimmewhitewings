package com.example.todoapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.todoapp.R

@Immutable
data class ExtendedTypography(
    val largeTitle: TextStyle = TextStyle.Default,
    val title: TextStyle = TextStyle.Default,
    val button: TextStyle = TextStyle.Default,
    val body: TextStyle = TextStyle.Default,
    val subhead: TextStyle = TextStyle.Default
)

val defaultTypography = ExtendedTypography(
    largeTitle = TextStyle(
        fontSize = 32.sp,
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight(500),
    ),
    title = TextStyle(
        fontSize = 20.sp,
        lineHeight = 32.sp,
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.5.sp
    ),
    button = TextStyle(
        fontSize = 14.sp,
        lineHeight = 24.sp,
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight(500),
        letterSpacing = 0.16.sp,
    ),
    body = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight(400),
    ),
    subhead = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = FontFamily(Font(R.font.roboto)),
        fontWeight = FontWeight(400),
    )
)

val LocalExtendedTypography = staticCompositionLocalOf {
    ExtendedTypography()
}
