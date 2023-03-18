package com.devinou971.minesweeperandroid.utils

enum class Difficulty(val id: Int, private val bombFactor: Double) {
    CUSTOM(-1, .0),
    EASY(0, .15),
    NORMAL(1, .22),
    HARD(2, .38);

    fun nbBombs(rows: Int, columns: Int) = (rows * columns * bombFactor).toInt()
}