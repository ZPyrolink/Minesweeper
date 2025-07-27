package com.devinou971.minesweeperandroid.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
            ImageOnTile(
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
                modifier = Modifier
                    .size(size.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = state.nbBombs.toString(),
                color = Settings.colors[state.nbBombs - 1],
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
            when {
                state.flagged -> ImageOnTile(
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
        fun ImageOnTile(
            cellSize: Int,
            @DrawableRes icon: Int,
            desc: String,
            onClick: (() -> Unit)? = null
        ) = Box(
            modifier = Modifier
                .size(cellSize.dp)
                .clickable(onClick = { onClick?.invoke() }),
        ) {
            Image(
                painter = painterResource(R.drawable.emptytile),
                contentDescription = "Empty tile"
            )

            Image(

                painter = painterResource(id = icon),
                contentDescription = desc
            )
        }
    }

    @Composable
    operator fun invoke(state: T, size: Int)
}