package com.devinou971.minesweeperandroid.classes

import android.graphics.Point
import com.devinou971.minesweeperandroid.extensions.*

class MinesweeperBoard(val nbRows: Int, val nbCols: Int, private val nbBombs: Int) {
    private val grid = Array(nbRows) { Array(nbCols) { Slot() } }
    val rows
        get() = grid.size
    val columns
        get() = grid[0].size

    var nbFlags = nbBombs
        private set
    var gameOver = false
        private set
    var isFirstTouch = true
        private set

    operator fun get(row: Int, columns: Int) = grid[row][columns]
    operator fun get(co: Point) = this[co.y, co.x]

    fun reveal(position: Point) {
        if (gameOver)
            return

        if (isFirstTouch) {
            addBombsDynamicallyToGrid(position)
            isFirstTouch = false
        }

        val slot = this[position]
        when {
            slot.isFlagged -> {}
            slot.isBomb -> {
                gameOver = true
                slot.reveal()
            }
            else -> {
                slot.reveal()
                if (slot.nbBombs != 0)
                    return

                for (neighbor in slot.getUnseenNeighbors(grid))
                    if (!neighbor.isBomb && !neighbor.isFlagged && !neighbor.isRevealed) {
                        neighbor.reveal()
                        reveal(neighbor.position)
                    }
            }
        }
    }

    override fun toString() =
        grid.joinToString("\n") { line -> line.joinToString(" ") }

    fun flag(position: Point): Boolean {
        val slot = this[position]

        if (!slot.isRevealed) {
            if (slot.isFlagged) {
                nbFlags++
                slot.isFlagged = false
            } else if (nbFlags > 0) {
                nbFlags--
                slot.isFlagged = true
            }
        }

        return slot.isFlagged
    }

    private fun addBombsDynamicallyToGrid(firstTouch: Point) {
        val bombs = mutableListOf<Point>()
        val numberGenerator = Random()
        for (i in 1..this.nbBombs) {
            var point: Point
            do {
                point = numberGenerator.nextPoint(
                    0 until nbCols,
                    0 until nbRows
                )
            } while (point.nextTo(firstTouch) || point in bombs)
            bombs.add(point)
        }

        for (p in Point().until(nbRows, nbCols)) {
            this[p].position = p
            if (this[p].position in bombs)
                this[p].isBomb = true
            else
                this[p].nbBombs = this[p].position.countNeighbors(bombs)
        }

        print("xRayView:\n${xRayView()}")
    }

    fun xRayView() = grid.joinToString("\n") { line ->
        line.joinToString(" ") { slot -> slot.xRayView() }
    }

    fun won() = grid.asSequence()
        .flatMap { it.asSequence() }
        .all { slot -> slot.isRevealed || slot.isBomb }

    fun revive() {
        gameOver = false
        grid.asSequence()
            .flatMap { it.asSequence() }
            .filter { it.isBomb && it.isRevealed }
            .forEach { it.hide() }
    }
}