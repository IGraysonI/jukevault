package com.example.jukevault_android.types.sort_types

import android.provider.MediaStore

fun checkGenreSortType(sortType: Int?, order: Int, ignoreCase: Boolean): String {
    // [ASC} = Ascending.
    // [DESC] = Descending.
    // TODO: Review this code later
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    } else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Genres.NAME + orderAndCase
        else -> MediaStore.Audio.Genres.DEFAULT_SORT_ORDER + orderAndCase
    }
}