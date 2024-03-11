package com.example.jukevault_android.queries

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.File

class QueryHelper {
    // This method will load some extra information about songs.
    fun loadSongExtraInfo(
        uri: Uri,
        songData: MutableMap<String, Any?>
    ): MutableMap<String, Any?> {
        val file = File(songData["_data"].toString())

        // Getting displayName without [Extension].
        songData["_display_name_wo_ext"] = file.nameWithoutExtension
        // Adding only the extension.
        songData["file_extension"] = file.extension

        // A different type of "data"
        val tempUrl = ContentUris.withAppendedId(uri, songData["_id"].toString().toLong())
        songData["_uri"] = tempUrl.toString()

        return songData
    }

    // This method will separate [String] from [Int].
    fun loadSongItem(itemProperty: String, cursor: Cursor): Any? {
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

            "size",
            "bookmark",
            "date_added",
            "date_modified",
            "duration",
            "track" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
            // Boolean
            "is_alarm",
            "is_music",
            "is_notification",
            "is_podcast",
            "is_audiobook",
            "is_ringtone" -> {
                val value = cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
                if (value == "0") return false
                return true
            }
            // String
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

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

            "number_of_songs" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    fun loadPlaylistItem(itemProperty: String, cursor: Cursor): Any? {
        return when (itemProperty) {
            "_id",
            "date_added",
            "date_modified" -> cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))

            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

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

    fun getMediaCount(type: Int, arg: String, resolver: ContentResolver): Int {
        val uri: Uri = if (type == 0) {
            MediaStore.Audio.Genres.Members.getContentUri("external", arg.toLong())

        } else {
            MediaStore.Audio.Playlists.Members.getContentUri("external", arg.toLong())
        }
        val cursor = resolver.query(uri, null, null, null, null)
        val count = cursor?.count ?: -1
        cursor?.close()
        return count
    }

    // Ignore the [Data] deprecation because this plugin supports older versions.
    @Suppress("DEPRECATION")
    fun loadFirstItem(type: Int, id: Number, resolver: ContentResolver): String? {
        // We use almost the same method to 'query' the first item from Song/Album/Artist and we
        // need to use a different uri when 'querying' from playlist.
        // If [type] is something different, return null.
        val selection: String? = when (type) {
            0 -> MediaStore.Audio.Media._ID + "=?"
            1 -> MediaStore.Audio.Media.ALBUM_ID + "=?"
            2 -> null
            3 -> MediaStore.Audio.Media.ARTIST_ID + "=?"
            4 -> null
            else -> return null
        }

        var dateOrId: String? = null
        var cursor: Cursor? = null

        try {
            // Type 2 or 4 we use a different uri.
            //
            // Type 2 == Playlist
            // Type 4 == Genre
            //
            // And for the other types, we use the normal uri.
            when (true) {
                (type == 2 && selection == null) -> {
                    cursor = resolver.query(
                        MediaStore.Audio.Playlists.Members.getContentUri("external", id.toLong()),
                        arrayOf(
                            MediaStore.Audio.Playlists.Members.DATA,
                            MediaStore.Audio.Playlists.Members.AUDIO_ID
                        ),
                        null,
                        null,
                        null
                    )
                }

                (type == 4 && selection == null) -> {
                    cursor = resolver.query(
                        MediaStore.Audio.Genres.Members.getContentUri("external", id.toLong()),
                        arrayOf(
                            MediaStore.Audio.Genres.Members.DATA,
                            MediaStore.Audio.Genres.Members.AUDIO_ID
                        ),
                        null,
                        null,
                        null
                    )
                }

                else -> {
                    cursor = resolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID),
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
            // Try / Catch to avoid problems. Everytime someone requests the first song from the
            // playlist and this playlist is empty will crash the app. So we just 'print' the error.
            try {
                dateOrId =
                    if (Build.VERSION.SDK_INT >= 29 && (type == 2 || type == 3 || type == 4)) {
                        cursor.getString(1)
                    } else {
                        cursor.getString(0)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("JukevaultAndroidError", e.toString())
            }
        }
        cursor?.close()

        return dateOrId
    }

    fun chooseWithFilterType(uri: Uri, itemProperty: String, cursor: Cursor): Any? {
        return when (uri) {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI -> loadSongItem(itemProperty, cursor)
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI -> loadAlbumItem(itemProperty, cursor)
            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI -> loadPlaylistItem(
                itemProperty,
                cursor
            )

            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI -> loadArtistItem(itemProperty, cursor)
            MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI -> loadGenreItem(itemProperty, cursor)
            else -> null
        }
    }
}


