package com.devinou971.minesweeperandroid.states

import android.graphics.Point
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.extensions.countNeighbors
import com.devinou971.minesweeperandroid.extensions.nextPoint
import com.devinou971.minesweeperandroid.extensions.nextTo
import com.devinou971.minesweeperandroid.extensions.until
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.utils.Difficulty
import kotlin.random.Random

class MinesweeperBoardState(
    private val size: SerializableIntSize,
    private val nbBombs: Int,
    val difficulty: Difficulty
) {
    enum class Mode(@DrawableRes val icon: Int) {
        REVEAL(R.drawable.pickaxeicon),
        FLAG(R.drawable.flagicon);

        val next
            get() = when (this) {
                REVEAL -> FLAG
                FLAG -> REVEAL
            }
    }

    val nbCols get() = size.width
    val nbRows get() = size.height

    var mode: Mode by mutableStateOf(Mode.REVEAL)
        private set

    fun changeMode() {
        mode = mode.next
    }

    private var grid: List<List<SlotState>>? by mutableStateOf(null)

    val points get() = Point().until(nbRows, nbCols)

    var nbFlags = nbBombs
        private set

    var gameOver by mutableStateOf(false)
        private set
    val isFirstTouch: Boolean get() = grid == null

    operator fun get(row: Int, columns: Int): SlotState =
        grid?.get(row)?.get(columns) ?: SlotState.Null(Point(columns, row))

    operator fun get(co: Point) = this[co.y, co.x]

    fun xRayView(): String = grid?.joinToString("\n") { line ->
        line.joinToString(" ") { slot -> slot.xRayView() }
    } ?: "Not generated"

    private fun generate(firstTouch: Point) {
        val bombs = mutableListOf<Point>()

        repeat(nbBombs) {
            var point: Point
            do {
                point = Random.nextPoint(
                    xRange = 0 until nbCols,
                    yRange = 0 until nbRows
                )
            } while (point.nextTo(firstTouch) || point in bombs)
            bombs.add(point)
        }

        grid = List(nbRows) { r ->
            List(nbCols) { c ->
                val point = Point(c, r)
                if (point in bombs)
                    SlotState.Bomb(point)
                else
                    SlotState.Number(point, point.countNeighbors(bombs))
            }
        }

        Log.i("xRayView", xRayView())
    }

    fun slotClick(position: Point) {
        if (gameOver)
            return

        if (isFirstTouch)
            generate(position)

        val slot = get(position)
        when (mode) {
            Mode.REVEAL -> reveal(slot)
            Mode.FLAG -> switchFlag(slot)
        }
    }

    private fun reveal(slot: SlotState) {
        when {
            slot.flagged -> return
            slot is SlotState.Bomb -> {
                gameOver = true
                slot.reveal()
            }

            slot is SlotState.Number -> {
                Log.d("Reveal", "Revealing ${slot.position}")
                slot.reveal()

                if (slot.nbBombs != 0)
                    return

                for (neighbor in slot.getUnseenNeighbors(grid!!)) {
                    if (neighbor is SlotState.Bomb || neighbor.flagged || neighbor.revealed)
                        continue

                    reveal(neighbor)
                }
            }
        }
    }

    private fun switchFlag(slot: SlotState): Boolean {
        if (!slot.revealed) {
            if (slot.flagged)
                nbFlags++
            else if (nbFlags > 0)
                nbFlags--

            slot.switchFlag()
        }

        return slot.flagged
    }

    override fun toString() =
        grid?.joinToString("\n") { it.joinToString(" ") } ?: "Not generated"

    val won: Boolean
        get() = grid?.run {
            asSequence()
                .flatMap { it.asSequence() }
                .all { slot -> slot.revealed || slot is SlotState.Bomb }
        } ?: false

    fun revive() {
        gameOver = false
        grid?.run {
            asSequence()
                .flatMap { it.asSequence() }
                .filter { slot -> slot is SlotState.Bomb && slot.revealed }
                .forEach { it.hide() }
        }
    }

    fun createNew() = MinesweeperBoardState(size, nbBombs, difficulty)
}