package com.devinou971.minesweeperandroid

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.devinou971.minesweeperandroid.serializer.SettingsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

private typealias D = R.drawable

private val Context.settingsDs: DataStore<Settings> by dataStore("Settings", SettingsSerializer)

@Serializable(with = SettingsSerializer::class)
sealed class Settings(
    var colors: List<Color>,
    var theme: Theme
) {
    @Serializable
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

    companion object Default : Settings(
        emptyList(),
        Theme.DEFAULT
    ) {
        fun init(ctx: Context) {
            val dto = readDataStore(ctx)

            theme = dto.theme
            colors = dto.colors
        }

        fun reset() {
            val dto = SettingsSerializer.defaultValue

            colors = dto.colors
            theme = dto.theme
        }

        fun readDataStore(ctx: Context): Settings = runBlocking {
            ctx.settingsDs.data.first()
        }

        fun save(ctx: Context) {
            runBlocking {
                ctx.settingsDs.updateData { Settings }
            }
        }

        override fun toString(): String {
            return "Settings(colors=$colors, theme=$theme)"
        }
    }

    class DTO(colors: List<Color>, theme: Theme) : Settings(colors, theme)
}