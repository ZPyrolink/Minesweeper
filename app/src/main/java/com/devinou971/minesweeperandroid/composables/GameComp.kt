package com.devinou971.minesweeperandroid.composables

import android.graphics.Point
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devinou971.minesweeperandroid.states.MinesweeperBoardState
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme

@Composable
fun GameComp(
    board: MinesweeperBoardState,
    cellSize: Int
) = Column(
    modifier = Modifier.fillMaxSize(),
) {
    Row(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Number here")
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reload"
        )

        Text(text = "00:00")
        Box {
            IconButton(
                onClick = board::changeMode
            ) {
                Image(
                    painter = painterResource(board.mode.icon),
                    contentDescription = board.mode.name
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(cellSize.dp)
        ) {
            items(board.points.asSequence().toList()) {
                SlotComp(
                    board[it],
                    size = cellSize
                ) { board.slotClick(it) }
            }
        }
    }
}

private const val width = 360
private const val nbC = 10

@Preview(widthDp = width)
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) {
    Surface {
        val game = MinesweeperBoardState(SerializableIntSize(nbC, nbC), 8)
        GameComp(
            board = game,
            cellSize = width / nbC
        )
    }
}

@Preview(widthDp = width)
@Composable
private fun PreviewGenerated() = MinesweeperAndroidTheme(true) {
    Surface {
        val game = MinesweeperBoardState(SerializableIntSize(nbC, nbC), 8)
            .apply { slotClick(Point()) }

        GameComp(
            board = game,
            cellSize = width / nbC
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) { Text(text = game.xRayView(), fontFamily = FontFamily.Default) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) { Text(text = game.toString(), fontFamily = FontFamily.Default) }
    }
}