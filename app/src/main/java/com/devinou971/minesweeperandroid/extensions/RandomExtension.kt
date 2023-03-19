package com.devinou971.minesweeperandroid.extensions

import android.graphics.Point
import java.time.Instant
import kotlin.random.Random
import kotlin.random.nextInt

fun Random() = Random(Instant.now().epochSecond)
fun Random.nextPoint(xRange: IntRange, yRange: IntRange) = Point(nextInt(xRange), nextInt(yRange))