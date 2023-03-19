package com.devinou971.minesweeperandroid.classes

import android.graphics.Point
import com.devinou971.minesweeperandroid.extensions.nextTo

class Slot {
    var isRevealed: Boolean = false
        private set
    var isBomb: Boolean = false
    var nbBombs: Int = 0
    var isFlagged = false
    var position = Point()

    fun reveal() {
        isRevealed = true
    }

    override fun toString() = when {
        isFlagged -> "F"
        isRevealed -> if (!isBomb) "$nbBombs" else "B"
        else -> "#"
    }

    fun nextTo(o: Slot) = position.nextTo(o.position)

    fun getUnseenNeighbors(grid: Array<Array<Slot>>) = grid.asSequence()
        .flatMap { it.asSequence() }
        .filter { x -> nextTo(x) && !x.isRevealed }

    fun xRayView() = if (isBomb) "B" else "$nbBombs"

    fun hide() {
        isRevealed = false
    }
}