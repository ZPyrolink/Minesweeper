package com.devinou971.minesweeperandroid.composables

import android.graphics.Point
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.states.SlotState
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme
import com.devinou971.minesweeperandroid.ui.theme.slotColorScheme

private const val TAG = "SlotComp"

sealed interface SlotComp<T : SlotState> {
    @Composable
    operator fun invoke(state: T, size: Int)

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
                return@Box

            Text(
                modifier = Modifier
                    .size(size.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = state.nbBombs.toString(),
                color = MaterialTheme.slotColorScheme[state.nbBombs - 1],
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
            val slotState = state.state

            if (slotState == SlotState.State.Flagged) {
                ImageOnTile(
                    cellSize = size,
                    icon = Settings.theme[R.drawable.flagicon],
                    desc = "Flagged tile",
                    onClick = onClick
                )
                return
            }

            Box {
                val sizeAnim by animateDpAsState(
                    targetValue = if (slotState == SlotState.State.Revealed) 0.dp else size.dp
                )

                val animating by remember { derivedStateOf { sizeAnim.value != 0f } }

                if (slotState == SlotState.State.Hidded || slotState == SlotState.State.Revealed && animating) Icon(
                    modifier = Modifier
                        .size(sizeAnim)
                        .align(Alignment.Center)
                        .clickable(onClick = onClick)
                        .zIndex(1f),
                    painter = painterResource(id = Settings.theme[R.drawable.emptytile]),
                    contentDescription = "Hidden tile"
                )

                if (slotState == SlotState.State.Revealed)
                    (when (state) {
                        is SlotState.Bomb -> Bomb
                        is SlotState.Number -> Number
                        else -> throw IllegalStateException("Trying to render ${state::class.simpleName}")
                    } as SlotComp<T>)(state, size)
            }
        }

        @Composable
        fun ImageOnTile(
            modifier: Modifier = Modifier,
            cellSize: Int,
            @DrawableRes icon: Int,
            desc: String,
            onClick: (() -> Unit)? = null
        ) = Box(
            modifier = modifier
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
}

@PreviewLightDark
@Composable
private fun NumbersPrev() = MinesweeperAndroidTheme {
    Surface {
        Column {
            for (i in 1..9)
                SlotComp(
                    state = SlotState.Number(Point(), i)
                        .apply { reveal() },
                    size = 64
                ) {}
        }
    }
}

@PreviewLightDark
@Composable
private fun AnimPrev() = MinesweeperAndroidTheme {
    Surface {
        Column {
            for (i in 1..9)
                SlotComp(
                    state = SlotState.Number(Point(), i),
                    size = 64
                ) {}
        }
    }
}