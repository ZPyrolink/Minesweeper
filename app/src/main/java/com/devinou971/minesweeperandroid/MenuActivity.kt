package com.devinou971.minesweeperandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.storageclasses.GameDataDAO
import com.devinou971.minesweeperandroid.utils.Difficulty
import com.devinou971.minesweeperandroid.utils.ExtraUtils
import com.devinou971.minesweeperandroid.utils.putExtra
import com.devinou971.minesweeperandroid.utils.putExtras

class MenuActivity : AppCompatActivity() {

    private var appDatabase: AppDatabase? = null
    private var gameDataDAO: GameDataDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        for (buttonId in arrayOf(
            R.id.easyLevelButton, R.id.normalLevelButton,
            R.id.hardLevelButton, R.id.customLevelButon
        ))
            findViewById<Button>(buttonId).setOnClickListener { startGame(buttonId) }

        findViewById<ImageButton>(R.id.parameterButton).setOnClickListener { goToParameter() }


        val highscoreViews = mutableListOf<TextView>()
        highscoreViews.add(findViewById(R.id.bestScoreEasy))
        highscoreViews.add(findViewById(R.id.bestScoreNormal))
        highscoreViews.add(findViewById(R.id.bestScoreHard))

        Thread {
            accessDatabase()
            for (i in 0..highscoreViews.size) {
                val bestScore = gameDataDAO?.getBestTimeForDifficulty(i)
                if (bestScore != null) {
                    val minutes = bestScore.time / 60
                    val seconds = bestScore.time - minutes * 60

                    runOnUiThread {
                        highscoreViews[i].text =
                            resources.getString(R.string.highscore, minutes, seconds)
                    }
                }
            }

        }.start()
    }

    private fun goToParameter() {
        val intent = Intent(this, ParametersActivity::class.java).apply { }
        startActivity(intent)
    }

    private fun startGame(viewId: Int) {
        val availableHeight = (window.decorView.height * 0.80).toInt()
        val availableWidth = window.decorView.width
        val nbCols = 10
        val cellSize = availableWidth / nbCols
        val nbRows = availableHeight / cellSize
        when (viewId) {
            R.id.customLevelButon -> {
                val intent = Intent(this, CustomGameActivity::class.java).apply {
                    putExtra(ExtraUtils.NB_COLS, nbCols)
                    putExtra(ExtraUtils.NB_ROWS, nbRows)
                }
                startActivity(intent)
            }
            else -> {
                val difficulty = when (viewId) {
                    R.id.easyLevelButton -> Difficulty.EASY
                    R.id.normalLevelButton -> Difficulty.NORMAL
                    R.id.hardLevelButton -> Difficulty.HARD
                    else -> throw Exception()
                }

                startActivity(Intent(this, GameActivity::class.java).apply {
                    putExtras(
                        difficulty.nbBombs(nbRows, nbCols),
                        nbCols,
                        nbRows,
                        cellSize,
                        difficulty
                    )
                })
            }
        }
    }

    private fun accessDatabase() {
        appDatabase = AppDatabase.getAppDataBase(this)
        gameDataDAO = appDatabase?.gameDataDAO()
    }
}