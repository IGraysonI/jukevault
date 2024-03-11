package com.example.jukevault_android.utils

import android.os.Build
import android.provider.MediaStore

// Query songs projection.
fun songProjection(): Array<String> {
    val tempProjection = arrayListOf(
        MediaStore.Audio.Media.DATA, // TODO: Depricated
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ARTIST,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.BOOKMARK,
        MediaStore.Audio.Media.COMPOSER,
        MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.DATE_MODIFIED,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.TRACK,
        MediaStore.Audio.Media.YEAR,
        MediaStore.Audio.Media.IS_ALARM,
        MediaStore.Audio.Media.IS_MUSIC,
        MediaStore.Audio.Media.IS_NOTIFICATION,
        MediaStore.Audio.Media.IS_PODCAST,
        MediaStore.Audio.Media.IS_RINGTONE,
    )
    if (Build.VERSION.SDK_INT >= 29) {
        tempProjection.add(MediaStore.Audio.Media.IS_AUDIOBOOK) // Only Api 29+
    }
    if (Build.VERSION.SDK_INT >= 30) {
        tempProjection.add(MediaStore.Audio.Media.GENRE) // Only Api 30+
        tempProjection.add(MediaStore.Audio.Media.GENRE_ID) // Only Api 30+
    }
    return tempProjection.toTypedArray()
}

// Query playlist projection.
// TODO: Depricated
val playlistProjection = arrayOf(
    MediaStore.Audio.Playlists.DATA,
    MediaStore.Audio.Playlists._ID,
    MediaStore.Audio.Playlists.DATE_ADDED,
    MediaStore.Audio.Playlists.DATE_MODIFIED,
    MediaStore.Audio.Playlists.NAME,
)

// Query artist projection.
val artistProjection = arrayOf(
    MediaStore.Audio.Artists._ID,
    MediaStore.Audio.Artists.ARTIST,
    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
    MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
)

// Query genre projection.
val genreProjection = arrayOf(
    MediaStore.Audio.Genres._ID,
    MediaStore.Audio.Genres.NAME,
)