package com.devinou971.minesweeperandroid.wrappers

interface Wrapper<TOrigin, TWrap> {
    fun from(t: TWrap): TOrigin
    fun to(t: TOrigin): TWrap
}

interface StringWrapper<T> {
    val stringWrapper: Wrapper<T, String>
}

interface IntWrapper<T> {
    val intWrapper: Wrapper<T, Int>
}