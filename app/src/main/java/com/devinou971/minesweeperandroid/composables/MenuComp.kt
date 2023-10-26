package com.devinou971.minesweeperandroid.composables

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import com.devinou971.minesweeperandroid.CustomGameActivity
import com.devinou971.minesweeperandroid.GameActivity
import com.devinou971.minesweeperandroid.ParametersActivity
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.utils.Difficulty
import com.devinou971.minesweeperandroid.utils.ExtraUtils
import com.devinou971.minesweeperandroid.utils.putExtra
import com.devinou971.minesweeperandroid.utils.putExtras
import com.devinou971.minesweeperandroid.utils.rememberMutableState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun MenuComp() = Box(modifier = Modifier.fillMaxSize()) {
    val ctx = LocalContext.current

    IconButton(
        modifier = Modifier.align(Alignment.TopEnd),
        onClick = { startActivity(ctx, Intent(ctx, ParametersActivity::class.java), null) }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Open settings"
        )
    }

    Column {
        val view = LocalView.current

        fun startGame(d: Difficulty) {
            val availableHeight = (view.height * 0.80).toInt()
            val availableWidth = view.width
            val nbCols = 10
            val cellSize = availableWidth / nbCols
            val nbRows = availableHeight / cellSize

            ctx.startActivity(when (d) {
                Difficulty.CUSTOM -> Intent(ctx, CustomGameActivity::class.java).apply {
                    putExtra(ExtraUtils.NB_COLS, nbCols)
                    putExtra(ExtraUtils.NB_ROWS, nbRows)
                }

                Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD ->
                    Intent(ctx, GameActivity::class.java).apply {
                        putExtras(
                            nbRows,
                            nbCols,
                            d.nbBombs(nbRows, nbCols),
                            cellSize
                        )
                    }
            })
        }

        for (d in Difficulty.values())
            LevelBtn(d, ::startGame)
    }
}

@Composable
fun LevelBtn(
    difficulty: Difficulty,
    onClick: (Difficulty) -> Unit
) = Column {
    Button(onClick = { onClick(difficulty) }) {
        Text(text = difficulty.name)
    }

    if (difficulty == Difficulty.CUSTOM)
        return

    val ctx = LocalContext.current

    var hightscore: Duration? by rememberMutableState(value = null)

    LaunchedEffect(key1 = Unit) {
        val tmp: Duration = withContext(Dispatchers.IO) {
            val data = AppDatabase.getAppDataBase(ctx).gameDataDAO()
                .getBestTimeForDifficulty(difficulty.id) ?: return@withContext (-1).seconds

            data.time.seconds
        }

        withContext(Dispatchers.Main) {
            hightscore = tmp
        }
    }

    Text(text = when (hightscore) {
        (-1).seconds -> stringResource(id = R.string.no_highscore_yet)
        null -> "Loading..."
        else -> hightscore.toString()
    })
}