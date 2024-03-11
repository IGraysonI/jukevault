package com.example.jukevault_android.types.sort_types

import android.provider.MediaStore

fun checkAlbumSortType(sortType: Int?, order: Int, ignoreCase: Boolean): String {
    // [ASC] = Ascending.
    // [DESC] = Descending.
    // TODO: Review this code later
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    } else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Albums.ALBUM + orderAndCase
        1 -> MediaStore.Audio.Albums.ARTIST + orderAndCase
        2 -> MediaStore.Audio.Albums.NUMBER_OF_SONGS + orderAndCase
        else -> MediaStore.Audio.Albums.DEFAULT_SORT_ORDER + orderAndCase
    }
}