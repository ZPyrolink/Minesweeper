package com.devinou971.minesweeperandroid.extensions

import androidx.compose.ui.graphics.Color

fun Color.toHexString(): String = ((value shr 32) and 0x00FFFFFFu)
    .toString(16)
    .padStart(6, '0')