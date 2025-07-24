package com.devinou971.minesweeperandroid.states

import android.graphics.Point
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.extensions.nextTo
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme

private const val TAG = "SlotState"

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
        Log.i(TAG, "reveal: $position")
        revealed = true
    }

    fun switchFlag() {
        Log.i(TAG, "switchFlag: $position")
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