package com.igraysoni.jukevault_android.queries

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igraysoni.jukevault_android.controller.PermissionController
import com.igraysoni.jukevault_android.types.checkAlbumSortType
import com.igraysoni.jukevault_android.types.checkAlbumUriType
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** AlbumsQuery class.
 * This class is used to query the albums.
 */
class AlbumsQuery: ViewModel() {
    // Main parameters.
    private val helper = QueryHelper()
    private var selection: String = ""

    // Non-nullable parameters.
    private lateinit var uri: Uri
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    private val albumProjection: Array<String> get() : Array<String> {
            val temporaryProjection = arrayListOf(
                MediaStore.Audio.Albums.ALBUM_ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.LAST_YEAR,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS_FOR_ARTIST,
            )

            // Only in API >= 29.
            if (Build.VERSION.SDK_INT > 29) {
                temporaryProjection.add(3, MediaStore.Audio.Albums.ARTIST_ID)
            }
            return temporaryProjection.toTypedArray()
        }

    private suspend fun loadAlbums(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
        val cursor = resolver.query(uri, albumProjection, selection, null, sortType)
            val albumList: ArrayList<MutableMap<String, Any?>> = ArrayList()

            // For each album inside this cursor take one and format into [Map<String, Any?>].
            while (cursor != null && cursor.moveToNext()) {
               val temporaryData: MutableMap<String, Any?> = HashMap()
                for (albumMedia in cursor.columnNames) {
                    temporaryData[albumMedia] = helper.loadAlbumItem(albumMedia, cursor)
                }

                // In Android 10 and above [album_art] will return null, to avoid problems
                // we will remove it.
                val art = temporaryData["album_art"].toString()
                if (art.isEmpty()) temporaryData.remove("album_art")
                albumList.add(temporaryData)
            }
            cursor?.close()
            return@withContext albumList
    }

    @Suppress("UNCHECKED_CAST")
    fun initAlbums(
        context: Context,
        result: MethodChannel.Result?,
        call: MethodCall?,
        sink: EventChannel.EventSink?,
        arguments: Map<*, *>?
    ) {
        resolver = context.contentResolver

        // Defining the [arguments], that will be delivered from either [result] or [sink].
        val pArguments: Map<String, Any?> = (arguments ?: call?.arguments) as Map<String, Any?>

        // Basic filters.
        val pSortType: Int? = pArguments["sortType"] as Int?
        val pOrderType: Int = pArguments["orderType"] as Int
        val pIgnoreCase: Boolean = pArguments["ignoreCase"] as Boolean
        val pUri: Int = pArguments["uri"] as Int
        val pLimit: Int? = pArguments["limit"] as Int?

        val toQuery: Map<Int, ArrayList<String>> = pArguments["toQuery"] as Map<Int, ArrayList<String>>
        val toRemove: Map<Int, ArrayList<String>> = pArguments["toRemove"] as Map<Int, ArrayList<String>>

        sortType = checkAlbumSortType(pSortType, pOrderType, pIgnoreCase)
        if (pLimit != null) sortType += " LIMIT $pLimit"
        uri = checkAlbumUriType(pUri)

        // For every row from "toQuery" keep the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toQuery) {
            for (value: String in values) {
                selection += albumProjection[id] + " LIKE '%" + value + "%' " + "AND "
            }
        }

        // For every row from "toRemove" remove the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toRemove) {
            for (value: String in values) {
                selection += albumProjection[id] + " NOT LIKE '%" + value + "%' " + "AND "
            }
        }
        selection = selection.removeSuffix("AND ")

        val hasPermission: Boolean = PermissionController().permissionStatus(context)
        if (!hasPermission) {
            sink?.error(
                "403",
                "The application doesn't have permissions.",
                "Call the [permissionRequest] method to request the permissions."
            )
            result?.error(
                "403",
                "The application doesn't have permissions.",
                "Call the [permissionRequest] method to request the permissions."
            )
            return
        }
        viewModelScope.launch {
            val resultAlbumList: ArrayList<MutableMap<String, Any?>> = loadAlbums()
            sink?.success(resultAlbumList)
            result?.success(resultAlbumList)
        }
    }
}