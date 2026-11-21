package com.example.cutsteps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.cutsteps.R

val Pink50 = Color(0xFFFFF1F6)
val Pink100 = Color(0xFFFFDCE9)
val Pink200 = Color(0xFFFFC2DA)
val Pink300 = Color(0xFFFF9EC4)
val Pink400 = Color(0xFFFF7AB0)
val Pink500 = Color(0xFFFF5C9E)
val Pink600 = Color(0xFFEF3D87)
val Pink700 = Color(0xFFD12670)
val Berry = Color(0xFF9C2B5B)

@OptIn(ExperimentalTextApi::class)
val PixelFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
val PressStart2P = GoogleFont(name = "Press Start 2P")

@OptIn(ExperimentalTextApi::class)
val PixelFontFamily = FontFamily(
    Font(googleFont = PressStart2P, fontProvider = PixelFontProvider, weight = FontWeight.Normal)
)

private val CuteTypography = Typography(
    displayLarge = TextStyle(fontFamily = PixelFontFamily, fontSize = 26.sp, lineHeight = 34.sp),
    displayMedium = TextStyle(fontFamily = PixelFontFamily, fontSize = 22.sp, lineHeight = 30.sp),
    headlineMedium = TextStyle(fontFamily = PixelFontFamily, fontSize = 18.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontFamily = PixelFontFamily, fontSize = 16.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = PixelFontFamily, fontSize = 13.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = PixelFontFamily, fontSize = 12.sp, lineHeight = 20.sp),
    bodyMedium = TextStyle(fontFamily = PixelFontFamily, fontSize = 10.sp, lineHeight = 17.sp),
    labelLarge = TextStyle(fontFamily = PixelFontFamily, fontSize = 10.sp, lineHeight = 16.sp),
    labelMedium = TextStyle(fontFamily = PixelFontFamily, fontSize = 9.sp, lineHeight = 14.sp),
    labelSmall = TextStyle(fontFamily = PixelFontFamily, fontSize = 8.sp, lineHeight = 12.sp)
)

private val CuteColorScheme = lightColorScheme(
    primary = Pink500,
    onPrimary = Color.White,
    primaryContainer = Pink100,
    onPrimaryContainer = Berry,
    secondary = Pink300,
    onSecondary = Berry,
    tertiary = Pink400,
    background = Pink50,
    onBackground = Berry,
    surface = Color.White,
    onSurface = Berry
)

@Composable
fun CuteStepsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CuteColorScheme,
        typography = CuteTypography,
        content = content
    )
}
