package com.igraysoni.jukevault_android.queries

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igraysoni.jukevault_android.controller.PermissionController
import com.igraysoni.jukevault_android.types.checkArtworkFormat
import com.igraysoni.jukevault_android.types.checkArtworkType
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

/**
 * ArtworkQuery class.
 * This class is used to query the artwork.
 */
class ArtworkQuery: ViewModel() {
    // Main parameters.
    private val helper = QueryHelper()
    private var type: Int = -1
    private var id: Number = 0
    private var quality: Int = 50
    private var size: Int = 100

    // Non-nullable parameters.
    private lateinit var query: Uri
    private lateinit var uri: Uri
    private lateinit var resolver: ContentResolver
    private lateinit var format: Bitmap.CompressFormat

    private suspend fun loadArtwork(): ByteArray? = withContext(Dispatchers.IO) {
        var artworkData: ByteArray? = null

        // Check [Android] version and query [type]
        //
        // If [Android] >= 29/Q
        //  function has limited access to files and [loadThumbnail] is used.
        // If [Android] < 29/Q
        //  function uses [embeddedPicture] from [MediaMetadataRetriever].
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                // If [type] is [2] - Playlist, [3] - Artist or [4] - Genre we get first item from
                // the list.
                //
                // In [MethodChannel] the [id] is defined as [Number], its would be converted
                // to [Long].
                query = if (type == 2 || type == 3 || type == 4) {
                    val item = helper.loadFirstItem(type, id, resolver) ?: return@withContext null
                    ContentUris.withAppendedId(uri, item.toLong())
                } else {
                    ContentUris.withAppendedId(uri, id.toLong())
                }
                val bitmap = resolver.loadThumbnail(query, Size(size, size), null)
                artworkData = convertOrResize(bitmap = bitmap)!!
            } catch (e: Exception) {
                Log.e("ArtworkQuery", "Error: ${e.message}")
            }
        }
        return@withContext artworkData
    }

    private fun convertOrResize(
        bitmap: Bitmap? = null,
        byteArray: ByteArray? = null,
    ): ByteArray? {
        val convertedBytes: ByteArray?
        val byteArrayBase = ByteArrayOutputStream()
        try {
            if (bitmap != null) {
                bitmap.compress(format, quality, byteArrayBase)
            } else {
                val convertedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
                convertedBitmap.compress(format, quality, byteArrayBase)
            }
        } catch (e: Exception) {
            Log.e("ArtworkQuery", "Error: ${e.message}")
        }
        convertedBytes = byteArrayBase.toByteArray()
        byteArrayBase.close()
        return convertedBytes
    }

    fun queryArtwork(
        context: Context,
        result: MethodChannel.Result,
        call: MethodCall,
    ) {
        resolver = context.contentResolver
        id = call.argument<Number>("id")!!
        size = call.argument<Int>("size")!!

        // The [quality] value cannot be greater than 100, if it is set to [50].
        quality = call.argument<Int>("quality")!!
        if (quality > 100) quality = 50

        format = checkArtworkFormat(call.argument<Int>("format")!!)
        type = call.argument<Int>("type")!!

        // Check uri:
        // [0] - Audio.
        // [1] - Album.
        // [2] - Playlist.
        // [3] - Artist.
        // [4] - Genre.
        uri = checkArtworkType(type)

        val hasPermission: Boolean = PermissionController().permissionStatus(context)
        if (!hasPermission) {
            result.error(
                "403",
                "The application doesn't have permissions.",
                "Call the [permissionRequest] method to request the permissions."
            )
            return
        }
        viewModelScope.launch {
            var resultArtwork: ByteArray? = loadArtwork()
            if (resultArtwork != null && resultArtwork.isEmpty()) resultArtwork = null
            result.success(
                hashMapOf<String, Any?>(
                        "_id" to id.toInt(),
                        "artwork" to resultArtwork,
                        "path" to query.path,
                        "type" to format.name,
                        )
            )
        }
    }
}