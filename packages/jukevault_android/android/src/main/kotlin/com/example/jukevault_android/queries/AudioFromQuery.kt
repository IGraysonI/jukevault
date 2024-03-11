package com.example.jukevault_android.queries

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukevault_android.providers.PluginProvider
import com.example.jukevault_android.types.checkAudiosFromType
import com.example.jukevault_android.types.sort_types.checkSongSortType
import com.example.jukevault_android.utils.songProjection
import io.flutter.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioFromQuery : ViewModel() {
    companion object {
        private const val TAG: String = "AudioFromQuery"
        private val URI: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    // Main parameters.
    private val helper = QueryHelper()
    private var pId = 0
    private var pUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    // None of this parameters can be null.
    private lateinit var where: String
    private lateinit var whereVal: String
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    fun querySongsFrom() {
        val call = PluginProvider.call()
        val result = PluginProvider.result()
        val context = PluginProvider.context()
        this.resolver = context.contentResolver

        // The type of item:
        // * 0 -> Album
        // * 1 -> Album Id
        // * 2 -> Artist
        // * 3 -> Artist Id
        // * 4 -> Genre
        // * 5 -> Genre Id
        // * 6 -> Playlist
        val type = call.argument<Int>("type")!!

        // Sort: Type and Order.
        sortType = checkSongSortType(
            call.argument<Int>("sortType"),
            call.argument<Int>("orderType")!!,
            call.argument<Boolean>("ignoreCase")!!
        )

        Log.d(TAG, "Query config: ")
        Log.d(TAG, "\tsortType: $sortType")
        Log.d(TAG, "\ttype: $type")
        Log.d(TAG, "\turi: $URI")

        // TODO: Add a better way to handle this query
        // This will fix (for now) the problem between Android < 30 && Android >= 30.
        // The method used to query genres on Android >= 30. Don't work properly on Android < 30
        // so we need to separate.
        //
        // If helper == 6(Playlist) send to 'querySongsFromPlaylistOrGenre' in any version.
        // If helper == 4(Genre) || helper == 5(Genre Id) and Android <30 send to
        // 'querySongsFromPlaylistOrGenre'. Else follow the rest of the normal code.
        //
        // Why? Android 10 and below doesn't have 'genre' category and we need to use a workaround.
        // 'MediaStore'(https://developer.android.com/reference/android/provider/MediaStore.Audio.AudioColumns#GENRE)
        if (type == 6 || ((type == 4 || type == 5) && Build.VERSION.SDK_INT < 30)) {
            // Words on Android 10
            querySongsFromPlaylistOrGenre(result, call, type)
        } else {
            // Works on Android 11 and above.
            // 'whereVal' -> Album/Arist/Genre.
            // 'where' -> uri
            whereVal = call.argument<Any>("where")!!.toString()
            where = checkAudiosFromType(type)

            // Query everything in background.
            viewModelScope.launch {
                var resultSongList = loadSongsFrom()
                result.success(resultSongList)
            }
        }
    }

    private suspend fun loadSongsFrom(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            // Setup the cursor with 'uri', 'projection', 'selection(where)' and 'values(whereVal)'.
            val cursor = resolver.query(URI, songProjection(), where, arrayOf(whereVal), sortType)
            val songsFromList: ArrayList<MutableMap<String, Any?>> = ArrayList()
            Log.d(TAG, "Cursor count: ${cursor?.count}")

            // For each item in the cursor, take one and format
            // into a Map<String, Any?> and add to the list.
            while (cursor != null && cursor.moveToNext()) {
                val tempData: MutableMap<String, Any?> = HashMap()
                for (audioMedia in cursor.columnNames) {
                    tempData[audioMedia] = helper.loadSongItem(audioMedia, cursor)
                }

                // Get an extra information from audio, e.g: extension, uri, etc...
                val tempExtraData = helper.loadSongExtraInfo(URI, tempData)
                tempData.putAll(tempExtraData)
                songsFromList.add(tempData)
            }
            cursor?.close()
            return@withContext songsFromList
        }

    // TODO: Remove unnecessary code.
    private fun querySongsFromPlaylistOrGenre(
        result: MethodChannel.Result,
        call: MethodCall,
        type: Int
    ) {
        val info = call.argument<Any>("where")!!

        // Check if playlist exists using the id.
        val checkedName = if (type == 4 || type == 5) {
            checkName(genreName = info.toString())
        } else {
            checkName(pName = info.toString())
        }
        if (!checkedName) pId = info.toString().toInt()

        pUri = if (type == 4 || type == 5) {
            MediaStore.Audio.Genres.Members.getContentUri("external", pId.toLong())
        } else {
            MediaStore.Audio.Playlists.Members.getContentUri("external", pId.toLong())
        }

        // Query everything in background.
        viewModelScope.launch {
            var resultSongList = loadSongsFromPlaylistOrGenre()
            result.success(resultSongList)
        }
    }

    private suspend fun loadSongsFromPlaylistOrGenre(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            val songFrom: ArrayList<MutableMap<String, Any?>> = ArrayList()
            val cursor = resolver.query(pUri, songProjection(), null, null, sortType)
            Log.d(TAG, "Cursor count: ${cursor?.count}")
            while (cursor != null && cursor.moveToNext()) {
                val tempData: MutableMap<String, Any?> = HashMap()
                for (audioMedia in cursor.columnNames) {
                    tempData[audioMedia] = helper.loadSongItem(audioMedia, cursor)
                }

                // Get an extra information from audio, e.g: extension, uri, etc...
                val tempExtraData = helper.loadSongExtraInfo(URI, tempData)
                tempData.putAll(tempExtraData)
                songFrom.add(tempData)
            }
            cursor?.close()
            return@withContext songFrom
        }

    private fun checkName(pName: String? = null, genreName: String? = null): Boolean {
        val uri: Uri
        val projection: Array<String>

        if (pName != null) {
            uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
            projection = arrayOf(MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME)
        } else {
            uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
            projection = arrayOf(MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME)
        }
        val cursor = resolver.query(uri, projection, null, null, null)
        while (cursor != null && cursor.moveToNext()) {
            val name = cursor.getString(0)
            if (name != null && name == pName || name == genreName) {
                pId = cursor.getInt(1)
                return true
            }
        }
        cursor?.close()
        return false
    }
}