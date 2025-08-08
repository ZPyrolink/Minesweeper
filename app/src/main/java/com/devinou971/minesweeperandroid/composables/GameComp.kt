package com.devinou971.minesweeperandroid.composables

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.navigation.Screen
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.states.MinesweeperBoardState
import com.devinou971.minesweeperandroid.states.SlotState
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.utils.Difficulty
import com.devinou971.minesweeperandroid.viewmodels.ChronoVM
import kotlin.random.Random

@Composable
fun GameComp(
    initialState: MinesweeperBoardState,
    navCtrl: NavController,
    cellSize: Int
) {
    var board by remember { mutableStateOf(initialState) }
    val chrono = remember { ChronoVM() }

    var navigateToHome by remember { mutableStateOf(false) }
    LaunchedEffect(navigateToHome) {
        if (navigateToHome)
            navCtrl.popBackStack(Screen.DifficultyChooser, false)
    }

    fun replay() {
        chrono.reset()
        board = board.createNew()
    }

    @Composable
    fun EndGame(
        @StringRes text: Int,
        anotherChance: Boolean
    ) {
        chrono.stop()

        GameOverDialog(
            text = text,
            onReplay = ::replay,
            onAnotherChance = if (anotherChance) {
                {
                    chrono.start()
                    board.revive()
                }
            } else null,
            onReturnToMenu = { navigateToHome = true }
        )
    }

    if (board.gameOver && !navigateToHome)
        EndGame(
            text = R.string.gameover,
            true
        )

    if (board.won && !navigateToHome) {
        chrono.save(
            LocalContext.current,
            board.difficulty
        )

        EndGame(
            text = R.string.you_won_string,
            false
        )
    }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "${board.nbFlags}")
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(Settings.theme[R.drawable.flagicon]),
                    contentDescription = "Flags"
                )
            }
            IconButton(
                onClick = ::replay
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload"
                )
            }

            Text(text = chrono.currentTime.toComponents { minutes, seconds, _ ->
                "%02d:%02d".format(minutes, seconds)
            })

            SlotComp.ImageOnTile(
                40,
                Settings.theme[board.mode.icon],
                desc = board.mode.name,
                onClick = board::changeMode
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(cellSize.dp)
            ) {
                items(board.points.asSequence().toList()) {
                    SlotComp(
                        board[it],
                        size = cellSize
                    ) {
                        if (board.isFirstTouch)
                            chrono.start()

                        board.slotClick(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun GameOverDialog(
    @StringRes text: Int,
    onReplay: () -> Unit,
    onReturnToMenu: () -> Unit,
    onAnotherChance: (() -> Unit)? = null
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
            text = stringResource(text),
            fontSize = 50.sp,
            color = Color(0xFFE6E0E9)
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

            if (onAnotherChance != null) {
                TextButton(
                    resource = R.string.another_chance,
                    fontSize = 25,
                    onClick = onAnotherChance
                )
            }


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

private fun generatePrevBoard() = MinesweeperBoardState(
    SerializableIntSize(nbC, nbC),
    8,
    Difficulty.CUSTOM
)

@Preview(widthDp = width)
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) {
    Surface {
        GameComp(
            initialState = generatePrevBoard(),
            navCtrl = rememberNavController(),
            cellSize = width / nbC
        )
    }
}

@Preview(widthDp = width)
@Composable
private fun PreviewGenerated() = MinesweeperAndroidTheme(true) {
    Surface {
        val game = generatePrevBoard()
            .apply { slotClick(Point()) }

        GameComp(
            initialState = game,
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
@Preview(widthDp = width, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGO() = MinesweeperAndroidTheme {
    Surface {
        val game = generatePrevBoard()
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
            initialState = game,
            navCtrl = rememberNavController(),
            cellSize = width / nbC
        )
    }
}

@Preview(widthDp = width)
@Preview(widthDp = width, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewWin() = MinesweeperAndroidTheme {
    Surface {
        val game = generatePrevBoard()
            .apply {
                slotClick(Point())

                for (point in points) {
                    val slot = get(point)
                    if (!slot.revealed && slot is SlotState.Number)
                        slotClick(point)
                }
            }

        GameComp(
            initialState = game,
            navCtrl = rememberNavController(),
            cellSize = width / nbC
        )
    }
}

//endregion