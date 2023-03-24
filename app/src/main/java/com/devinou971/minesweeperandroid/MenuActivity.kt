package com.devinou971.minesweeperandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.utils.Difficulty
import com.devinou971.minesweeperandroid.utils.ExtraUtils
import com.devinou971.minesweeperandroid.utils.putExtra
import com.devinou971.minesweeperandroid.utils.putExtras
import java.util.concurrent.TimeUnit

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        for (buttonId in arrayOf(
            R.id.easyLevelButton, R.id.normalLevelButton,
            R.id.hardLevelButton, R.id.customLevelButon
        ))
            findViewById<Button>(buttonId).setOnClickListener { startGame(buttonId) }

        findViewById<ImageButton>(R.id.parameterButton).setOnClickListener { openSettings() }
    }

    override fun onResume() {
        super.onResume()

        val highscoreTexts = mapOf<Difficulty, TextView>(
            Pair(Difficulty.EASY, findViewById(R.id.bestScoreEasy)),
            Pair(Difficulty.NORMAL, findViewById(R.id.bestScoreNormal)),
            Pair(Difficulty.HARD, findViewById(R.id.bestScoreHard)),
        )

        Thread {
            for (difficulty in highscoreTexts.keys) {
                val bestScore = AppDatabase.getAppDataBase(this).gameDataDAO()
                    .getBestTimeForDifficulty(difficulty.id)

                val str = if (bestScore == null) {
                    getString(R.string.no_highscore_yet)
                } else {
                    val minutes = TimeUnit.SECONDS.toMinutes(bestScore.time.toLong()).toInt()
                    val seconds = bestScore.time % TimeUnit.MINUTES.toSeconds(1)

                    getString(R.string.highscore, minutes, seconds)
                }

                runOnUiThread { highscoreTexts[difficulty]!!.text = str }
            }
        }.start()
    }

    private fun openSettings() =
        startActivity(Intent(this, ParametersActivity::class.java))

    private fun startGame(viewId: Int) {
        val availableHeight = (window.decorView.height * 0.80).toInt()
        val availableWidth = window.decorView.width
        val nbCols = 10
        val cellSize = availableWidth / nbCols
        val nbRows = availableHeight / cellSize

        startActivity(when (viewId) {
            R.id.customLevelButon -> Intent(this, CustomGameActivity::class.java).apply {
                putExtra(ExtraUtils.NB_COLS, nbCols)
                putExtra(ExtraUtils.NB_ROWS, nbRows)
            }

            R.id.easyLevelButton, R.id.normalLevelButton, R.id.hardLevelButton -> {
                val difficulty = when (viewId) {
                    R.id.easyLevelButton -> Difficulty.EASY
                    R.id.normalLevelButton -> Difficulty.NORMAL
                    R.id.hardLevelButton -> Difficulty.HARD
                    else -> throw IllegalArgumentException()
                }

                Intent(this, GameActivity::class.java).apply {
                    putExtras(
                        nbRows,
                        nbCols,
                        difficulty.nbBombs(nbRows, nbCols),
                        cellSize,
                        difficulty
                    )
                }
            }

            else -> throw IllegalArgumentException()
        })
    }
}