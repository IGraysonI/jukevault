package com.igraysonl.jukevault

import android.app.Activity
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import androidx.annotation.NonNull
import com.igraysonl.jukevault.controllers.PermissionController
import com.igraysonl.jukevault.controllers.QueryController
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** JukevaultPlugin Central */
class JukevaultPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    companion object {
        // Get the current class name.
        private val TAG: String = this::class.java.name

        // Method channel name.
        private const val CHANNEL_NAME = "com.igraysonl.jukevault"
    }

    // Dart <-> Kotlin communication
    private lateinit var channel: MethodChannel

    private lateinit var context: Context
    private lateinit var queryController: QueryController
    private lateinit var permissionController: PermissionController

    // Main parameters
    private var activity: Activity? = null
    private var binding: ActivityPluginBinding? = null

    // Dart <-> Kotlin communication
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        // Define the [context]
        context = flutterPluginBinding.applicationContext

        // Setup the method channel communication.
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
    }

    // Methods will always follow the same route:
    // Receive method -> check permission -> controller -> do what's needed -> return to dart
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        // Setup the [QueryController].
        this.queryController = QueryController(context, call, result)

        // Both [activity] and [binding] are from [onAttachedToActivity].
        // If one of them are null. Something is really wrong.
        if (activity == null || binding == null) {
            result.error(
                "$TAG::onMethodCall",
                "The [activity] or [binding] parameter is null!",
                null
            )
        }

        // If user deny permission request a pop up will immediately show up.
        // If [retryRequest] is null, the message will only show after calling the method again.
        val retryRequest = call.argument<Boolean>("retryRequest") ?: false

        // Setup the [PermissionController]
        permissionController = PermissionController(retryRequest)

        // Detect the method.
        when (call.method) {
            // Permissions
            "permissionsStatus" -> result.success(permissionController.permissionStatus(context))
            "permissionsRequest" -> {
                // Add to controller the ability to listen the request result.
                binding!!.addRequestPermissionsResultListener(permissionController)

                // Request the permission.
                permissionController.requestPermission(activity!!, result)
            }

            // Device information
            "queryDeviceInfo" -> {
                result.success(
                    hashMapOf<String, Any>(
                        "device_model" to Build.MODEL,
                        "device_sys_version" to Build.VERSION.SDK_INT,
                        "device_sys_type" to "Android"
                    )
                )
            }

            // This method will scan the given path to update the 'state'.
            // When deleting a file using 'dart:io', call this method to update the file 'state'.
            "scan" -> {
                // TODO: Add option to scan multiple paths.
                val sPath: String = call.argument<String>("path")!!

                // Check if the given file is empty.
                if (sPath.isEmpty()) result.success(false)

                // Scan and return
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(sPath),
                    null
                ) { _, _ ->
                    result.success(true)
                }
            }

            // All others methods
            else -> queryController.call()
        }
    }

    // Dart <-> Kotlin communication
    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    // Attach the activity and get the [activity] and [binding].
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        // Define the activity and binding.
        this.activity = binding.activity
        this.binding = binding
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    // Detach all parameters.
    override fun onDetachedFromActivity() {
        // Remove the permission listener
        if (binding != null) {
            binding!!.removeRequestPermissionsResultListener(permissionController)
        }

        // Remove both [activity] and [binding].
        this.activity = null
        this.binding = null
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }
}
