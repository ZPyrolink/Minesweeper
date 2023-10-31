package com.devinou971.minesweeperandroid.utils

internal operator fun Int.iterator(): Iterator<Int> = (0..this).iterator()