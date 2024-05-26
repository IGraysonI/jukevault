package com.example.jukevault_android.methods.observers

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.example.jukevault_android.methods.queries.AlbumsQuery
import io.flutter.plugin.common.EventChannel

class AlbumsObserver(
    private val context: Context,
) : ContentObserver(Handler(Looper.getMainLooper())), EventChannel.StreamHandler {

    companion object {
        // Fixed URI used as a path to [Albums].
        // Every 'observer' has your own URI.
        private val URI: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
    }

    // [Get] variable to detect when the observer is running or not.
    val isRunning: Boolean get() = pIsRunning

    // Main parameters
    private val query: AlbumsQuery get() = AlbumsQuery()

    private var sink: EventChannel.EventSink? = null
    private var args: Map<*, *>? = null

    // [Internal] variable to detect when the observer is running or not.
    private var pIsRunning: Boolean = false

    // This function will be 'called' everytime the MediaStore -> Albums change.
    override fun onChange(selfChange: Boolean) {
        // We reuse the [queryAlbums] from [AlbumsQuery] and 'query' everytime some change is detected.
        query.init(context, sink = sink, args = args)
    }

    // This function will be 'called' everytime the Flutter [EventChannel] is called.
    // [AlbumsObserver] event channel name: 'com.example.jukevault_android/albums_observer'.
    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        // Define the [sink] and [args]
        // The sink is used to define/send a event(change) or error.
        sink = events
        args = arguments as Map<*, *>

        // Register this class to observe the [MediaStore].
        context.contentResolver.registerContentObserver(URI, true, this)

        // Define the [AlbumsObserver] as running.
        pIsRunning = true

        // Send the initial data.
        query.init(context, sink = sink, args = args)
    }

    // This function will cancel the listener between the Dart <-> Native communication and at the
    // same time will unregister the 'MediaStore' listener.
    override fun onCancel(arguments: Any?) {
        // Stop listening the [MediaStore]
        context.contentResolver.unregisterContentObserver(this)

        // Cancel the [sink] and define [isRunning] as false.
        sink = null
        pIsRunning = false
    }
}