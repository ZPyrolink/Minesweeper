package com.devinou971.minesweeperandroid.ui.theme.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.devinou971.minesweeperandroid.Settings

abstract class SlotColorScheme(val content: List<Color> = emptyList()) {
    open operator fun get(i: Int): Color = content[i]

    class Default(darkTheme: Boolean) : SlotColorScheme(if (darkTheme) dark else light) {
        override operator fun get(i: Int) = Settings.colors.ifEmpty { content }[i]
    }

    companion object {
        val Local = staticCompositionLocalOf<SlotColorScheme> { Default(true) }

        val light = listOf(
            Color.Blue,
            Color(0xFF008000),
            Color(0xFF8B0000),

            Color(0xff1e90ff),
            Color(0xff006400),
            Color.Magenta,

            Color(0xFFFF97A5),
            Color.Cyan,
            Color(0xFFffd700)
        )

        val dark = listOf(
            Color.Cyan,
            Color.Green,
            Color.Red,

            Color(0xff4169e1),
            Color(0xff006400),
            Color.Magenta,

            Color(0xFFFF97A5),
            Color(0XFFADD8E6),
            Color(0xFFffd700)
        )
    }
}