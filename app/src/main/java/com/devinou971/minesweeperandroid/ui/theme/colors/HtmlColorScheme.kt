package com.devinou971.minesweeperandroid.ui.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
class HtmlColorScheme private constructor(
    val gainsboro: Color = Color.Companion.Unspecified
) {
    companion object {
        val Local = staticCompositionLocalOf { HtmlColorScheme() }

        val Gainsboro = Color(0xffdcdcdc)

        val light = HtmlColorScheme(
            gainsboro = Gainsboro
        )

        val dark = HtmlColorScheme(
            gainsboro = Gainsboro
        )
    }
}