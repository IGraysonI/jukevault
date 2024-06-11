package com.igraysoni.jukevault_android.controller

import android.content.Context
import com.igraysoni.jukevault_android.queries.AudiosQuery
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class QueryController(
    private val context: Context,
    private val call: MethodCall,
    private val result: MethodChannel.Result,
) {
    fun call() {
        when (call.method) {
            // Query methods
            "queryArtworks" -> AudiosQuery().init(context, result, call)
            "queryAlbums" -> AudiosQuery().init(context, result, call)
            "queryArtists" -> AudiosQuery().init(context, result, call)
            "queryAudios" -> AudiosQuery().init(context, result, call)
            "queryGenres" -> AudiosQuery().init(context, result, call)
            "queryPlaylists" -> AudiosQuery().init(context, result, call)

            // Playlist methods
            "createPlaylist" -> PlaylistController().createPlaylist(context, result, call)
            "removePlaylist" -> PlaylistController().removePlaylist(context, result, call)
            "addToPlaylist" -> PlaylistController().addToPlaylist(context, result, call)
            "removeFromPlaylist" -> PlaylistController().removeFromPlaylist(context, result, call)
            "renamePlaylist" -> PlaylistController().renamePlaylist(context, result, call)
            "moveItemTo" -> PlaylistController().moveItemTo(context, result, call)

            else -> result.notImplemented()
        }
    }
}