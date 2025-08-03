package com.devinou971.minesweeperandroid.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

//region Encoder

fun Encoder.encodeStructure(
    descriptor: SerialDescriptor,
    encode: CompositeEncoder.() -> Unit
) {
    val composite = beginStructure(descriptor)
    composite.encode()
    composite.endStructure(descriptor)
}

fun <T> Encoder.encodeList(
    descriptor: SerialDescriptor,
    list: List<T>,
    serializer: KSerializer<T>
) {
    val composite = beginCollection(descriptor, list.size)
    for ((index, color) in list.withIndex())
        composite.encodeSerializableElement(descriptor, index, serializer, color)
    composite.endStructure(descriptor)
}

fun <T : Enum<T>> CompositeEncoder.encodeEnumElement(
    descriptor: SerialDescriptor,
    index: Int,
    value: T,
) = encodeIntElement(descriptor, index, value.ordinal)

fun <T> CompositeEncoder.encodeListElements(
    descriptor: SerialDescriptor,
    index: Int,
    serializer: KSerializer<T>,
    value: List<T>
) = encodeSerializableElement(descriptor, index, ListSerializer(serializer), value)

//endregion
//region Decoder

fun Decoder.foo(
    descriptor: SerialDescriptor,
    decode: CompositeDecoder.(Int) -> Unit
) {
    val decoder = beginStructure(descriptor)

    var i = 0
    while (decoder.decodeElementIndex(descriptor).also { i = it } != CompositeDecoder.DECODE_DONE) {
        decoder.decode(i)
    }

    decoder.endStructure(descriptor)
}

inline fun <reified T : Enum<T>> CompositeDecoder.decodeEnumElement(
    descriptor: SerialDescriptor,
    index: Int,
): T = enumValues<T>()[decodeIntElement(descriptor, index)]

fun <T> CompositeDecoder.decodeListElements(
    descriptor: SerialDescriptor,
    index: Int,
    serializer: KSerializer<T>
): List<T> = decodeSerializableElement(descriptor, index, ListSerializer(serializer))

//endregion