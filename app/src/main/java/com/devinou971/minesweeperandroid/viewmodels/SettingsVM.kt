package com.devinou971.minesweeperandroid.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase

class SettingsVM : ViewModel() {
    private val _colors = Settings.colors.toMutableStateList()
    val colors get() = _colors.toList()

    fun changeColor(index: Int, newColor: Color) {
        _colors[index] = newColor
    }

    private val _theme = mutableStateOf(Settings.theme)
    var theme by _theme

    fun save(ctx: Context) {
        Settings.theme = theme
        Settings.colors = colors
        Settings.save(ctx)
    }

    fun reset() {
        Settings.reset()

        _colors.clear()
        for (c in Settings.colors)
            _colors.add(c)
        _theme.value = Settings.theme
    }

    fun clearData(ctx: Context) {
        AppDatabase.getAppDataBase(ctx).clearAllTables()
    }
}