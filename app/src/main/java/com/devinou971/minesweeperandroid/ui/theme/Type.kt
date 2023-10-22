package com.devinou971.minesweeperandroid.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.devinou971.minesweeperandroid.R

val alarmClok = FontFamily(Font(R.font.alarm_clock))
val pixelatedPusab = FontFamily(Font(R.font.pixelated_pusab))

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = pixelatedPusab),
    displayMedium = TextStyle(fontFamily = pixelatedPusab),
    displaySmall = TextStyle(fontFamily = pixelatedPusab),
    headlineLarge = TextStyle(fontFamily = pixelatedPusab),
    headlineMedium = TextStyle(fontFamily = pixelatedPusab),
    headlineSmall = TextStyle(fontFamily = pixelatedPusab),
    titleLarge = TextStyle(fontFamily = pixelatedPusab),
    titleMedium = TextStyle(fontFamily = pixelatedPusab),
    titleSmall = TextStyle(fontFamily = pixelatedPusab),
    bodyLarge = TextStyle(fontFamily = pixelatedPusab),
    bodyMedium = TextStyle(fontFamily = pixelatedPusab),
    bodySmall = TextStyle(fontFamily = pixelatedPusab),
    labelLarge = TextStyle(fontFamily = pixelatedPusab),
    labelMedium = TextStyle(fontFamily = pixelatedPusab),
    labelSmall = TextStyle(fontFamily = pixelatedPusab)
)

@Preview
@Composable
private fun AlarmClok() = MinesweeperAndroidTheme(true) {
    Surface {
        Column {
            Text(text = "123456789", fontFamily = alarmClok)
            Text(text = "123456789", fontFamily = alarmClok, fontWeight = FontWeight.Bold)
            Text(text = "123456789", fontFamily = alarmClok, fontStyle = FontStyle.Italic)
        }
    }
}

@Preview
@Composable
private fun PixelatedPusab() = MinesweeperAndroidTheme(true) {
    Surface {
        Column {
            Text(text = "Text", fontFamily = pixelatedPusab)
            Text(text = "Text", fontFamily = pixelatedPusab, fontWeight = FontWeight.Bold)
            Text(text = "Text", fontFamily = pixelatedPusab, fontStyle = FontStyle.Italic)
        }
    }
}