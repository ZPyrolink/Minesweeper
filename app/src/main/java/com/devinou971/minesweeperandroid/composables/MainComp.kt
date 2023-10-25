package com.devinou971.minesweeperandroid.composables

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.devinou971.minesweeperandroid.MenuActivity
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme

@Composable
fun Main() = Column(
    Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text(
        text = stringResource(id = R.string.minesweeper),
        textAlign = TextAlign.Center,
        fontSize = 48.sp
    )

    val ctx = LocalContext.current

    Button(onClick = { startActivity(ctx, Intent(ctx, MenuActivity::class.java), null) }) {
        Text(
            text = stringResource(id = R.string.play),
            fontSize = 48.sp
        )
    }
}

@Preview
@Composable
private fun Preview() = MinesweeperAndroidTheme(true) { Surface { Main() } }