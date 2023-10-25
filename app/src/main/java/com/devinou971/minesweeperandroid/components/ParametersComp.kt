package com.devinou971.minesweeperandroid.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.composables.utils.RgbColorPicker
import com.devinou971.minesweeperandroid.extensions.ToastExt
import com.devinou971.minesweeperandroid.extensions.toColorString
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.utils.rememberMutableState

@Composable
fun ParametersComp() = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 25.dp, vertical = 10.dp)
) {
    Text(
        text = stringResource(id = R.string.colors),
        textAlign = TextAlign.Center,
        fontSize = 25.sp
    )

    ColorList(colors = Settings.newColors)

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = stringResource(id = R.string.theme_title),
        textAlign = TextAlign.Center,
        fontSize = 25.sp
    )

    ThemeList(themes = Settings.Theme.values().asList())

    Spacer(modifier = Modifier.weight(1f))

    val ctx = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = {
            Settings.reset(ctx)
            Settings.save(ctx)
            ToastExt.showText(ctx, R.string.settings_cleared, Toast.LENGTH_SHORT)
        }) {
            Text(text = stringResource(id = R.string.clear_settings))
        }

        Button(onClick = {
            AppDatabase.getAppDataBase(ctx).clearAllTables()
            ToastExt.showText(ctx, "Data cleared!", Toast.LENGTH_SHORT)
        }) {
            Text(text = stringResource(id = R.string.clear_data))
        }
    }
}

@Composable
fun ColorList(colors: List<Color>) = LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    itemsIndexed(colors) { i, it -> ColorItem(it, i) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorItem(
    color: Color,
    index: Int
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val ctx = LocalContext.current

        var colorPicker by rememberMutableState(value = false)
        var tmpColor by rememberMutableState(value = color)
        var currentColor by rememberMutableState(value = tmpColor)

        fun updateColor() {
            currentColor = tmpColor
            Settings.newColors[index] = currentColor
            Settings.save(ctx)
        }

        if (colorPicker) {
            fun close() {
                colorPicker = false
            }

            AlertDialog(
                title = { Text(text = "Color picker") },
                confirmButton = {
                    Button(onClick = {
                        updateColor()
                        close()
                    }) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                },
                dismissButton = {
                    Button(onClick = ::close) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                },
                onDismissRequest = ::close,
                text = {
                    RgbColorPicker(color = currentColor, onColorChanged = {
                        tmpColor = it
                    })
                }
            )
        }

        Text(text = "${index + 1}")
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier
            .fillMaxHeight(.5f)
            .aspectRatio(1f)
            .background(color)
            .clickable { colorPicker = true })

        var error by rememberMutableState(value = false)
        TextField(
            modifier = Modifier.fillMaxWidth(2 / 3f),
            value = color.toArgb().toColorString(),
            leadingIcon = { Text(text = "#", fontFamily = FontFamily.Default) },
            onValueChange = {
                error = (it.length == 3 || it.length == 6) && it.all { c ->
                    c.isDigit() || c.uppercaseChar() in 'A'..'F'
                }

                if (error)
                    return@TextField

                val str = "#" +
                        if (it.length == 6) it
                        else "${it[0]}${it[0]}${it[1]}${it[1]}${it[2]}${it[2]}"

                tmpColor = Color(str.toColorInt())
                updateColor()
            },
            singleLine = true,
            isError = error,
            placeholder = { Text(text = stringResource(id = R.string.rgb_hint)) }
        )
    }
}

val themeIconSize = 35.dp

@Composable
fun ThemeList(themes: List<Settings.Theme>) = LazyVerticalGrid(
    columns = GridCells.Adaptive(themeIconSize),
    verticalArrangement = Arrangement.spacedBy(5.dp),
    horizontalArrangement = Arrangement.spacedBy(5.dp)
) {
    items(themes) {
        val ctx = LocalContext.current

        ThemeItem(
            theme = it,
            selected = it == Settings.theme,
            onSelect = { t ->
                Settings.theme = t
                Settings.save(ctx)
            }
        )
    }
}

@Composable
fun ThemeItem(
    theme: Settings.Theme,
    selected: Boolean,
    onSelect: (Settings.Theme) -> Unit
) = Box(modifier = Modifier.size(themeIconSize)) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = if (selected) R.drawable.tile else R.drawable.emptytile),
        contentDescription = null
    )

    Image(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onSelect(theme) }
            .apply {
                if (selected)
                    background(colorResource(id = R.color.gainsboro))
            },
        painter = painterResource(id = theme.icon),
        contentDescription = "${theme.name} theme"
    )
}

@Preview
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) { Surface { ParametersComp() } }