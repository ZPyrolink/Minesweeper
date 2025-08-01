package com.devinou971.minesweeperandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.devinou971.minesweeperandroid.ui.theme.colors.HtmlColorScheme
import com.devinou971.minesweeperandroid.ui.theme.colors.SlotColorScheme
import com.devinou971.minesweeperandroid.ui.theme.colors.darkColorScheme
import com.devinou971.minesweeperandroid.ui.theme.colors.lightColorScheme

val MaterialTheme.htmlColorScheme: HtmlColorScheme
    @Composable
    get() = HtmlColorScheme.Local.current

val MaterialTheme.slotColorScheme
    @Composable
    get() = SlotColorScheme.Local.current

@Composable
fun MinesweeperAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val ctx = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(ctx)
            else dynamicLightColorScheme(ctx)

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val view = LocalView.current
    val editMode = view.isInEditMode
    if (!editMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    val htmlColorScheme = HtmlColorScheme.run {
        if (darkTheme) dark
        else light
    }

    val slotColorScheme = SlotColorScheme.Default(darkTheme)

    CompositionLocalProvider(
        HtmlColorScheme.Local provides htmlColorScheme,
        SlotColorScheme.Local provides slotColorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}