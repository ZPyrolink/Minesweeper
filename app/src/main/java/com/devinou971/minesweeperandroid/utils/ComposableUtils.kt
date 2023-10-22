package com.devinou971.minesweeperandroid.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy

@Composable
fun <T> rememberMutableState(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
) = remember { mutableStateOf(value, policy) }