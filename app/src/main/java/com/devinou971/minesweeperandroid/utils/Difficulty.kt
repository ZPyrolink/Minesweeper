package com.devinou971.minesweeperandroid.utils

enum class Difficulty(private val bombFactor: Double) {
    EASY(0.15),
    NORMAL(0.22),
    HARD(0.38);

    fun nbBombs(rows: Int, columns: Int) = (rows * columns * bombFactor).toInt()
}