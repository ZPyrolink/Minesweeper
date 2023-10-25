package com.devinou971.minesweeperandroid

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.devinou971.minesweeperandroid.utils.get
import com.devinou971.minesweeperandroid.utils.put
import com.devinou971.minesweeperandroid.utils.putList
import com.devinou971.minesweeperandroid.wrappers.ColorWrapper

private typealias D = R.drawable

object Settings {
    @Deprecated("Use the color instead!")
    lateinit var colors: IntArray
    var newColors: SnapshotStateList<Color> = mutableStateListOf(*Defaults.colors.toTypedArray())
        private set

    var theme: Theme by mutableStateOf(Defaults.theme)

    private object Defaults {
        val colors: List<Color> = mutableListOf(
            Color.Blue,
            Color.Green,
            Color.Red,
            Color(0, 0, 0x7f),
            Color(0x7f, 0, 0),
            Color(0xff, 0xc0, 0xcb),
            Color.Magenta,
            Color.Cyan,
            Color.Yellow
        )
        val theme: Theme = Theme.DEFAULT
    }

    fun init(context: Context) {
        val settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        if (settings.contains("init"))
            get(settings)
        else
            reset(context)
    }

    public fun reset(context: Context) {
        newColors = mutableStateListOf(*Defaults.colors.toTypedArray())
        theme = Defaults.theme

        save(context)
    }

    private fun get(settings: SharedPreferences) = settings.apply {
//        getMutableList(::newColors, Defaults.colors, ColorWrapper.stringWrapper::from)
//        TODO
        get(::theme, Defaults.theme)
    }

    fun save(context: Context) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit(true) {
            putBoolean("init", true)
            putList(::newColors, ColorWrapper.stringWrapper::to)
            put(::theme)
        }
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