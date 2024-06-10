package com.igraysoni.jukevault_android.queries

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igraysoni.jukevault_android.controller.PermissionController
import com.igraysoni.jukevault_android.types.checkAudioType
import com.igraysoni.jukevault_android.types.checkGenreSortType
import com.igraysoni.jukevault_android.types.checkGenreUriType
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * GenresQuery class.
 * This class is used to query the genres.
 */
class GenresQuery : ViewModel() {
    // Main parameters.
    private val helper = QueryHelper()
    private var selection: String = ""

    // Non-nullable parameters.
    private lateinit var uri: Uri
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    private val genreProjection: Array<String> get() = arrayOf(
        MediaStore.Audio.Genres._ID,
        MediaStore.Audio.Genres.NAME
    )

    private suspend fun loadGenres(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            val cursor = resolver.query(uri, genreProjection, selection, null, sortType)
            val genreList = arrayListOf<MutableMap<String, Any?>>()

            // For each genre inside this cursor take one and format into [Map<String, Any?>].
            while (cursor != null && cursor.moveToNext()) {
               val temporaryData: MutableMap<String, Any?> = HashMap()
                for (genreMedia in cursor.columnNames) {
                    temporaryData[genreMedia] = helper.loadGenreItem(genreMedia, cursor)
                }
                val mediaCount = helper.getMediaCount(0, temporaryData["_id"].toString(), resolver)
                temporaryData["num_of_songs"] = mediaCount
                if (temporaryData["name"] != null && temporaryData["_id"] != 0) {
                    genreList.add(temporaryData)
                }
            }
            cursor?.close()
            return@withContext genreList
        }

    fun init(
        context: Context,
        result: MethodChannel.Result? = null,
        call: MethodCall? = null,
        sink: EventChannel.EventSink? = null,
        arguments: Map<*, *>? = null
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

        sortType = checkGenreSortType(pSortType, pOrderType, pIgnoreCase)
        if (pLimit != null) sortType += " LIMIT $pLimit"
        uri = checkGenreUriType(pUri)

        // For every row from "toQuery" keep the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toQuery) {
            for (value: String in values) {
                selection += genreProjection[id] + " LIKE '%" + value + "%' " + "AND "
            }
        }

        // For every row from "toRemove" remove the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toRemove) {
            for (value: String in values) {
                selection += genreProjection[id] + " NOT LIKE '%" + value + "%' " + "AND "
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
            val resultGenreList: ArrayList<MutableMap<String, Any?>> = loadGenres()
            sink?.success(resultGenreList)
            result?.success(resultGenreList)
        }
    }
}