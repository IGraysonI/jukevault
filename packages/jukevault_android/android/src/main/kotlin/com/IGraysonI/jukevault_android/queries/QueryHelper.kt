package com.igraysoni.jukevault_android.queries

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.igraysoni.jukevault_android.enums.MediaTypeEnum
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File

/** Helper class for building queries. */
class QueryHelper {
    /** Method for loading extra information about audio/song. */
    fun loadAudioExtraInformation(
        uri: Uri,
        audioData: MutableMap<String, Any?>,
    ): MutableMap<String, Any?> {
        val file = File(audioData["_data"].toString())
        val audioFile = AudioFileIO.read(file)
        val tags = audioFile.tag

        // Display name without [Extension].
        audioData["_display_name_without_extension"] = file.nameWithoutExtension

        // File extension.
        audioData["file_extension"] = file.extension

        // Lyrics field from the audio file.
        audioData["lyrics"] = tags.getFirst(FieldKey.LYRICS)

        val temporaryUri = ContentUris.withAppendedId(uri, audioData["_id"].toString().toLong())
        audioData["_uri"] = temporaryUri
        return audioData
    }

    /** Method for separation of [String] from [Int] values for audio items. */
    fun loadAudioItem(
        itemProperty: String,
        cursor: Cursor,
    ): Any? {
        return when (itemProperty) {
            // [Int] values.
            "_id",
            "album_id",
            "artist_id" -> {
                // The [id] field in Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            "_size",
            "audio_id",
            "bookmark",
            "data_added",
            "data_modified",
            "duration",
            "track" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))

            // [Boolean] values.
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

    /** Method for separation of [String] from [Int] values for album items. */
    fun loadAlbumItem(
        itemProperty: String,
        cursor: Cursor,
    ): Any? {
        return when (itemProperty) {
            // [Int] values.
            "_id",
            "artist_id" -> {
                // [Album] id for Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            "numsongs" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))

            // String
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    /** Method for separation of [String] from [Int] values for artist items. */
    fun loadArtistItem(
        itemProperty: String,
        cursor: Cursor,
    ): Any? {
        return when (itemProperty) {
            // [Int] values.
            "_id" -> {
                // [Artist] id for Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }
            "number_of_albums",
            "number_of_tracks" -> cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))

            // String
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    /** Method for separation of [String] from [Int] values for genre items. */
    fun loadGenreItem(
        itemProperty: String,
        cursor: Cursor,
    ): Any? {
        return when (itemProperty) {
            // [Int] values.
            "_id" -> {
                // [Genre] id for Android >= 30/R is a [Long] instead of [Int].
                if (Build.VERSION.SDK_INT >= 30) {
                    cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))
                } else {
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemProperty))
                }
            }

            // String
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    /** Method for separation of [String] from [Int] values for playlist items. */
    fun loadPlaylistItem(
        itemProperty: String,
        cursor: Cursor,
    ): Any? {
        return when (itemProperty) {
            // [Int] values.
            "_id",
            "date_added",
            "date_modified" -> cursor.getLong(cursor.getColumnIndexOrThrow(itemProperty))

            // String
            else -> cursor.getString(cursor.getColumnIndexOrThrow(itemProperty))
        }
    }

    // TODO: Look into using a different method for getting the media count for playlists.
    @Suppress("DEPRECATION")
    /** Method for getting the media count for genres and playlists. */
    fun getMediaCount(
        mediaType: MediaTypeEnum,
        arguments: String,
        resolver: ContentResolver,
    ): Int {
        val uri: Uri = if (mediaType == MediaTypeEnum.GENRE) {
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
        mediaType: MediaTypeEnum,
        id: Number,
        resolver: ContentResolver,
    ): String? {
        // Method to query the first item for Audio/Album/Artist are the same.
        // Playlist and Genre require a different uri.
        val selection: String? = when (mediaType) {
            MediaTypeEnum.AUDIO -> MediaStore.Audio.Media._ID + "=?"
            MediaTypeEnum.ALBUM -> MediaStore.Audio.Media.ALBUM_ID + "=?"
            MediaTypeEnum.ARTIST -> MediaStore.Audio.Media.ARTIST_ID + "=?"
            MediaTypeEnum.GENRE -> null
            MediaTypeEnum.PLAYLIST -> null
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
                (mediaType == MediaTypeEnum.PLAYLIST) -> {
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
                (mediaType == MediaTypeEnum.GENRE) -> {
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
                    if (Build.VERSION.SDK_INT >= 29 && (mediaType == MediaTypeEnum.PLAYLIST
                                        || mediaType == MediaTypeEnum.ARTIST
                                        || mediaType == MediaTypeEnum.GENRE)) {
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