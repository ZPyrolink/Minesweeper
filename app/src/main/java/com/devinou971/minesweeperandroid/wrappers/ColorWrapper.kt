package com.devinou971.minesweeperandroid.wrappers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object ColorWrapper : StringWrapper<Color>, IntWrapper<Color> {
    override val stringWrapper: Wrapper<Color, String> = object : Wrapper<Color, String> {
        override fun from(t: String): Color = Color(t.toULong(16))
        override fun to(t: Color): String = t.value.toString(16)
    }

    override val intWrapper: Wrapper<Color, Int> = object : Wrapper<Color, Int> {
        override fun from(t: Int): Color = Color(t)
        override fun to(t: Color): Int = t.toArgb()
    }
}