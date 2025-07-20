package com.devinou971.minesweeperandroid.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SerializableIntSizeAsLongSerializer : KSerializer<SerializableIntSize> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IntSize", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): SerializableIntSize =
        SerializableIntSize(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: SerializableIntSize) =
        encoder.encodeLong(value.packedValue)
}