package com.devinou971.minesweeperandroid.extensions

import android.graphics.Point
import kotlin.random.Random
import kotlin.random.nextInt

fun Random.nextPoint(xRange: IntRange, yRange: IntRange) = Point(nextInt(xRange), nextInt(yRange))