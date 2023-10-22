package com.devinou971.minesweeperandroid.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.utils.rememberMutableState

@Composable
fun RgbColorPicker(color: Color, onColorChanged: (Color) -> Unit) = Column {
    var r by rememberMutableState(value = color.red * 255)
    var g by rememberMutableState(value = color.green * 255)
    var b by rememberMutableState(value = color.blue * 255)
    fun color() = Color(r.toInt(), g.toInt(), b.toInt())

    Slider(
        value = r, onValueChange = {
            r = it
        }, valueRange = 0f..255f, steps = 0,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = Color(r / 255, 0f, 0f),
        ),
        onValueChangeFinished = { onColorChanged(color()) }
    )
    Slider(
        value = g, onValueChange = {
            g = it
        }, valueRange = 0f..255f, steps = 0,
        colors = SliderDefaults.colors(
            thumbColor = Color.Green,
            activeTrackColor = Color(0f, g / 255, 0f),
        ),
        onValueChangeFinished = { onColorChanged(color()) }
    )
    Slider(
        value = b, onValueChange = {
            b = it
            onColorChanged(color())
        }, valueRange = 0f..255f, steps = 0,
        colors = SliderDefaults.colors(
            thumbColor = Color.Blue,
            activeTrackColor = Color(0f, 0f, b / 255),
        ),
        onValueChangeFinished = { onColorChanged(color()) }
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Box(
            Modifier
                .size(25.dp)
                .background(color = color())
        ) {}
    }
}

@Preview
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) {
    RgbColorPicker(Color.Cyan) {}
}