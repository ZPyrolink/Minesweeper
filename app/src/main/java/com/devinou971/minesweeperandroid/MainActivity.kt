package com.devinou971.minesweeperandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.devinou971.minesweeperandroid.navigation.Screen
import com.devinou971.minesweeperandroid.ui.theme.MinesweeperAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinesweeperAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val ctrl = rememberNavController()

                    NavHost(
                        navController = ctrl,
                        graph = Screen.rememberNavGraph(ctrl)
                    )
                }
            }
        }
    }
}