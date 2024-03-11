package com.example.jukevault_android.types

import android.net.Uri
import android.provider.MediaStore
import com.example.jukevault_android.utils.artistProjection
import com.example.jukevault_android.utils.genreProjection
import com.example.jukevault_android.utils.playlistProjection
import com.example.jukevault_android.utils.songProjection

fun checkWithFiltersType(sortType: Int): Uri {
    return when (sortType) {
        0 -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        1 -> MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        2 -> MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        3 -> MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
        4 -> MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        else -> throw Exception("[checkWithFiltersType] value not found.")
    }
}

fun checkProjection(withType: Uri): Array<String>? {
    return when (withType) {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI -> songProjection()
        // [Album] projection is null because we need all items.
        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI -> null
        MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI -> playlistProjection
        MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI -> artistProjection
        MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI -> genreProjection
        else -> songProjection()
    }
}

fun checkSongsArgs(args: Int): String {
    return when (args) {
        0 -> MediaStore.Audio.Media.TITLE + " like ?"
        1 -> MediaStore.Audio.Media.DISPLAY_NAME + " like ?"
        2 -> MediaStore.Audio.Media.ALBUM + " like ?"
        3 -> MediaStore.Audio.Media.ARTIST + " like ?"
        else -> throw Exception("[checkSongsArgs] value not found.")
    }
}

fun checkAlbumsArgs(args: Int): String {
    return when (args) {
        0 -> MediaStore.Audio.Albums.ALBUM + " like ?"
        1 -> MediaStore.Audio.Albums.ARTIST + " like ?"
        else -> throw Exception("[checkAlbumsArgs] value not found.")
    }
}

fun checkPlaylistsArgs(args: Int): String {
    return when (args) {
        0 -> MediaStore.Audio.Playlists.NAME + " like ?"
        else -> throw Exception("[checkPlaylistsArgs] value not found.")
    }
}

fun checkArtistsArgs(args: Int): String {
    return when (args) {
        0 -> MediaStore.Audio.Artists.ARTIST + " like ?"
        else -> throw Exception("[checkArtistsArgs] value not found.")
    }
}

fun checkGenresArgs(args: Int): String {
    return when (args) {
        0 -> MediaStore.Audio.Genres.NAME + " like ?"
        else -> throw Exception("[checkGenresArgs] value not found.")
    }
}