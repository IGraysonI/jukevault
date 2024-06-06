package com.igraysoni.jukevault_android.types

import android.net.Uri
import android.provider.MediaStore

fun checkAlbumUriType(uriType: Int): Uri = when (uriType) {
    0 -> MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
    1 -> MediaStore.Audio.Albums.INTERNAL_CONTENT_URI
    else -> throw Exception("[checkAlbumUriType] Invalid URI type.")
}

fun checkArtistUriType(uriType: Int): Uri = when (uriType) {
    0 -> MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
    1 -> MediaStore.Audio.Artists.INTERNAL_CONTENT_URI
    else -> throw Exception("[checkArtistUriType] Invalid URI type.")
}

fun checkAudioUriType(uriType: Int): Uri = when (uriType) {
    0 -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    1 -> MediaStore.Audio.Media.INTERNAL_CONTENT_URI
    else -> throw Exception("[checkAudioUriType] Invalid URI type.")
}

fun checkGenreUriType(uriType: Int): Uri = when (uriType) {
    0 -> MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
    1 -> MediaStore.Audio.Genres.INTERNAL_CONTENT_URI
    else -> throw Exception("[checkGenreUriType] Invalid URI type.")
}

// TODO: Solve deprecated method.
@Suppress("DEPRECATION")
fun checkPlaylistUriType(uriType: Int): Uri = when (uriType) {
    0 -> MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
    1 -> MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI
    else -> throw Exception("[checkPlaylistUriType] Invalid URI type.")
}