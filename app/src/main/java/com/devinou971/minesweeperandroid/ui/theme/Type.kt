package com.devinou971.minesweeperandroid.ui.theme

import android.R.attr.fontFamily
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
    displayLarge = Typography().displayLarge.copy(fontFamily = pixelatedPusab),
    displayMedium = Typography().displayMedium.copy(fontFamily = pixelatedPusab),
    displaySmall = Typography().displaySmall.copy(fontFamily = pixelatedPusab),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = pixelatedPusab),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = pixelatedPusab),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = pixelatedPusab),
    titleLarge = Typography().titleLarge.copy(fontFamily = pixelatedPusab),
    titleMedium = Typography().titleMedium.copy(fontFamily = pixelatedPusab),
    titleSmall = Typography().titleSmall.copy(fontFamily = pixelatedPusab),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = pixelatedPusab),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = pixelatedPusab),
    bodySmall = Typography().bodySmall.copy(fontFamily = pixelatedPusab),
    labelLarge = Typography().labelLarge.copy(fontFamily = pixelatedPusab),
    labelMedium = Typography().labelMedium.copy(fontFamily = pixelatedPusab),
    labelSmall = Typography().labelSmall.copy(fontFamily = pixelatedPusab)
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