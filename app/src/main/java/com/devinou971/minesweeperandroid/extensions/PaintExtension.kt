package com.devinou971.minesweeperandroid.extensions

import android.graphics.Paint

fun Paint(color: Int, textSize: Float) = Paint(0).apply {
    this.color = color
    this.textSize = textSize
}