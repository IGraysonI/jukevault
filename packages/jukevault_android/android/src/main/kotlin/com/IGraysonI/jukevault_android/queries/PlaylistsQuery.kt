package com.igraysoni.jukevault_android.queries

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igraysoni.jukevault_android.controller.PermissionController
import com.igraysoni.jukevault_android.types.checkAudioSortType
import com.igraysoni.jukevault_android.types.checkAudioType
import com.igraysoni.jukevault_android.types.checkAudioUriType
import com.igraysoni.jukevault_android.types.checkPlaylistSortType
import com.igraysoni.jukevault_android.types.checkPlaylistUriType
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * PlaylistsQuery class.
 * This class is used to query the playlists.
 */
// TODO: Fix deprecation.
@Suppress("DEPRECATION")
class PlaylistsQuery : ViewModel() {
    // Main parameters.
    private val helper = QueryHelper()
    private var selection: String = ""

    // Non-nullable parameters.
    private lateinit var uri: Uri
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    val playlistProjection: Array<String> get() = arrayOf(
        MediaStore.Audio.Playlists._ID,
        MediaStore.Audio.Playlists.DATA,
        MediaStore.Audio.Playlists.DATE_ADDED,
        MediaStore.Audio.Playlists.DATE_MODIFIED,
        MediaStore.Audio.Playlists.NAME,
    )

    private suspend fun loadPlaylists(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            val cursor = resolver.query(uri, playlistProjection, selection, null, sortType)
            val playlistList: ArrayList<MutableMap<String, Any?>> = ArrayList()
            // For each playlist inside this cursor take one and format into [Map<String, Any?>].
            while (cursor != null && cursor.moveToNext()) {
                val temporaryData: MutableMap<String, Any?> = HashMap()
                for (playlistMedia in cursor.columnNames) {
                    temporaryData[playlistMedia] = helper.loadPlaylistItem(playlistMedia, cursor)
                }
                // Audio extra information
               val mediaCount = helper.getMediaCount(1, temporaryData["_id"].toString(), resolver)
                temporaryData["num_of_songs"] = mediaCount
                playlistList.add(temporaryData)
            }
            cursor?.close()
            return@withContext playlistList
        }

    fun init(
        context: Context,
        result: MethodChannel.Result? = null,
        call: MethodCall? = null,
        sink: EventChannel.EventSink? = null,
        arguments: Map<*, *>? = null,
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

        val toQuery: Map<Int, ArrayList<String>> =
            pArguments["toQuery"] as Map<Int, ArrayList<String>>
        val toRemove: Map<Int, ArrayList<String>> =
            pArguments["toRemove"] as Map<Int, ArrayList<String>>
        val type: Map<Int, Int> = pArguments["type"] as Map<Int, Int>

        sortType = checkPlaylistSortType(pSortType, pOrderType, pIgnoreCase)
        if (pLimit != null) sortType += " LIMIT $pLimit"
        uri = checkPlaylistUriType(pUri)

        // For every row from "toQuery" keep the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toQuery) {
            for (value: String in values) {
                selection += playlistProjection[id] + " LIKE '%" + value + "%' " + "AND "
            }
        }

        // For every row from "toRemove" remove the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toRemove) {
            for (value: String in values) {
                selection += playlistProjection[id] + " NOT LIKE '%" + value + "%' " + "AND "
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
            val resultAudioList: ArrayList<MutableMap<String, Any?>> = loadPlaylists()
            sink?.success(resultAudioList)
            result?.success(resultAudioList)
        }
    }

}