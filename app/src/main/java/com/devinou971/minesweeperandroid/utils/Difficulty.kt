package com.devinou971.minesweeperandroid.utils

enum class Difficulty(val id: Int, private val bombPercentage: Double) {
    EASY(0, .15),
    NORMAL(1, .22),
    HARD(2, .38),
    CUSTOM(-1, .0), ;

    fun nbBombs(columns: Int, rows: Int) = (rows * columns * bombPercentage).toInt()

    companion object {
        fun nbBombs(columns: Int, rows: Int, percentage: Float) =
            (rows * columns * percentage).toInt()
    }
}