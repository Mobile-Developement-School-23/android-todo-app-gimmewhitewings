package com.example.todoapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val supportSeparator: Color = Color.Unspecified,
    val supportOverlay: Color = Color.Unspecified,
    val labelPrimary: Color = Color.Unspecified,
    val labelSecondary: Color = Color.Unspecified,
    val labelTertiary: Color = Color.Unspecified,
    val labelDisable: Color = Color.Unspecified,
    val colorRed: Color = Color.Unspecified,
    val colorBlue: Color = Color.Unspecified,
    val colorGreen: Color = Color.Unspecified,
    val colorGray: Color = Color.Unspecified,
    val colorGrayLight: Color = Color.Unspecified,
    val colorWhite: Color = Color.Unspecified,
    val backPrimary: Color = Color.Unspecified,
    val backSecondary: Color = Color.Unspecified,
    val backElevated: Color = Color.Unspecified
)

val lightExtendedColors = ExtendedColors(
    supportSeparator = LightSupportSeparator,
    supportOverlay = LightSupportOverlay,
    labelPrimary = LightLabelPrimary,
    labelSecondary = LightLabelSecondary,
    labelTertiary = LightLabelTertiary,
    labelDisable = LightLabelDisable,
    colorRed = LightColorRed,
    colorBlue = LightColorBlue,
    colorGreen = LightColorGreen,
    colorGray = LightColorGray,
    colorGrayLight = LightColorGrayLight,
    colorWhite = White,
    backPrimary = LightBackPrimary,
    backSecondary = LightBackSecondary,
    backElevated = LightBackElevated
)

val darkExtendedColors = ExtendedColors(
    supportSeparator = DarkSupportSeparator,
    supportOverlay = DarkSupportOverlay,
    labelPrimary = DarkLabelPrimary,
    labelSecondary = DarkLabelSecondary,
    labelTertiary = DarkLabelTertiary,
    labelDisable = DarkLabelDisable,
    colorRed = DarkColorRed,
    colorBlue = DarkColorBlue,
    colorGreen = DarkColorGreen,
    colorGray = DarkColorGray,
    colorGrayLight = DarkColorGrayLight,
    colorWhite = White,
    backPrimary = DarkBackPrimary,
    backSecondary = DarkBackSecondary,
    backElevated = DarkBackElevated
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors()
}
