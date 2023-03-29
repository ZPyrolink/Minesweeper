package com.devinou971.minesweeperandroid.extensions

fun startThread(runnable: Runnable) = Thread(runnable).apply { start() }