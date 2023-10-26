package com.devinou971.minesweeperandroid.composables

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.devinou971.minesweeperandroid.GameActivity
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.utils.putExtras
import com.devinou971.minesweeperandroid.utils.rememberMutableState

@Composable
fun CustomGameComp(
    nbCols: Int,
    nbRows: Int
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text(
        text = stringResource(id = R.string.custom_game_mode),
        fontSize = 34.sp
    )

    var mNbCols by rememberMutableState(value = nbCols.toFloat())
    var mNbRows by rememberMutableState(value = nbRows.toFloat())
    var mNbBombs by rememberMutableState(value = 30f)

    LabeledSlider(
        title = stringResource(id = R.string.number_of_rows_string),
        value = mNbRows,
        range = 5f..nbRows.toFloat()
    ) { mNbRows = it }

    LabeledSlider(
        title = stringResource(id = R.string.number_of_cols_string),
        value = mNbCols,
        range = 5f..nbCols.toFloat(),
    ) { mNbCols = it }

    LabeledSlider(
        title = stringResource(id = R.string.amount_of_bombs_in_percent_string),
        value = mNbBombs,
        range = 12f..80f
    ) { mNbBombs = it }

    val ctx = LocalContext.current
    val view = LocalView.current

    Button(onClick = {
        val availableHeight = (view.height * 0.80).toInt()
        val availableWidth = view.width

        val cellSize = if (availableWidth / nbCols <= availableHeight / nbRows)
            availableWidth / nbCols else
            availableHeight / nbRows

        ctx.startActivity(Intent(ctx, GameActivity::class.java).apply {
            putExtras(mNbBombs.toInt(), mNbCols.toInt(), mNbRows.toInt(), cellSize)
        })
    }) {
        Text(text = stringResource(id = R.string.start_game))
    }
}

@Composable
fun LabeledSlider(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = title,
        fontSize = 20.sp
    )

    var tmpValue by rememberMutableState(value = value)

    Slider(
        modifier = Modifier.fillMaxWidth(.5f),
        value = tmpValue,
        onValueChange = { tmpValue = it },
        onValueChangeFinished = { onValueChanged(tmpValue) },
        valueRange = range,
        steps = (range.endInclusive - range.start).toInt()
    )
}

@Preview
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) {
    Surface {
        CustomGameComp(nbCols = 50, nbRows = 20)
    }
}