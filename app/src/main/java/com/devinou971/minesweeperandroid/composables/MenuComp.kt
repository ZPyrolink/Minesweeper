package com.devinou971.minesweeperandroid.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.navigation.Screen
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.utils.Difficulty
import com.devinou971.minesweeperandroid.utils.LocalDpSize
import com.devinou971.minesweeperandroid.utils.rememberMutableState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun MenuComp(
    navCtrl: NavController
) = Box(modifier = Modifier.fillMaxSize()) {
    IconButton(
        modifier = Modifier.align(Alignment.TopEnd),
        onClick = { navCtrl.navigate(Screen.Parameters) }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Open settings"
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val dpSize = LocalDpSize

        fun startGame(d: Difficulty) {
            val availableHeight = (dpSize.height.value * 0.80).toInt()
            val availableWidth = dpSize.width.value
            val nbCols = 10
            val cellSize = availableWidth / nbCols
            val nbRows = (availableHeight / cellSize).toInt()

            val size = SerializableIntSize(nbCols, nbRows)

            if (d == Difficulty.CUSTOM)
                navCtrl.navigate(Screen.CustomGameSettings(size))
            else
                navCtrl.navigate(
                    Screen.Game(
                        size,
                        cellSize.toInt(),
                        d.nbBombs(nbCols, nbRows),
                        d
                    )
                )
        }

        for (d in Difficulty.entries)
            LevelBtn(d, ::startGame)
    }
}

@Composable
fun LevelBtn(
    difficulty: Difficulty,
    onClick: (Difficulty) -> Unit
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(5.dp)
) {
    Button(onClick = { onClick(difficulty) }) {
        Text(text = difficulty.name)
    }

    if (difficulty == Difficulty.CUSTOM)
        return

    val ctx = LocalContext.current

    var hightscore: Duration? by rememberMutableState(value = null)

    LaunchedEffect(key1 = Unit) {
        val tmp: Duration = withContext(Dispatchers.IO) {
            val data = AppDatabase.getAppDataBase(ctx).gameDataDAO
                .getBestTimeForDifficulty(difficulty.id) ?: return@withContext (-1).seconds

            data.time.seconds
        }

        withContext(Dispatchers.Main) {
            hightscore = tmp
        }
    }

    Text(
        text = when (hightscore) {
            (-1).seconds -> stringResource(id = R.string.no_highscore_yet)
            null -> "Loading..."
            else -> hightscore.toString()
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() = MinesweeperAndroidTheme {
    Surface {
        MenuComp(rememberNavController())
    }
}