package com.devinou971.minesweeperandroid.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize

@Composable
fun <T> rememberMutableState(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
) = remember { mutableStateOf(value, policy) }

val LocalDpSize: DpSize
    @Composable
    get() {
        val size = LocalWindowInfo.current.containerSize
        val density = LocalDensity.current

        return with(density) {
            DpSize(
                size.width.toDp(),
                size.height.toDp()
            )
        }
    }