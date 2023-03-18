package com.devinou971.minesweeperandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.devinou971.minesweeperandroid.utils.ExtraUtils
import com.devinou971.minesweeperandroid.utils.getIntExtra
import com.devinou971.minesweeperandroid.utils.putExtras
import com.google.android.material.slider.Slider

class CustomGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_game)

        val colsSlider: Slider = findViewById(R.id.colsSlider)
        val rowsSlider: Slider = findViewById(R.id.rowsSlider)

        colsSlider.valueTo = intent.getIntExtra(ExtraUtils.NB_COLS, 10).toFloat()
        rowsSlider.valueTo = intent.getIntExtra(ExtraUtils.NB_ROWS, 10).toFloat()
        colsSlider.value = colsSlider.valueTo
        rowsSlider.value = rowsSlider.valueTo

        val gameButton = findViewById<Button>(R.id.startGameButton)
        gameButton.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        val colsSlider: Slider = findViewById(R.id.colsSlider)
        val rowsSlider: Slider = findViewById(R.id.rowsSlider)
        val bombsSlider: Slider = findViewById(R.id.bombsSlider)

        val nbCols = colsSlider.value.toInt()
        val nbRows = rowsSlider.value.toInt()
        val nbBombs = (nbRows * nbCols * (bombsSlider.value / 100)).toInt()

        val availableHeight = (window.decorView.height * 0.80).toInt()
        val availableWidth = window.decorView.width

        val cellSize = if (availableWidth / nbCols <= availableHeight / nbRows)
            availableWidth / nbCols else
            availableHeight / nbRows

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtras(nbBombs, nbCols, nbRows, cellSize)
        }
        startActivity(intent)
    }
}
