package com.devinou971.minesweeperandroid.serializer

import androidx.datastore.core.Serializer
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.utils.decodeEnumElement
import com.devinou971.minesweeperandroid.utils.decodeListElements
import com.devinou971.minesweeperandroid.utils.encodeEnumElement
import com.devinou971.minesweeperandroid.utils.encodeListElements
import com.devinou971.minesweeperandroid.utils.encodeStructure
import com.devinou971.minesweeperandroid.utils.foo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : KSerializer<Settings>, Serializer<Settings> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Settings") {
        element<Settings.Theme>("theme")
        element("colors", ListSerializer(ColorSerializer).descriptor)
    }

    override fun serialize(
        encoder: Encoder,
        value: Settings
    ) = encoder.encodeStructure(descriptor) {
        encodeEnumElement(descriptor, 0, value.theme)
        encodeListElements(descriptor, 1, ColorSerializer, value.colors)
    }

    override fun deserialize(decoder: Decoder): Settings {
        var theme = defaultValue.theme
        var colors = defaultValue.colors
        decoder.foo(descriptor) {
            when (it) {
                0 -> theme = decodeEnumElement<Settings.Theme>(descriptor, 0)
                1 -> colors = decodeListElements(descriptor, 1, ColorSerializer)
                else -> throw SerializationException("Unknown index $it")
            }
        }
        
        return Settings.DTO(colors, theme)
    }

    override val defaultValue: Settings =
        Settings.DTO(emptyList(), Settings.Theme.DEFAULT)

    override suspend fun readFrom(input: InputStream): Settings =
        Json.decodeFromStream(this, input)

    override suspend fun writeTo(t: Settings, output: OutputStream) =
        Json.encodeToStream(this, t, output)
}