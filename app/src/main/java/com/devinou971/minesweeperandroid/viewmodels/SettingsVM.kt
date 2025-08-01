package com.devinou971.minesweeperandroid.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase

private const val TAG = "SettingsVM"

class SettingsVM(
    private val defaultColors: List<Color>
) : ViewModel() {
    private val _colors = Settings.colors.toMutableStateList()
    val colors
        get() = _colors.ifEmpty { defaultColors }.toList()

    fun changeColor(index: Int, newColor: Color) {
        if (_colors.isEmpty())
            _colors.addAll(defaultColors)

        _colors[index] = newColor
    }

    private val _theme = mutableStateOf(Settings.theme)
    var theme by _theme

    fun save(ctx: Context) {
        Log.i(TAG, "saving: $this")
        Settings.theme = theme
        Settings.colors = _colors.toList()
        Settings.save(ctx)
    }

    fun reset() {
        Settings.reset()

        _colors.clear()
        _colors.addAll(Settings.colors)
        _theme.value = Settings.theme
    }

    fun clearData(ctx: Context) {
        AppDatabase.getAppDataBase(ctx).clearAllTables()
    }

    override fun toString(): String {
        return "SettingsVM(colors=${_colors.toList()}, theme=$theme)"
    }
}