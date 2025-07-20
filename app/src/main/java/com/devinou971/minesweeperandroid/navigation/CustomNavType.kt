package com.devinou971.minesweeperandroid.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.devinou971.minesweeperandroid.serializer.SerializableIntSize

object CustomNavType {
    object IntSizeT : NavType<SerializableIntSize>(false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: SerializableIntSize
        ) = bundle.putLong(
            key,
            value.packedValue
        )

        override fun get(
            bundle: SavedState,
            key: String
        ): SerializableIntSize? = SerializableIntSize(bundle.getLong(key))

        override fun parseValue(value: String): SerializableIntSize {
            val (width, height) = value.split('x').map { it.toInt() }
            return SerializableIntSize(width, height)
        }

        override fun serializeAsValue(value: SerializableIntSize): String =
            "${value.width}x${value.height}"
    }
}