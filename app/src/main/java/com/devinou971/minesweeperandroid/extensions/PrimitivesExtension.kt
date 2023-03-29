package com.devinou971.minesweeperandroid.extensions

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

fun Int.toColorString() = (this and 0xff000000.inv().toInt()).toString(Int.HEX_BASE)
    .padStart(6, '0')

val Int.Companion.HEX_BASE get() = 16

enum class CpxUnit(var value: Int) {
    PX(TypedValue.COMPLEX_UNIT_PX),
    SP(TypedValue.COMPLEX_UNIT_SP),
    DIP(TypedValue.COMPLEX_UNIT_DIP),
    PT(TypedValue.COMPLEX_UNIT_PT),
    IN(TypedValue.COMPLEX_UNIT_IN),
    MM(TypedValue.COMPLEX_UNIT_MM),
}

infix fun <T : Number> T.applyDim(unit: CpxUnit) =
    applyDim(unit, Resources.getSystem().displayMetrics)

fun <T : Number> T.applyDim(unit: CpxUnit, metrics: DisplayMetrics) =
    TypedValue.applyDimension(unit.value, this.toFloat(), metrics).toInt()

infix fun <T : Number> T.deriveDim(unit: CpxUnit) =
    deriveDim(unit, Resources.getSystem().displayMetrics)

fun <T : Number> T.deriveDim(unit: CpxUnit, metrics: DisplayMetrics) = when (unit) {
    CpxUnit.PX -> toInt();
    CpxUnit.DIP -> toInt() / metrics.density;
    CpxUnit.SP -> toInt() / metrics.scaledDensity;
    CpxUnit.PT -> toInt() / metrics.xdpi * (1.0f / 72);
    CpxUnit.IN -> toInt() / metrics.xdpi;
    CpxUnit.MM -> toInt() / metrics.xdpi * (1.0f / 25.4f);
}.toInt()