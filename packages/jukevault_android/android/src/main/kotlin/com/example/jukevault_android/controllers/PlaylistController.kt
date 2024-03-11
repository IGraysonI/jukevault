package com.example.jukevault_android.controllers

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.jukevault_android.providers.PluginProvider

class PlaylistController {
    private val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
    private val contentValues = ContentValues()
    private val channelError = "JukevaultAndroidError"
    private lateinit var resolver: ContentResolver

    private val columns = arrayOf(
        "count(*)"
    )
    private val context = PluginProvider.context()
    private val result = PluginProvider.result()
    private val call = PluginProvider.call()

    fun createPlaylist() {
        this.resolver = context.contentResolver
        val playlistName = call.argument<String>("playlistName")!!

        // For create we don't check if name already exists
        contentValues.put(MediaStore.Audio.Playlists.NAME, playlistName)
        contentValues.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis())
        resolver.insert(uri, contentValues)
        result.success(true)
    }

    fun removePlaylist() {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!

        // Check if Playlist exists based on ID
        if (!checkPlaylistId(playlistId)) result.success(false)
        else {
            val delUri = ContentUris.withAppendedId(uri, playlistId.toLong())
            resolver.delete(delUri, null, null)
            result.success(true)
        }
    }


    // TODO: Add option to use a list
    // FIXME: Error on Android 10
    fun addToPlaylist() {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val audioId = call.argument<Int>("audioId")!!

        // Check if Playlist exists based on ID
        if (!checkPlaylistId(playlistId)) result.success(false)
        else {
            val uri =
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId.toLong())

            // If Android is 10/Q or above "count(*)" don't count, so we use other method.
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
                Log.i(channelError, e.toString())
            }
        }
    }

    // TODO: Add option to use a list
    fun removeFromPlaylist() {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val audioId = call.argument<Int>("audioId")!!

        // Check if Playlist exists based on ID
        if (!checkPlaylistId(playlistId)) result.success(false)
        else {
            try {
                val uri = MediaStore.Audio.Playlists.Members.getContentUri(
                    "external",
                    playlistId.toLong()
                )
                val where = MediaStore.Audio.Playlists.Members._ID + "=?"
                resolver.delete(uri, where, arrayOf(audioId.toString()))
                result.success(true)
            } catch (e: Exception) {
                Log.i(channelError, e.toString())
                result.success(false)
            }
        }
    }

    // TODO: Need to test
    fun moveItemTo() {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val from = call.argument<Int>("from")!!
        val to = call.argument<Int>("to")!!

        // Check if Playlist exists based on ID
        if (!checkPlaylistId(playlistId)) result.success(false)
        else {
            MediaStore.Audio.Playlists.Members.moveItem(
                resolver,
                playlistId.toLong(),
                from,
                to
            )
            result.success(true)
        }
    }

    fun renamePlaylist() {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!
        val newPlaylistName = call.argument<String>("newPName")!!

        // Check if Playlist exists based on ID
        if (!checkPlaylistId(playlistId)) result.success(false)
        else {
            contentValues.put(MediaStore.Audio.Playlists.NAME, newPlaylistName)
            contentValues.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis())
            resolver.update(uri, contentValues, "_id=${playlistId.toLong()}", null)
            result.success(true)
        }
    }

    // TODO: Need to test
    fun deletePlaylist() {
        this.resolver = context.contentResolver
        val playlistId = call.argument<Int>("playlistId")!!

        // Check if Playlist exists based on ID
        if (!checkPlaylistId(playlistId)) result.success(false)
        else {
            val delUri = ContentUris.withAppendedId(uri, playlistId.toLong())
            resolver.delete(delUri, null, null)
            result.success(true)
        }
    }

    // Return true if playlist exists
    private fun checkPlaylistId(pId: Int): Boolean {
        val cursor = resolver.query(
            uri,
            arrayOf(MediaStore.Audio.Playlists.NAME, MediaStore.Audio.Playlists._ID),
            null,
            null,
            null,
        )
        while (cursor != null && cursor.moveToNext()) {
            val playlistId = cursor.getInt(1)
            if (playlistId == pId) return true
        }
        cursor?.close()
        return false
    }
}