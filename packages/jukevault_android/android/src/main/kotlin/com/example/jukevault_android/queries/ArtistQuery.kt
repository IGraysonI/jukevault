package com.example.jukevault_android.queries

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukevault_android.providers.PluginProvider
import com.example.jukevault_android.types.checkArtistsUriType
import com.example.jukevault_android.types.sort_types.checkArtistSortType
import com.example.jukevault_android.utils.artistProjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistQuery : ViewModel() {
    companion object {
        private const val TAG: String = "ArtistQuery"
    }

    private val helper = QueryHelper()

    // None of this methods can be null.
    private lateinit var uri: Uri
    private lateinit var resolver: ContentResolver
    private lateinit var sortType: String

    // Method to query all artists.
    fun queryArtists() {
        val call = PluginProvider.call()
        val result = PluginProvider.result()
        val context = PluginProvider.context()
        this.resolver = context.contentResolver

        // Sort: Type and Order
        sortType = checkArtistSortType(
            call.argument<Int>("sortType"),
            call.argument<Int>("orderType")!!,
            call.argument<Boolean>("ignoreCase")!!
        )

        // Check uri:
        // * 0 -> External.
        // * 1 -> Internal.
        uri = checkArtistsUriType(call.argument<Int>("uri")!!)

        Log.d(TAG, "Query config: ")
        Log.d(TAG, "\tsort type: $sortType")
        Log.d(TAG, "\turi: $uri")

        // Query everything in background for a better performance.
        viewModelScope.launch {
            val resultArtists = loadArtists()
            result.success(resultArtists)
        }
    }

    // Loading in background.
    private suspend fun loadArtists(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            // Setup the cursor with 'uri', 'projection' and 'sortType'.
            val cursor = resolver.query(
                uri,
                artistProjection,
                null,
                null,
                sortType
            )
            val artistList: ArrayList<MutableMap<String, Any?>> = ArrayList()
            Log.d(TAG, "Cursor count: ${cursor?.count}")

            // For each item(artist) inside this "cursor" object. Take one and format
            // into a Map<String, dynamic> and add to the list.
            while (cursor != null && cursor.moveToNext()) {
                val tempData: MutableMap<String, Any?> = HashMap()
                for (artistMedia in cursor.columnNames) {
                    tempData[artistMedia] = helper.loadArtistItem(artistMedia, cursor)
                }
                artistList.add(tempData)
            }

            // Close cursor to avoid memory leaks.
            cursor?.close()
            return@withContext artistList
        }
}