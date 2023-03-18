package com.devinou971.minesweeperandroid.utils

import android.content.Intent
import java.io.Serializable

enum class ExtraUtils {
    NB_ROWS,
    NB_COLS,
    NB_BOMBS,
    CELL_SIZE,
    DIFFICULTY;
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

fun Intent.putExtra(extraUtils: ExtraUtils, value: Int) = putExtra(extraUtils.name, value)
fun Intent.putExtra(extraUtils: ExtraUtils, value: Serializable) = putExtra(extraUtils.name, value);
fun Intent.getIntExtra(extraUtils: ExtraUtils, default: Int) =
    getIntExtra(extraUtils.name, default);

fun <T : Serializable> Intent.getExtra(extraUtils: ExtraUtils, default: T) =
    if (hasExtra(extraUtils.name)) (getSerializableExtra(extraUtils.name) ?: default) as T else default