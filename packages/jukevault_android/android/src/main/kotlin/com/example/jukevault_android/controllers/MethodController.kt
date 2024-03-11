package com.example.jukevault_android.controllers

import com.example.jukevault_android.consts.Method
import com.example.jukevault_android.providers.PluginProvider
import com.example.jukevault_android.queries.AlbumQuery
import com.example.jukevault_android.queries.AllPathQuery
import com.example.jukevault_android.queries.ArtistQuery
import com.example.jukevault_android.queries.ArtworkQuery
import com.example.jukevault_android.queries.AudioFromQuery
import com.example.jukevault_android.queries.AudioQuery
import com.example.jukevault_android.queries.GenreQuery
import com.example.jukevault_android.queries.PlaylistQuery
import com.example.jukevault_android.queries.WithFiltersQuery


class MethodController {
    fun find() {
        when (PluginProvider.call().method) {
            // Query methods
            Method.GET_SONGS -> AudioQuery().querySongs()
            Method.GET_ALBUMS -> AlbumQuery().queryAlbums()
            Method.GET_ARTISTS -> ArtistQuery().queryArtists()
            Method.GET_PLAYLISTS -> PlaylistQuery().queryPlaylists()
            Method.GET_GENRES -> GenreQuery().queryGenres()
            Method.GET_ARTWORK -> ArtworkQuery().queryArtworks()
            Method.GET_SONGS_FROM -> AudioFromQuery().querySongsFrom()
            Method.GET_WITH_FILTERS -> WithFiltersQuery().queryWithFilters()
            Method.GET_ALL_PATHS -> AllPathQuery().queryAllPath()

            // Playlist methods
            Method.CREATE_PLAYLIST -> PlaylistController().createPlaylist()
            Method.DELETE_PLAYLIST -> PlaylistController().deletePlaylist()
            Method.ADD_TO_PLAYLIST -> PlaylistController().addToPlaylist()
            Method.REMOVE_FROM_PLAYLIST -> PlaylistController().removeFromPlaylist()
            Method.RENAME_PLAYLIST -> PlaylistController().renamePlaylist()
            Method.MOVE_ITEM_TO -> PlaylistController().moveItemTo()
            else -> PluginProvider.result().notImplemented()
        }
    }
}