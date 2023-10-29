package com.devinou971.minesweeperandroid.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.classes.MinesweeperBoard
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme

@Composable
fun GameComp(
    board: MinesweeperBoard,
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
            Image(
                painter = painterResource(id = R.drawable.emptytile),
                contentDescription = stringResource(id = R.string.change_mode_image)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val icons = mapOf(
            "B" to Settings.theme[R.drawable.bombicon],
            "F" to Settings.theme[R.drawable.flagicon],
            "#" to Settings.theme[R.drawable.emptytile]
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(cellSize.dp)
        ) {
            items(board.points.asSequence().toList()) {
                when (val res = board[it].toString()) {
                    "B", "F", "#" -> Icon(
                        modifier = Modifier
                            .size(cellSize.dp),
                        painter = painterResource(id = icons[res]!!),
                        contentDescription = res
                    )

                    "0" -> {} // We don't display when there are no bombs
                    else -> Text(
                        text = res,
                        color = Settings.newColors[res.toInt() - 1],
                        fontSize = 50.sp,
                    )
                }
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
        GameComp(
            board = MinesweeperBoard(nbC, nbC, 8),
            cellSize = width / nbC
        )
    }
}