package com.devinou971.minesweeperandroid.extensions

fun Int.toColorString() = (this and 0xff000000.inv().toInt()).toString(Int.HEX_BASE)
    .padStart(6, '0')

val Int.Companion.HEX_BASE get() = 16