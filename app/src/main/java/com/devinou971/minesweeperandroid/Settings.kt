package com.devinou971.minesweeperandroid

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.core.content.res.use

object Settings {
    lateinit var colors: IntArray
        private set

    fun init(context: Context) {
        val settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        if (settings.contains("init"))
            get(settings)
        else
            reset(context)
    }

    public fun reset(context: Context) {
        context.resources.obtainTypedArray(R.array.default_number_colors).use {
            colors = IntArray(it.length()) {i -> it.getColor(i, 0)}
        }
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