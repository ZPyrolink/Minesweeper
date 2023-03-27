package com.devinou971.minesweeperandroid

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.DrawableRes
import androidx.core.content.edit
import androidx.core.content.res.use

private typealias D = R.drawable

object Settings {
    lateinit var colors: IntArray
        private set

    lateinit var theme: Theme

    fun init(context: Context) {
        val settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        if (settings.contains("init"))
            get(settings)
        else
            reset(context)
    }

    public fun reset(context: Context) {
        context.resources.run {
            obtainTypedArray(R.array.default_number_colors).use {
                colors = IntArray(it.length()) { i -> it.getColor(i, 0) }
            }
            theme = Theme.valueOf(getString(R.string.default_theme))
        }
    }

    private fun get(settings: SharedPreferences) = settings.run {
        colors = getString("colors", null)!!
            .split(",")
            .map { it.toInt() }
            .toIntArray()
        theme = Theme.valueOf(getString("theme", "DEFAULT")!!)
    }

    fun save(context: Context) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit(true) {
            putBoolean("init", true)
            putString("colors", colors.joinToString(","))
            putString("theme", theme.toString())
        }
    }

    enum class Theme(private val map: Map<Int, Int>?, @DrawableRes val icon: Int) {
        DEFAULT(null, D.bombicon),
        MINECRAFT(
            mapOf(
                D.bombicon to D.bombicon_minecraft,
                D.flagicon to D.flagicon_minecraft,
                D.pickaxeicon to D.pickaxeicon_minecraft,
                D.bombicon to D.bombicon_minecraft
            ), D.bombicon_minecraft
        );

        @DrawableRes
        operator fun get(resource: Int) = map?.get(resource) ?: resource
    }
}