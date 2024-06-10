package com.igraysoni.jukevault_android.queries

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igraysoni.jukevault_android.controller.PermissionController
import com.igraysoni.jukevault_android.types.checkAlbumSortType
import com.igraysoni.jukevault_android.types.checkArtistSortType
import com.igraysoni.jukevault_android.types.checkArtistUriType
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ArtistsQuery class.
 * This class is used to query the artists.
 */
class ArtistsQuery : ViewModel() {
    // Main parameters.
    private val helper = QueryHelper()
    private var selection: String = ""

    // Non-nullable parameters.
    private lateinit var uri: Uri
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    private val artistProjection: Array<String> get() : Array<String> {
        return arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        )
    }

    private suspend fun loadArtists(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            val cursor = resolver.query(uri, artistProjection, selection, null, sortType)
            val artistList: ArrayList<MutableMap<String, Any?>> = ArrayList()

            // For each artist inside this cursor take one and format into [Map<String, Any?>].
            while (cursor != null && cursor.moveToNext()) {
                val temporaryData: MutableMap<String, Any?> = HashMap()
                for (artistMedia in cursor.columnNames) {
                    temporaryData[artistMedia] = helper.loadArtistItem(artistMedia, cursor)
                }
                artistList.add(temporaryData)
            }
            cursor?.close()
            return@withContext artistList
        }

    @Suppress("UNCHECKED_CAST")
    fun init(
        context: Context,
        result: MethodChannel.Result?,
        call: MethodCall?,
        sink: EventChannel.EventSink?,
        arguments: Map<*, *>?,
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

        sortType = checkArtistSortType(pSortType, pOrderType, pIgnoreCase)
        if (pLimit != null) sortType += " LIMIT $pLimit"
        uri = checkArtistUriType(pUri)

        // For every row from "toQuery" keep the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toQuery) {
            for (value: String in values) {
                selection += artistProjection[id] + " LIKE '%" + value + "%' " + "AND "
            }
        }

        // For every row from "toRemove" remove the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toRemove) {
            for (value: String in values) {
                selection += artistProjection[id] + " NOT LIKE '%" + value + "%' " + "AND "
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
            val resultAlbumList: ArrayList<MutableMap<String, Any?>> = loadArtists()
            sink?.success(resultAlbumList)
            result?.success(resultAlbumList)
        }
    }
}