package com.devinou971.minesweeperandroid.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastExt {
    fun showText(context: Context, @StringRes resId: Int, duration: Int): Toast =
        Toast.makeText(context, resId, duration).apply { show() }

    fun showText(context: Context, text: CharSequence, duration: Int): Toast =
        Toast.makeText(context, text, duration).apply { show() }
}