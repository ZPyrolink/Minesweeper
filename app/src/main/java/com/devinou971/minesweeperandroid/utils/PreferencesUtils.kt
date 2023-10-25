package com.devinou971.minesweeperandroid.utils

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import com.devinou971.minesweeperandroid.wrappers.ColorWrapper
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

fun <V, T> SharedPreferences.getNullable(
    ref: KMutableProperty0<V?>,
    ifNotNull: (String, T) -> V,
    defaultValue: T
): Unit = ref.set(if (contains(ref.name)) ifNotNull(ref.name, defaultValue) else null)

fun <T> SharedPreferences.get(ref: KMutableProperty0<T>, default: T): Unit =
    ref.set(get(ref.name, default))

@Suppress("UNCHECKED_CAST")
operator fun <T> SharedPreferences.get(
    key: String,
    default: T
): T {
    fun err(): Nothing = throw NotImplementedError("Cannot get current class")

    return (when (default) {
        is Boolean -> ::getBoolean
        is Float -> ::getFloat
        is Int -> ::getInt
        is Long -> ::getLong
        is String -> ::getString
        is ClosedFloatingPointRange<*> -> when (default.start) {
            is Float -> ::getFloatRange

            else -> err()
        }

        else -> err()
    } as (String, T) -> T)(key, default)
}

inline operator fun <reified T : Enum<T>> SharedPreferences.get(
    key: String,
    default: T
): T = getEnum(key, default)

inline fun <reified T : Enum<T>> SharedPreferences.getEnum(name: String, default: Enum<T>): T =
    enumValueOf(getString(name, default.name)!!)

fun SharedPreferences.getColor(name: String, default: Int): Color =
    ColorWrapper.intWrapper.from(getInt(name, default))

fun <T> SharedPreferences.getList(
    ref: KMutableProperty0<List<T>>,
    default: List<T>,
    transform: (String) -> T
): Unit = ref.set(getList(ref.name, default, transform))

fun <T> SharedPreferences.getMutableList(
    ref: KMutableProperty0<MutableList<T>>,
    default: MutableList<T>,
    transform: (String) -> T
): Unit = ref.set(getMutableList(ref.name, default, transform))

fun <T> SharedPreferences.getList(
    key: String,
    default: List<T>,
    transform: (String) -> T
): List<T> = getStringSet(key, null)?.map(transform) ?: default

fun <T> SharedPreferences.getMutableList(
    key: String,
    default: MutableList<T>,
    transform: (String) -> T
): MutableList<T> = getStringSet(key, null)?.map(transform)?.toMutableList() ?: default

fun SharedPreferences.getFloatRange(
    key: String,
    default: ClosedFloatingPointRange<Float>
): ClosedFloatingPointRange<Float> =
    getFloat("${key}Start", default.start)..getFloat("${key}End", default.endInclusive)

private typealias Editor = SharedPreferences.Editor

fun <T> Editor.removeOrPut(ref: KProperty0<T>): Editor = when (val value = ref.get()) {
    null -> remove(ref.name)
    else -> set(ref.name, value)
}

fun <T> Editor.put(ref: KProperty0<T>): Editor = set(ref.name, ref.get())

@Suppress("UNCHECKED_CAST")
operator fun <T> Editor.set(
    key: String,
    value: T
): Editor {
    fun err(): Nothing = throw NotImplementedError("Cannot save $value")

    return (when (value) {
        is Boolean -> ::putBoolean
        is Float -> ::putFloat
        is Int -> ::putInt
        is Long -> ::putLong
        is String -> ::putString
        is Color -> ::putColor
        is ClosedFloatingPointRange<*> -> when (value.start) {
            is Float -> ::putFloatRange

            else -> err()
        }

        is Enum<*> -> ::putEnum

        else -> err()
    } as (String, T) -> Editor)(key, value)
}

fun <T : Enum<T>> Editor.putEnum(key: String, value: Enum<T>): Editor = putString(key, value.name)

fun Editor.putColor(key: String, value: Color): Editor =
    putInt(key, ColorWrapper.intWrapper.to(value))

fun <T> Editor.putList(
    ref: KProperty0<List<T>>,
    transform: (T) -> String = { it.toString() }
): Editor = putList(ref.name, ref.get(), transform)

fun <T> Editor.putList(
    key: String,
    list: List<T>,
    transform: (T) -> String = { it.toString() }
): Editor = putStringSet(key, list.map(transform).toSet())

fun Editor.putFloatRange(
    key: String,
    value: ClosedFloatingPointRange<Float>
): Editor = apply {
    putFloat("${key}Start", value.start)
    putFloat("${key}End", value.endInclusive)
}