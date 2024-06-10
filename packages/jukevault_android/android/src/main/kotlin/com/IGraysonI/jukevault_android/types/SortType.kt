package com.igraysoni.jukevault_android.types

import android.provider.MediaStore

fun checkAlbumSortType(
    sortType: Int?,
    order: Int,
    ignoreCase: Boolean,
): String {
    // TODO: review this function.
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    }  else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Albums.ALBUM + orderAndCase
        1 -> MediaStore.Audio.Albums.ARTIST + orderAndCase
        2 -> MediaStore.Audio.Albums.NUMBER_OF_SONGS + orderAndCase
        else -> MediaStore.Audio.Albums.DEFAULT_SORT_ORDER + orderAndCase
    }
}

fun checkArtistSortType(
    sortType: Int?,
    order: Int,
    ignoreCase: Boolean,
): String {
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    }  else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Artists.ARTIST + orderAndCase
        1 -> MediaStore.Audio.Artists.NUMBER_OF_ALBUMS + orderAndCase
        2 -> MediaStore.Audio.Artists.NUMBER_OF_TRACKS + orderAndCase
        else -> MediaStore.Audio.Artists.DEFAULT_SORT_ORDER + orderAndCase
    }
}

fun checkAudioSortType(
    sortType: Int?,
    order: Int,
    ignoreCase: Boolean,
): String {
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    }  else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Media.TITLE + orderAndCase
        1 -> MediaStore.Audio.Media.ARTIST + orderAndCase
        2 -> MediaStore.Audio.Media.ALBUM + orderAndCase
        3 -> MediaStore.Audio.Media.DURATION + orderAndCase
        4 -> MediaStore.Audio.Media.DATE_ADDED + orderAndCase
        5 -> MediaStore.Audio.Media.SIZE + orderAndCase
        6 -> MediaStore.Audio.Media.DISPLAY_NAME + orderAndCase
        7 -> MediaStore.Audio.Media.TRACK + orderAndCase
        else -> MediaStore.Audio.Media.DEFAULT_SORT_ORDER + orderAndCase
    }
}

fun checkGenreSortType(
    sortType: Int?,
    order: Int,
    ignoreCase: Boolean,
): String {
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    }  else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Genres.NAME + orderAndCase
        else -> MediaStore.Audio.Genres.DEFAULT_SORT_ORDER + orderAndCase
    }
}

// TODO: review this function.
@Suppress("DEPRECATION")
fun checkPlaylistSortType(
    sortType: Int?,
    order: Int,
    ignoreCase: Boolean,
): String {
    val orderAndCase: String = if (ignoreCase) {
        if (order == 0) " COLLATE NOCASE ASC" else " COLLATE NOCASE DESC"
    }  else {
        if (order == 0) " ASC" else " DESC"
    }
    return when (sortType) {
        0 -> MediaStore.Audio.Playlists.NAME + orderAndCase
        1 -> MediaStore.Audio.Playlists.DATE_ADDED + orderAndCase
        else -> MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER + orderAndCase
    }
}