package com.devinou971.minesweeperandroid.extensions


import android.graphics.Point
import android.graphics.PointF
import android.view.MotionEvent

fun Point.nextTo(o: Point, inclusive: Boolean = true): Boolean =
    o.y in this.y - 1..this.y + 1 &&
            o.x in this.x - 1..this.x + 1 &&
            (inclusive || o != this)

fun Point.countNeighbors(l: MutableList<Point>): Int {
    return l.count { x -> this.nextTo(x) }
}

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

operator fun Point.times(factor: Int) = Point(x * factor, y * factor)

fun Point.until(rows: Int, columns: Int) = object : Iterator<Point> {
    private lateinit var current: Point

    private fun nextIndices(): Point {
        if (!::current.isInitialized)
            return Point(x, y)

        return if (current.x + 1 >= columns)
            Point(x, current.y + 1) else
            current + Point(1, 0)
    }

    override fun hasNext(): Boolean {
        val next = nextIndices()
        return next.y in y until rows && next.x in x until columns
    }

    override fun next(): Point {
        current = nextIndices()
        return current
    }
}

fun PointF(e: MotionEvent) = PointF(e.x, e.y)