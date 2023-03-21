package com.devinou971.minesweeperandroid

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.content.edit

object Settings {
    lateinit var colors: IntArray
        private set

    fun init(context: Context) {
        val settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        if (settings.contains("init"))
            get(settings)
        else
            create()
    }

    private fun create() {
        colors = intArrayOf(
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.rgb(0, 0, 127),
            Color.rgb(127, 0, 0),
            Color.rgb(255, 192, 203),
            Color.MAGENTA,
            Color.CYAN,
            Color.YELLOW
        )
    }

    private fun get(settings: SharedPreferences) = settings.run {
        colors = getString("colors", null)!!
            .split(",")
            .map { it.toInt() }
            .toIntArray()
    }


    fun save(context: Context) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit(true) {
            putBoolean("init", true)
            putString("colors", colors.joinToString(","))
        }
    }
}