package com.devinou971.minesweeperandroid.classes

import android.graphics.Point
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.extensions.countNeighbors
import com.devinou971.minesweeperandroid.extensions.nextPoint
import com.devinou971.minesweeperandroid.extensions.nextTo
import com.devinou971.minesweeperandroid.extensions.until
import kotlin.random.Random

class MinesweeperBoardJC(
    val nbRows: Int,
    val nbCols: Int,
    private val nbBombs: Int
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

    var mode: Mode by mutableStateOf(Mode.REVEAL)
        private set

    private var grid: SnapshotStateList<SnapshotStateList<SlotJC>>? = null

    val points get() = Point().until(nbRows, nbCols)

    var nbFlags = nbBombs
        private set

    var gameOver = false
        private set
    val isFirstTouch: Boolean get() = grid == null

    operator fun get(row: Int, columns: Int): SlotJC = grid?.get(row)?.get(columns) ?: SlotJC.Null

    operator fun get(co: Point) = this[co.y, co.x]

    fun xRayView(): String = grid?.joinToString("\n") { line ->
        line.joinToString(" ") { slot -> slot.xRayView() }
    } ?: "Not generated"

    private fun generate(firstTouch: Point) {
        val bombs = mutableListOf<Point>()

        for (i in 1..nbBombs) {
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
                    SlotJC.Bomb(point)
                else
                    SlotJC.Number(point, point.countNeighbors(bombs))
            }.toMutableStateList()
        }.toMutableStateList()

        Log.i("xRayView", xRayView())
    }

    fun reveal(position: Point) {
        if (gameOver)
            return

        if (isFirstTouch)
            generate(position)

        val slot = get(position)
        when {
            slot.flagged -> {}
            slot is SlotJC.Bomb -> {
                gameOver = true
                slot.reveal()
            }

            slot is SlotJC.Number -> {
                Log.d("Generation", "Revealing ${slot.position}")
                slot.reveal()
                if (slot.nbBombs != 0)
                    return

                for (neighbor in slot.getUnseenNeighbors(grid!!)) {
                    if (neighbor is SlotJC.Bomb || neighbor.flagged || neighbor.revealed) {
                        Log.d("Generation", "Ignoring ${neighbor.position}")
                        continue
                    }

                    reveal(neighbor.position)
                }
            }
        }
    }

    override fun toString() =
        grid?.joinToString("\n") { it.joinToString(" ") } ?: "Not generated"

    fun switchFlag(position: Point): Boolean {
        val slot = this[position]

        if (!slot.revealed) {
            if (slot.flagged)
                nbFlags++
            else if (nbFlags > 0)
                nbFlags--

            slot.switchFlag()
        }

        return slot.flagged
    }

    val won: Boolean
        get() = grid?.asSequence()
            ?.flatMap { it.asSequence() }
            ?.all { slot -> slot.revealed || slot is SlotJC.Bomb }
            ?: false

    fun revive() {
        gameOver = false
        grid?.asSequence()
            ?.flatMap { it.asSequence() }
            ?.filter { slot -> slot is SlotJC.Bomb && slot.revealed }
            ?.forEach { it.hide() }
    }
}