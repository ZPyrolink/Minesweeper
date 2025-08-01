package com.devinou971.minesweeperandroid

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.devinou971.minesweeperandroid.ui.theme.colors.SlotColorScheme
import com.devinou971.minesweeperandroid.utils.get
import com.devinou971.minesweeperandroid.utils.getColorList
import com.devinou971.minesweeperandroid.utils.put
import com.devinou971.minesweeperandroid.utils.putColorList

private typealias D = R.drawable

private const val TAG = "Settings"

object Settings {
    var colors: List<Color> = emptyList()
    var theme: Theme = Defaults.theme

    private object Defaults {
        val theme: Theme = Theme.DEFAULT
    }

    fun init(context: Context) {
        val settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        if (settings.contains("init")) {
            get(settings)
        } else {
            save(context)
        }
    }

    public fun reset() {
        colors = SlotColorScheme.light.toList()
        theme = Defaults.theme
    }

    private fun get(settings: SharedPreferences) = settings.apply {
        getColorList(::colors, colors)
        Log.i(TAG, "get: Colors = $colors")
        get(::theme, Defaults.theme)
        Log.i(TAG, "get: Theme = $theme")
    }

    fun save(context: Context) {
        Log.i(TAG, "save: $this")
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit(true) {
            putBoolean("init", true)
            putColorList(::colors)
            put(::theme)
        }
    }

    override fun toString(): String {
        return "Settings(colors=$colors, theme=$theme)"
    }

    enum class Theme(private val map: Map<Int, Int>?, @DrawableRes val icon: Int) {
        DEFAULT(null, D.bombicon_new),
        MINECRAFT(
            mapOf(
                D.bombicon to D.bombicon_minecraft_new,
                D.flagicon to D.flagicon_minecraft_new,
                D.pickaxeicon to D.pickaxeicon_minecraft_new
            ), D.bombicon_minecraft_new
        ),
        PIXEL(
            mapOf(
                D.bombicon to D.bombicon_pixel_new,
                D.flagicon to D.flagicon_pixel_new,
                D.pickaxeicon to D.pickaxeicon_pixel_new
            ), D.pickaxeicon_pixel_new
        );

        @DrawableRes
        operator fun get(resource: Int) = map?.get(resource) ?: resource
    }
}