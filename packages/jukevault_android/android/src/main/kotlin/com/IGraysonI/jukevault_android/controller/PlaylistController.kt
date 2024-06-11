// TODO: Deprecation fix.
@file:Suppress("DEPRECATION")

package com.igraysoni.jukevault_android.controller

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * PlaylistController
 * Controller for playlist related methods.
 */
class PlaylistController {
    // Main parameters.
    private val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
    private val contentValues = ContentValues()
    private val channelError = "jukevault"
    private val columns = arrayOf("count(*)")

    // Non-nullable parameters.
    private lateinit var resolver: ContentResolver

    private fun checkPlaylist(
        id: Int? = null,
        name: String? = null,
    ): Int? {
        val cursor = resolver.query(
            uri,
            arrayOf(MediaStore.Audio.Playlists.NAME, MediaStore.Audio.Playlists._ID),
            null,
            null,
            null
        )
        while (cursor != null && cursor.moveToNext()) {
            val playlistName = cursor.getString(0)
            val playlistId = cursor.getInt(1)
            if (playlistName == name || playlistId == id) return playlistId
        }
        cursor?.close()
        return null
    }

    fun createPlaylist(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
        this.resolver = context.contentResolver
        val playlistName = call.argument<String>("playlistName")!!
        contentValues.put(MediaStore.Audio.Playlists.NAME, playlistName)
        contentValues.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis())

        resolver.insert(uri, contentValues)
        val id: Int? = checkPlaylist(name = playlistName)
        result.success(id)
    }

    fun removePlaylist(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        if (checkPlaylist(playlistId) == -1) result.success(false)

        val deleteUri = ContentUris.withAppendedId(uri, playlistId.toLong())
        resolver.delete(deleteUri, null, null)
        result.success(true)
    }

    // TODO: Add option to use list
    // TODO: Fix error on Android 10
    fun addToPlaylist(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
       this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val audioId = call.argument<Int>("audioId")!!
        if (checkPlaylist(playlistId) == -1) result.success(false)

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId.toLong())

        // If Android >= 10/Q "count(*)" don't count.
        val columnsBasedOnVersion = if (Build.VERSION.SDK_INT < 29) columns else null
        val cursor = resolver.query(uri, columnsBasedOnVersion, null, null, null)
        var count = -1
        while (cursor != null && cursor.moveToNext()) {
            count += if (Build.VERSION.SDK_INT < 29) cursor.count else cursor.getInt(0)
        }
        cursor?.close()

        try {
            contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1)
            contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId.toLong())
            resolver.insert(uri, contentValues)
            result.success(true)
        } catch (e: Exception) {
            Log.e(channelError, e.toString())
            result.success(false)
        }
    }

    // TODO: Add option to use list
    fun removeFromPlaylist(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val audioId = call.argument<Int>("audioId")!!
        if (checkPlaylist(playlistId) == -1) result.success(false)

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId.toLong())
        val where = MediaStore.Audio.Playlists.Members._ID + "=?"
        val whereArgs = arrayOf(audioId.toString())

        try {
            resolver.delete(uri, where, whereArgs)
            result.success(true)
        } catch (e: Exception) {
            Log.e(channelError, e.toString())
            result.success(false)
        }
    }

    // TODO: test this method
    fun moveItemTo(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val from = call.argument<Int>("from")!!
        val to = call.argument<Int>("to")!!
        if (checkPlaylist(playlistId) == -1) result.success(false)

        MediaStore.Audio.Playlists.Members.moveItem(resolver, playlistId.toLong(), from, to)
        result.success(true)
    }

    fun renamePlaylist(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val newPlaylistName = call.argument<String>("newPlaylistName")!!
        if (checkPlaylist(playlistId) == -1) result.success(false)

        contentValues.put(MediaStore.Audio.Playlists.NAME, newPlaylistName)
        contentValues.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis())
        resolver.update(uri, contentValues, "_id=${playlistId.toLong()}", null)
        result.success(true)
    }

}