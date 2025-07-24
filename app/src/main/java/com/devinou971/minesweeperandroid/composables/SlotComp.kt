package com.devinou971.minesweeperandroid.composables

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.states.SlotState

private const val TAG = "SlotComp"

sealed interface SlotComp<T : SlotState> {
    data object Null : SlotComp<SlotState.Null> {
        @Composable
        override operator fun invoke(state: SlotState.Null, size: Int) = Unit
    }

    data object Bomb : SlotComp<SlotState.Bomb> {
        @Composable
        override operator fun invoke(state: SlotState.Bomb, size: Int) {
            Mi(
                cellSize = size,
                icon = Settings.theme[R.drawable.bombicon],
                desc = "BOOM"
            )
        }
    }

    data object Number : SlotComp<SlotState.Number> {
        @Composable
        override operator fun invoke(state: SlotState.Number, size: Int) = Box(
            contentAlignment = Alignment.Center
        ) {
            if (state.nbBombs == 0) // We don't display when there are no bombs
                return

            Text(
                modifier = Modifier,
                text = state.nbBombs.toString(),
                color = Settings.newColors[state.nbBombs - 1],
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    companion object {
        @Composable
        operator fun <T : SlotState> invoke(
            state: T,
            size: Int,
            onClick: () -> Unit
        ) {
            Log.i(TAG, "Rendering ${state.position}: f=${state.flagged} ; r=${state.revealed}")

            when {
                state.flagged -> Mi(
                    cellSize = size,
                    icon = Settings.theme[R.drawable.flagicon],
                    desc = "Flagged tile",
                    onClick = onClick
                )

                !state.revealed -> Icon(
                    modifier = Modifier
                        .size(size.dp)
                        .clickable(onClick = onClick),
                    painter = painterResource(id = Settings.theme[R.drawable.emptytile]),
                    contentDescription = "Hidden tile"
                )

                else -> (when (state) {
                    is SlotState.Null -> Null
                    is SlotState.Bomb -> Bomb
                    is SlotState.Number -> Number
                } as SlotComp<T>)(state, size)
            }
        }

        @Composable
        fun Mi(
            cellSize: Int,
            @DrawableRes icon: Int,
            desc: String,
            onClick: (() -> Unit)? = null
        ) = Image(
            modifier = Modifier
                .size(cellSize.dp)
                .clickable(onClick = { onClick?.invoke() }),
            painter = painterResource(id = icon),
            contentDescription = desc
        )
    }

    @Composable
    operator fun invoke(state: T, size: Int)
}