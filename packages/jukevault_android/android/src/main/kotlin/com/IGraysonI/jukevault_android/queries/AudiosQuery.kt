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
import com.igraysoni.jukevault_android.types.checkAudioSortType
import com.igraysoni.jukevault_android.types.checkAudioType
import com.igraysoni.jukevault_android.types.checkAudioUriType
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * AudiosQuery class.
 * This class is used to query the audios.
 */
class AudiosQuery : ViewModel() {
    // Main parameters.
    private val helper = QueryHelper()
    private var selection: String = ""

    // Non-nullable parameters.
    private lateinit var uri: Uri
    private lateinit var sortType: String
    private lateinit var resolver: ContentResolver

    private val audioProjection: Array<String> get() : Array<String> {
        val temporaryProjection = arrayListOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.BOOKMARK,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_PODCAST,
            MediaStore.Audio.Media.IS_RINGTONE,
        )

        // Only in Api 29 and above.
        if (Build.VERSION.SDK_INT >= 29) {
            temporaryProjection.add(MediaStore.Audio.Media.IS_AUDIOBOOK)
        }

        // Only in Api 30 and above.
        if (Build.VERSION.SDK_INT >= 30) {
            temporaryProjection.add(MediaStore.Audio.Media.GENRE)
            temporaryProjection.add(MediaStore.Audio.Media.GENRE_ID)
        }
        return temporaryProjection.toTypedArray()
    }

    private suspend fun loadAudios(): ArrayList<MutableMap<String, Any?>> =
        withContext(Dispatchers.IO) {
            val cursor = resolver.query(uri, audioProjection, selection, null, sortType)
            val audioList: ArrayList<MutableMap<String, Any?>> = ArrayList()

            // For each audio inside this cursor take one and format into [Map<String, Any?>].
            while (cursor != null && cursor.moveToNext()) {
                val temporaryData: MutableMap<String, Any?> = HashMap()
                for (audioMedia in cursor.columnNames) {
                    temporaryData[audioMedia] = helper.loadAudioItem(audioMedia, cursor)
                }
                // Audio extra information
                val temporaryExtraData = helper.loadAudioExtraInformation(uri, temporaryData)
                temporaryData.putAll(temporaryExtraData)
                audioList.add(temporaryData)
            }
            cursor?.close()
            return@withContext audioList
    }

    @Suppress("UNCHECKED_CAST")
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

        val toQuery: Map<Int, ArrayList<String>> = pArguments["toQuery"] as Map<Int, ArrayList<String>>
        val toRemove: Map<Int, ArrayList<String>> = pArguments["toRemove"] as Map<Int, ArrayList<String>>
        val type: Map<Int, Int> = pArguments["type"] as Map<Int, Int>

        sortType = checkAudioSortType(pSortType, pOrderType, pIgnoreCase)
        if (pLimit != null) sortType += " LIMIT $pLimit"
        uri = checkAudioUriType(pUri)

        // TODO: Add a generic toQuery and toRemove builder. This will remove a lot of
        //  unnecessary code.
        // For every row from "toQuery" keep the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toQuery) {
            for (value: String in values) {
                selection += audioProjection[id] + " LIKE '%" + value + "%' " + "AND "
            }
        }

        // For every row from "toRemove" remove the media containing the filter.
        for ((id: Int, values: ArrayList<String>) in toRemove) {
            for (value: String in values) {
                selection += audioProjection[id] + " NOT LIKE '%" + value + "%' " + "AND "
            }
        }

        // Add/Remove audio type. Example: IS_MUSIC, IS_ALARM, IS_NOTIFICATION, IS_PODCAST, IS_RINGTONE.
        for (audioType in type) {
            selection += checkAudioType(audioType.key) + "=" + "${audioType.value} " + "AND "
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
            val resultAudioList: ArrayList<MutableMap<String, Any?>> = loadAudios()
            sink?.success(resultAudioList)
            result?.success(resultAudioList)
        }
    }
}