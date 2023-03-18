package com.devinou971.minesweeperandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToMenu(@Suppress("UNUSED_PARAMETER") view: View) {
        startActivity(Intent(this, MenuActivity::class.java))
    }


}