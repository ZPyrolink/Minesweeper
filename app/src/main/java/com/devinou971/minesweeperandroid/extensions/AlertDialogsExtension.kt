package com.devinou971.minesweeperandroid.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View

object AlertDialogExt {
    fun Builder(
        context: Context, title: String, view: View,
        positiveButton: Pair<String, ((DialogInterface, Int) -> Unit)?>,
        negativeButton: Pair<String, ((DialogInterface, Int) -> Unit)?>
    ): AlertDialog.Builder =
        AlertDialog.Builder(context).apply {
            setTitle(title)
            setView(view)
            setPositiveButton(positiveButton.first, positiveButton.second)
            setNegativeButton(negativeButton.first, negativeButton.second)
        }

    fun create(
        context: Context, title: String, view: View,
        positiveButton: Pair<String, ((DialogInterface, Int) -> Unit)?>,
        negativeButton: Pair<String, ((DialogInterface, Int) -> Unit)?>
    ): AlertDialog = Builder(context, title, view, positiveButton, negativeButton).create()

    fun createAndShow(
        context: Context, title: String, view: View,
        positiveButton: Pair<String, ((DialogInterface, Int) -> Unit)?>,
        negativeButton: Pair<String, ((DialogInterface, Int) -> Unit)?>
    ): AlertDialog = create(context, title, view, positiveButton, negativeButton).apply { show() }
}