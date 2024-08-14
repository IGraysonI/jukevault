package com.igraysonl.jukevault.methods.helper

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.File

class QueryHelper {
    // This method will load some extra information about audio/song
    fun loadAudioExtraInfo(
        uri: Uri,
        audioData: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        val file = File(audioData["_data"].toString())

        // Getting displayName without [Extension].
        audioData["_display_name_wo_ext"] = file.nameWithoutExtension
        // Adding only the extension
        audioData["file_extension"] = file.extension

        // A different type of "data"
        val tempUri = ContentUris.withAppendedId(uri, audioData["_id"].toString().toLong())
        audioData["_uri"] = tempUri.toString()
        return audioData
    }

    // This method will separate [String] from [Int]
    fun loadAudioItem(itemProperty: String, cursor: Cursor): Any? {
        return when (itemProperty) {
            // Int
            "_id",
            "album_id",
            "artist_id" -> {
                // The [id] from Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            "_size",
            "audio_id",
            "bookmark",
            "date_added",
            "date_modified",
            "duration",
            "track" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
            // Boolean
            "is_alarm",
            "is_audiobook",
            "is_music",
            "is_notification",
            "is_podcast",
            "is_ringtone" -> {
                val value = cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
                if (value == "0") return false
                return true
            }
            // String
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    // This method will separate [String] from [Int]
    fun loadAlbumItem(itemProperty: String, cursor: Cursor): Any? {
        return when (itemProperty) {
            "_id",
            "artist_id" -> {
                // The [album] id from Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            "numsongs" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    // This method will separate [String] from [Int]
    fun loadPlaylistItem(itemProperty: String, cursor: Cursor): Any? {
        return when (itemProperty) {
            "_id",
            "date_added",
            "date_modified" -> cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    // This method will separate [String] from [Int]
    fun loadArtistItem(itemProperty: String, cursor: Cursor): Any? {
        return when (itemProperty) {
            "_id" -> {
                // The [artist] id from Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            "number_of_albums",
            "number_of_tracks" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    // This method will separate [String] from [Int]
    fun loadGenreItem(itemProperty: String, cursor: Cursor): Any? {
        return when (itemProperty) {
            "_id" -> {
                // The [genre] id from Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    // TODO: Look into using a different method for getting the media count for playlists.
    @Suppress("DEPRECATION")
    /** Method for getting the media count for genres and playlists. */
    fun getMediaCount(
        type: Int,
        arguments: String,
        resolver: ContentResolver,
    ): Int {
        val uri: Uri = if (type == 0) {
            MediaStore.Audio.Genres.Members.getContentUri("external", arguments.toLong())
        } else {
            MediaStore.Audio.Playlists.Members.getContentUri("external", arguments.toLong())
        }
        val cursor = resolver.query(uri, null, null, null, null)
        val count = cursor?.count ?: -1
        cursor?.close()
        return count
    }

    // TODO: Look into using a different method for getting first item for playlists
    @Suppress("DEPRECATION")
    /** Method for loading the first item for audio/album/artist/genre/playlist. */
    fun loadFirstItem(
        type: Int,
        id: Number,
        resolver: ContentResolver,
    ): String? {
        // Method to query the first item for Audio/Album/Artist are the same.
        // Playlist and Genre require a different uri.
        val selection: String? = when (type) {
            0 -> MediaStore.Audio.Media._ID + "=?"
            1 -> MediaStore.Audio.Media.ALBUM_ID + "=?"
            2 -> null
            3 -> MediaStore.Audio.Media.ARTIST_ID + "=?"
            4 -> null
            else -> return null
        }

        var dataOrId: String? = null
        var cursor: Cursor? = null
        try {
            // Uri for type 2 or 4 is different.
            //
            // Type 2 == Playlist
            // Type 4 == Genre
            when (true) {
                (type == 2) -> {
                    cursor = resolver.query(
                        MediaStore.Audio.Playlists.Members.getContentUri("external", id.toLong()),
                        arrayOf(
                            MediaStore.Audio.Playlists.Members.DATA,
                            MediaStore.Audio.Playlists.Members.AUDIO_ID,
                        ),
                        null,
                        null,
                        null
                    )
                }
                (type == 4) -> {
                    cursor = resolver.query(
                        MediaStore.Audio.Genres.Members.getContentUri("external", id.toLong()),
                        arrayOf(
                            MediaStore.Audio.Genres.Members.DATA,
                            MediaStore.Audio.Genres.Members.AUDIO_ID,
                        ),
                        null,
                        null,
                        null
                    )
                }
                else -> {
                    cursor = resolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media._ID,
                        ),
                        selection,
                        arrayOf(id.toString()),
                        null
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (cursor != null) {
            cursor.moveToFirst()
            // Try / Catch in case of empty playlist or genre.
            try{
                dataOrId =
                    if (Build.VERSION.SDK_INT >= 29 && (type == 2 || type == 3 || type == 4)) {
                        cursor.getString(1)
                    } else {
                        cursor.getString(0)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        cursor?.close()
        return dataOrId
    }
}