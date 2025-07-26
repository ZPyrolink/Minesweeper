package com.devinou971.minesweeperandroid.composables

import android.graphics.Point
import androidx.annotation.StringRes
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.navigation.Screen
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.states.MinesweeperBoardState
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import kotlin.random.Random

@Composable
fun GameComp(
    state: MinesweeperBoardState,
    navCtrl: NavController,
    cellSize: Int
) {
    var board by remember { mutableStateOf(state) }

    if (board.gameOver)
        GameOverDialog(
            onReplay = { board = board.createNew() },
            onAnotherChance = { board.revive() },
            onReturnToMenu = { navCtrl.popBackStack(Screen.DifficultyChooser, false) }
        )

    Column(
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
}

@Composable
private fun GameOverDialog(
    onReplay: () -> Unit,
    onAnotherChance: () -> Unit,
    onReturnToMenu: () -> Unit
) = Dialog(
    onDismissRequest = {},
    properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    )
) {
    Box(
        modifier = Modifier
            .fillMaxSize(.9f)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.gameover),
            fontSize = 50.sp
        )

        @Composable
        fun TextButton(
            modifier: Modifier = Modifier,
            @StringRes resource: Int,
            fontSize: Int,
            onClick: () -> Unit
        ) = Button(onClick) {
            Text(
                modifier = modifier,
                text = stringResource(resource),
                fontSize = fontSize.sp,
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                resource = R.string.replay,
                fontSize = 30,
                onClick = onReplay
            )

            TextButton(
                resource = R.string.another_chance,
                fontSize = 25,
                onClick = onAnotherChance
            )

            TextButton(
                resource = R.string.return_to_menu,
                fontSize = 20,
                onClick = onReturnToMenu
            )
        }
    }
}

//region Previews

private const val width = 411
private const val nbC = 10

@Preview(widthDp = width)
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) {
    Surface {
        val game = MinesweeperBoardState(SerializableIntSize(nbC, nbC), 8)
        GameComp(
            state = game,
            navCtrl = rememberNavController(),
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
            state = game,
            navCtrl = rememberNavController(),
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

@Preview(widthDp = width)
@Composable
private fun PreviewGO() = MinesweeperAndroidTheme(true) {
    Surface {
        val game = MinesweeperBoardState(SerializableIntSize(nbC, nbC), 20)
            .apply {
                while (!gameOver)
                    slotClick(
                        Point(
                            Random.nextInt(nbC),
                            Random.nextInt(nbC)
                        )
                    )
            }

        GameComp(
            state = game,
            navCtrl = rememberNavController(),
            cellSize = width / nbC
        )
    }
}

//endregion