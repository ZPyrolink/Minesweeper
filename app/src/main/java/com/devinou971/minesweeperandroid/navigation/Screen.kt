package com.devinou971.minesweeperandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.devinou971.minesweeperandroid.composables.MainComp
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize
import com.devinou971.minesweeperandroid.utils.Difficulty
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface Screen {
    @Serializable
    data object Title : Screen

    @Serializable
    data object DifficultyChooser : Screen

    @Serializable
    data class CustomGameSettings(
        val size: SerializableIntSize
    ) : Screen

    @Serializable
    data class Game(
        val size: SerializableIntSize,
        val cellSize: Int,
        val nbBombs: Int
    ) : Screen

    @Serializable
    data object Parameters : Screen

    companion object {
        @Composable
        fun rememberNavGraph(ctrl: NavController): NavGraph = remember {
            ctrl.createGraph(startDestination = Title) {
                composable<Title> { MainComp(ctrl) }
                composable<DifficultyChooser> { TODO() }
                composable<CustomGameSettings> { TODO() }
                composable<Game>(
                    typeMap = mapOf(
                        typeOf<Difficulty>() to NavType.EnumType(Difficulty::class.java),
                        typeOf<SerializableIntSize>() to CustomNavType.IntSizeT
                    )
                ) {
                    val arguments = it.toRoute<Game>()
                }
                composable<Parameters> { TODO() }
            }
        }
    }
}