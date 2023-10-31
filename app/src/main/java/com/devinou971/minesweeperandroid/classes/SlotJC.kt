package com.devinou971.minesweeperandroid.classes

import android.graphics.Point
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.extensions.nextTo

sealed class SlotJC(
    val position: Point
) {
    object State {
        const val BOMB = "B"
        const val FLAG = "F"
        const val HIDE = "#"
    }

    object Null : SlotJC(Point(-1, -1)) {
        @Composable
        override fun Rendering(cellSize: Int) = Unit
    }

    class Bomb(position: Point) : SlotJC(position) {
        @Composable
        override fun Rendering(cellSize: Int) = IconOnTile(
            cellSize = cellSize,
            icon = Settings.theme[R.drawable.bombicon],
            desc = "BOOM"
        )
    }

    class Number(position: Point, val nbBombs: Int) : SlotJC(position) {
        @Composable
        override fun Rendering(cellSize: Int) = Box(
            contentAlignment = Alignment.Center
        ) {
            if (nbBombs == 0) // We don't display when there are no bombs
                return

            Text(
                modifier = Modifier,
                text = nbBombs.toString(),
                color = Settings.newColors[nbBombs - 1],
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }
    }

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

    fun getUnseenNeighbors(grid: SnapshotStateList<SnapshotStateList<SlotJC>>): List<SlotJC> =
        grid.flatMap { it.asSequence() }
            .filter { x -> position.nextTo(x.position, false) && !x.revealed }

    fun xRayView() = if (this is Number) "$nbBombs" else State.BOMB

    fun hide() {
        revealed = false
    }

    @Composable
    protected fun IconOnTile(
        cellSize: Int,
        @DrawableRes icon: Int,
        desc: String,
        onClick: ((Point) -> Unit)? = null
    ) = Box(
        modifier = Modifier
            .size(cellSize.dp)
            .clickable { onClick?.invoke(position) }
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.tile),
            contentDescription = "Tile background"
        )

        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = icon),
            contentDescription = desc
        )
    }

    @Composable
    fun Render(cellSize: Int, board: MinesweeperBoardJC) {
        when {
            flagged -> IconOnTile(
                cellSize = cellSize,
                icon = Settings.theme[R.drawable.flagicon],
                desc = "Flagged tile"
            ) {
                if (board.mode == MinesweeperBoardJC.Mode.REVEAL)
                    return@IconOnTile

                board.switchFlag(position)
            }

            !revealed -> Icon(
                modifier = Modifier
                    .size(cellSize.dp)
                    .clickable {
                        when (board.mode) {
                            MinesweeperBoardJC.Mode.REVEAL -> board.reveal(position)
                            MinesweeperBoardJC.Mode.FLAG -> board.switchFlag(position)
                        }
                    },
                painter = painterResource(id = Settings.theme[R.drawable.emptytile]),
                contentDescription = "Hidden tile"
            )

            else -> Rendering(cellSize)
        }
    }

    @Composable
    protected abstract fun Rendering(cellSize: Int)
}