package com.igraysonl.jukevault.methods.queries

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igraysonl.jukevault.controllers.PermissionController
import com.igraysonl.jukevault.methods.helper.QueryHelper
import com.igraysonl.jukevault.types.checkGenresUriType
import com.igraysonl.jukevault.types.sorttypes.checkGenreSortType
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** GenresQuery */
class GenresQuery : ViewModel() {

    // Main parameters.
    private val helper = QueryHelper()
    private var selection: String = ""

    // None of this methods can be null.
    private lateinit var uri: Uri
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    // Genres projection
    private val genreProjection: Array<String>
        get() = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
        )

    @Suppress("UNCHECKED_CAST")
    fun init(
        context: Context,
        // Call from 'MethodChannel' (method).
        result: MethodChannel.Result? = null,
        call: MethodCall? = null,
        args: Map<*, *>? = null
    ) {
        // Define the [resolver]. This method is used to call the [query].
        resolver = context.contentResolver

        // Define the [args]. Will be delivered from:
        // [result](From MethodChannel) or [sink](From EventChannel)
        val pArgs: Map<String, Any?> = (args ?: call?.arguments) as Map<String, Any?>

        // Define all 'basic' filters.
        val pSortType: Int? = pArgs["sortType"] as Int?
        val pOrderType: Int = pArgs["orderType"] as Int
        val pIgnoreCase: Boolean = pArgs["ignoreCase"] as Boolean
        val pUri: Int = pArgs["uri"] as Int
        val pLimit: Int? = pArgs["limit"] as Int?

        // Define the [toQuery] and [toRemove] filters.
        val toQuery: Map<Int, ArrayList<String>> = pArgs["toQuery"] as Map<Int, ArrayList<String>>
        val toRemove: Map<Int, ArrayList<String>> = pArgs["toRemove"] as Map<Int, ArrayList<String>>

        // Sort: Type and Order.
        sortType = checkGenreSortType(pSortType, pOrderType, pIgnoreCase)

        // Add a 'query' limit(if not null).
        if (pLimit != null) sortType += " LIMIT $pLimit"

        // Check uri:
        //   * [0]: External.
        //   * [1]: Internal.
        uri = checkGenresUriType(pUri)

        // For every 'row' from 'toQuery', *keep* the media that contains the 'filter'.
        for ((id, values) in toQuery) {
            for (value in values) {
                // The comparison type: contains
                selection += genreProjection[id] + " LIKE '%" + value + "%' " + "AND "
            }
        }

        // For every 'row' from 'toRemove', *remove* the media that contains the 'filter'.
        for ((id, values) in toRemove) {
            for (value in values) {
                // The comparison type: contains
                selection += genreProjection[id] + " NOT LIKE '%" + value + "%' " + "AND "
            }
        }

        // Remove the 'AND ' keyword from selection.
        selection = selection.removeSuffix("AND ")

        // Request permission status from the 'main' method.
        val hasPermission: Boolean = PermissionController().permissionStatus(context)

        // We cannot 'query' without permission so, throw a PlatformException.
        // Only one 'channel' will be 'functional'. If is null, ignore, if not, send the error.
        if (!hasPermission) {
            // Method from 'MethodChannel' (method)
            result?.error(
                "403",
                "The app doesn't have permission to read files.",
                "Call the [permissionsRequest] method or install a external plugin to handle the app permission."
            )

            // 'Exit' the function
            return
        }

        // Query everything in background for a better performance.
        viewModelScope.launch {
            // Start 'querying'.
            val resultGenreList: ArrayList<MutableMap<String, Any?>> = loadGenres()

            // After loading the information, send the 'result'.
            result?.success(resultGenreList)
        }
    }

    // Loading in Background
    private suspend fun loadGenres(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            // Setup the cursor with [uri], [projection] and [sortType].
            val cursor = resolver.query(uri, genreProjection, selection, null, sortType)
            // Empty list.
            val genreList: ArrayList<MutableMap<String, Any?>> = ArrayList()

            // For each item(genre) inside this "cursor", take one and "format"
            // into a [Map<String, dynamic>].
            while (cursor != null && cursor.moveToNext()) {
                val genreData: MutableMap<String, Any?> = HashMap()
                for (genreMedia in cursor.columnNames) {
                    genreData[genreMedia] = helper.loadGenreItem(genreMedia, cursor)
                }

                // Count and add the number of audios for every genre.
                val mediaCount = helper.getMediaCount(0, genreData["_id"].toString(), resolver)
                genreData["num_of_songs"] = mediaCount

                if (genreData["name"] != null && genreData["_id"] != 0) {
                    genreList.add(genreData)
                }
            }

            // Close cursor to avoid memory leaks.
            cursor?.close()
            // After finish the "query", go back to the "main" thread(You can only call flutter
            // inside the main thread).
            return@withContext genreList
        }
}