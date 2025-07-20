package com.devinou971.minesweeperandroid.serializer

import androidx.compose.runtime.Stable
import androidx.compose.ui.util.packInts
import androidx.compose.ui.util.unpackInt1
import androidx.compose.ui.util.unpackInt2
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class SerializableIntSize(val packedValue: Long) {
    constructor(width: Int, height: Int) : this(packInts(width, height))

    @Stable
    inline val width: Int
        get() = unpackInt1(packedValue)

    @Stable
    inline val height: Int
        get() = unpackInt2(packedValue)
}

