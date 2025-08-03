package com.devinou971.minesweeperandroid.states

import android.graphics.Point
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.devinou971.minesweeperandroid.extensions.nextTo

sealed class SlotState(
    val position: Point
) {
    object State {
        const val BOMB = "B"
        const val FLAG = "F"
        const val HIDE = "#"
    }

    class Null(position: Point) : SlotState(position)

    class Bomb(position: Point) : SlotState(position)

    class Number(position: Point, val nbBombs: Int) : SlotState(position)

    var revealed: Boolean by mutableStateOf(false)
        private set

    var flagged: Boolean by mutableStateOf(false)
        private set

    fun reveal() {
        revealed = true
    }

    fun switchFlag() {
        flagged = !flagged
    }

    override fun toString(): String = when {
        flagged -> State.FLAG
        revealed -> if (this is Number) "$nbBombs" else State.BOMB
        else -> State.HIDE
    }

    fun getUnseenNeighbors(grid: List<List<SlotState>>): List<SlotState> =
        grid.flatMap { it.asSequence() }
            .filter { x -> position.nextTo(x.position, false) && !x.revealed }

    fun xRayView() = if (this is Number) "$nbBombs" else State.BOMB

    fun hide() {
        revealed = false
    }
}