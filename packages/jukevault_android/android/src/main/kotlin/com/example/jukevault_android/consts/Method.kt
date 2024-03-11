package com.example.jukevault_android.consts

object Method {
    // General methods
    const val PERMISSION_STATUS = "permissionStatus"
    const val PERMISSION_REQUEST = "permissionRequest"
    const val GET_DEVICE_INFO = "getDeviceInfo"
    const val SCAN = "scan"
    const val SET_LOG_CONFIG = "setLogConfig"

    // Query methods
    const val GET_SONGS = "getSongs"
    const val GET_ALBUMS = "getAlbums"
    const val GET_ARTISTS = "getArtists"
    const val GET_GENRES = "getGenres"
    const val GET_PLAYLISTS = "getPlaylists"
    const val GET_ARTWORK = "getArtwork"
    const val GET_SONGS_FROM = "queryAudiosFrom"
    const val GET_WITH_FILTERS = "queryWithFilters"
    const val GET_ALL_PATHS = "queryAllPath"
    const val GET_LYRICS = "getLyrics"

    // Playlist methods
    const val CREATE_PLAYLIST = "createPlaylist"
    const val ADD_TO_PLAYLIST = "addToPlaylist"
    const val REMOVE_FROM_PLAYLIST = "removeFromPlaylist"
    const val DELETE_PLAYLIST = "deletePlaylist"
    const val RENAME_PLAYLIST = "renamePlaylist"
    const val MOVE_ITEM_TO = "moveItemTo"
}