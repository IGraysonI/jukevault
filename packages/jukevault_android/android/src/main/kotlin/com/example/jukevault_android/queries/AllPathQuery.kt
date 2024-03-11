package com.example.jukevault_android.queries

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.jukevault_android.providers.PluginProvider
import java.io.File

class AllPathQuery {
    companion object {
        private const val TAG: String = "AllPathQuery"

        private val URI: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    private lateinit var resolver: ContentResolver

    // Method to query all paths.
    fun queryAllPath() {
        val result = PluginProvider.result()
        val context = PluginProvider.context()
        this.resolver = context.contentResolver
        val resultAllPath = loadAllPath()
        result.success(resultAllPath)
    }

    private fun loadAllPath(): ArrayList<String> {
        val cursor = resolver.query(URI, null, null, null, null)
        val songPathList: ArrayList<String> = ArrayList()
        Log.d(TAG, "Cursor count: ${cursor?.count}")

        // For each item(path) inside this "cursor" object. Take one and add to the list
        while (cursor != null && cursor.moveToNext()) {
            val content =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            val path = File(content).parent

            // Check if path is null or if already exists in the list.
            if (path != null && !songPathList.contains(path)) {
                songPathList.add(path)
            }
        }

        // Close cursor to avoid memory leaks.
        cursor?.close()
        return songPathList
    }
}