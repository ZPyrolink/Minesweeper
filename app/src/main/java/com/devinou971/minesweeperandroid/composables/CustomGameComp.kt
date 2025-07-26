package com.devinou971.minesweeperandroid.composables

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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.navigation.Screen
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.utils.Difficulty
import com.devinou971.minesweeperandroid.utils.LocalDpSize
import com.devinou971.minesweeperandroid.utils.rememberMutableState

@Composable
fun CustomGameComp(
    size: SerializableIntSize,
    navCtrl: NavController
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text(
        text = stringResource(id = R.string.custom_game_mode),
        fontSize = 34.sp
    )

    var nbCols by rememberMutableState(value = size.width.toFloat())
    var nbRows by rememberMutableState(value = size.height.toFloat())
    var bombPercentage by rememberMutableState(value = 30f)

    LabeledSlider(
        title = stringResource(id = R.string.number_of_rows_string),
        value = nbRows,
        range = 5f..nbRows
    ) { nbRows = it }

    LabeledSlider(
        title = stringResource(id = R.string.number_of_cols_string),
        value = nbCols,
        range = 5f..nbCols,
    ) { nbCols = it }

    LabeledSlider(
        title = stringResource(id = R.string.amount_of_bombs_in_percent_string),
        value = bombPercentage,
        range = 12f..80f
    ) { bombPercentage = it }

    val dpSize = LocalDpSize

    Button(onClick = {
        val availableHeight = (dpSize.height.value * 0.80).toInt()
        val availableWidth = dpSize.width.value

        val cellSize = if (availableWidth / nbCols <= availableHeight / nbRows)
            availableWidth / nbCols else
            availableHeight / nbRows

        val finalSize = SerializableIntSize(nbCols.toInt(), nbRows.toInt())
        navCtrl.navigate(
            Screen.Game(
                finalSize,
                cellSize.toInt(),
                Difficulty.nbBombs(finalSize.width, finalSize.height, bombPercentage),
                Difficulty.CUSTOM
            )
        )
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
        CustomGameComp(SerializableIntSize(50, 20), rememberNavController())
    }
}