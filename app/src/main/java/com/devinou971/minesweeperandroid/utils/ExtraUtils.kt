package com.devinou971.minesweeperandroid.utils

import android.content.Intent
import java.io.Serializable

enum class ExtraUtils {
    NB_ROWS,
    NB_COLS,
    NB_BOMBS,
    CELL_SIZE,
    DIFFICULTY,

    TIMER_UPDATED,
    TIME_EXTRA;
}

fun Intent.putExtras(rows: Int, cols: Int, bombs: Int, cellSize: Int) {
    putExtra(ExtraUtils.NB_ROWS, rows);
    putExtra(ExtraUtils.NB_COLS, cols);
    putExtra(ExtraUtils.NB_BOMBS, bombs);
    putExtra(ExtraUtils.CELL_SIZE, cellSize);
}

fun Intent.putExtras(
    rows: Int,
    cols: Int,
    bombs: Int,
    cellSize: Int,
    difficulty: Difficulty
) {
    putExtras(rows, cols, bombs, cellSize)
    putExtra(ExtraUtils.DIFFICULTY, difficulty);
}

fun <T> Intent.putExtra(extraUtils: ExtraUtils, value: T) {
    when (value) {
        is Int -> putExtra(extraUtils.name, value)
        is Serializable -> putExtra(extraUtils.name, value)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> Intent.getExtra(extraUtils: ExtraUtils, default: T): T {
    return when (default) {
        is Int -> getIntExtra(extraUtils.name, default) as T;
        is Double -> getDoubleExtra(extraUtils.name, default) as T
        is Serializable -> if (hasExtra(extraUtils.name)) (getSerializableExtra(extraUtils.name)
            ?: default) as T else default
        else -> throw IllegalArgumentException()
    }
}