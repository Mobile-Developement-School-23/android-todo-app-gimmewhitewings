package com.example.todoapp.ui.theme.extended

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.todoapp.ui.theme.DarkBackElevated
import com.example.todoapp.ui.theme.DarkBackPrimary
import com.example.todoapp.ui.theme.DarkBackSecondary
import com.example.todoapp.ui.theme.DarkColorBlue
import com.example.todoapp.ui.theme.DarkColorGray
import com.example.todoapp.ui.theme.DarkColorGrayLight
import com.example.todoapp.ui.theme.DarkColorGreen
import com.example.todoapp.ui.theme.DarkColorRed
import com.example.todoapp.ui.theme.DarkLabelDisable
import com.example.todoapp.ui.theme.DarkLabelPrimary
import com.example.todoapp.ui.theme.DarkLabelSecondary
import com.example.todoapp.ui.theme.DarkLabelTertiary
import com.example.todoapp.ui.theme.DarkSupportOverlay
import com.example.todoapp.ui.theme.DarkSupportSeparator
import com.example.todoapp.ui.theme.LightBackElevated
import com.example.todoapp.ui.theme.LightBackPrimary
import com.example.todoapp.ui.theme.LightBackSecondary
import com.example.todoapp.ui.theme.LightColorBlue
import com.example.todoapp.ui.theme.LightColorGray
import com.example.todoapp.ui.theme.LightColorGrayLight
import com.example.todoapp.ui.theme.LightColorGreen
import com.example.todoapp.ui.theme.LightColorRed
import com.example.todoapp.ui.theme.LightLabelDisable
import com.example.todoapp.ui.theme.LightLabelPrimary
import com.example.todoapp.ui.theme.LightLabelSecondary
import com.example.todoapp.ui.theme.LightLabelTertiary
import com.example.todoapp.ui.theme.LightSupportOverlay
import com.example.todoapp.ui.theme.LightSupportSeparator
import com.example.todoapp.ui.theme.White

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
