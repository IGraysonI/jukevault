package com.igraysonl.jukevault.controllers

import android.content.Context
import com.igraysonl.jukevault.methods.queries.*
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class QueryController(
    private val context: Context,
    private val call: MethodCall,
    private val result: MethodChannel.Result
) {
    fun call() {
        when (call.method) {
            // Query methods
            "queryAudios" -> AudiosQuery().init(context, result, call)
            "queryAlbums" -> AlbumsQuery().init(context, result, call)
            "queryArtists" -> ArtistsQuery().init(context, result, call)
            "queryPlaylists" -> PlaylistsQuery().init(context, result, call)
            "queryGenres" -> GenresQuery().init(context, result, call)
            "queryArtwork" -> ArtworkQuery().queryArtwork(context, result, call)
            // Playlists methods
            "createPlaylist" -> PlaylistController().createPlaylist(context, result, call)
            "deletePlaylist" -> PlaylistController().deletePlaylist(context, result, call)
            "addToPlaylist" -> PlaylistController().addToPlaylist(context, result, call)
            "removeFromPlaylist" -> PlaylistController().removeFromPlaylist(
                context,
                result,
                call
            )
            "renamePlaylist" -> PlaylistController().renamePlaylist(context, result, call)
            "moveItemTo" -> PlaylistController().moveItemTo(context, result, call)
            // Called if the requested method doesn't exist.
            else -> result.notImplemented()
        }
    }
}