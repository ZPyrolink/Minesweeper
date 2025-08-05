package com.devinou971.minesweeperandroid.states

import android.graphics.Point
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.devinou971.minesweeperandroid.extensions.nextTo

sealed class SlotState(
    val position: Point
) {
    enum class State(val str: String? = null) {
        Hidded("#"),
        Flagged("F"),
        Revealed;

        fun switchFlag(): State {
            return when (this) {
                Hidded -> Flagged
                Flagged -> Hidded
                else -> throw IllegalStateException("A $this cannot switch flag")
            }
        }
    }

    class Null(position: Point) : SlotState(position)

    class Bomb(position: Point) : SlotState(position)

    class Number(position: Point, val nbBombs: Int) : SlotState(position)

    var state: State by mutableStateOf(State.Hidded)
        private set

    val revealed get() = state == State.Revealed
    val flagged get() = state == State.Flagged

    fun reveal() {
        state = State.Revealed
    }

    fun switchFlag() {
        state = state.switchFlag()
    }

    override fun toString(): String = state.str ?: if (this is Number) "$nbBombs" else "B"

    fun getUnseenNeighbors(grid: List<List<SlotState>>): List<SlotState> =
        grid.flatMap { it.asSequence() }
            .filter { x -> position.nextTo(x.position, false) && x.state != State.Revealed }

    fun xRayView() = if (this is Number) "$nbBombs" else "B"

    fun hide() {
        state = State.Hidded
    }
}