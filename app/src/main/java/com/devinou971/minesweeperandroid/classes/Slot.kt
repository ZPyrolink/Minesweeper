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

    fun getUnseenNeighbors(grid: MutableList<MutableList<Slot>>): MutableList<Slot> {
        val neighbors = mutableListOf<Slot>()
        grid.forEach { line -> neighbors.addAll(line.filter { x -> nextTo(x) && !x.isRevealed }) }
        return neighbors
    }

    fun xRayView() = if (isBomb) "B" else "$nbBombs"

    fun hide() {
        isRevealed = false
    }
}