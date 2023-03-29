package com.devinou971.minesweeperandroid

import android.app.Application

class Minesweeper : Application() {
    override fun onCreate() {
        super.onCreate()

        Settings.init(this)
    }
}