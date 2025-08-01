package com.devinou971.minesweeperandroid.extensions

import androidx.compose.ui.graphics.Color

fun Color.toHexString(): String = ((value shr 32) and 0x00FFFFFFu)
    .toString(16)
    .padStart(6, '0')

operator fun Color.unaryMinus(): Color = copy(
    red = 1f - red,
    green = 1f - green,
    blue = 1f - blue,
)