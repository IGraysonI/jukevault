package com.example.jukevault_android


import android.media.MediaScannerConnection
import android.os.Build
import com.example.jukevault_android.consts.Method
import com.example.jukevault_android.controllers.MethodController
import com.example.jukevault_android.controllers.PermissionController
import com.example.jukevault_android.providers.PluginProvider
import io.flutter.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** JukevaultAndroidPlugin */
class JukevaultAndroidPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    init {
        // Set default logging level.
        Log.setLogLevel(Log.WARN)
    }

    companion object {
        // Get the current class name.
        private const val TAG: String = "JukevaultAndroidPlugin"

        // Method channel name.
        //TODO: Change this to the name of your plugin.
        private const val CHANNEL_NAME: String = "jukevault_android"
    }

    private var permissionController = PermissionController()
    private var methodController = MethodController()
    private var binding: ActivityPluginBinding? = null

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.i(TAG, "onAttachedToEngine")
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        Log.i(TAG, "onMethodCall start")

        // Init the plugin provider with the current call and result.
        PluginProvider.setCurrentMethod(call, result)

        // If user deny permission request pop up will immediately show up.
        // If [retryRequest] is null, the message will only show when call method again
        val retryRequest = call.argument<Boolean>("retryRequest") ?: false
        permissionController.retryRequest = retryRequest

        Log.i(TAG, "onMethodCall: ${call.method}")
        when (call.method) {
            // Permission
            Method.PERMISSION_STATUS -> {
                val hasPermission = permissionController.permissionStatus()
                result.success(hasPermission)
            }

            Method.PERMISSION_REQUEST -> {
                permissionController.requestPermission()
            }

            // Device information
            Method.GET_DEVICE_INFO -> {
                result.success(
                    hashMapOf<String, Any>(
                        "device_model" to Build.MODEL,
                        "device_sys_version" to Build.VERSION.SDK_INT,
                        "device_sys_type" to "Android",
                    )
                )
            }

            // This method will scan the given path to update the state.
            // When deleting a file using 'dart:io', call this method to update the file state
            Method.SCAN -> {
                val sPath: String? = call.argument<String>("path")
                val context = PluginProvider.context()

                // Check if the given file is null or empty.
                if (sPath == null || sPath.isEmpty()) {
                    Log.w(TAG, "onMethodCall: The given path is null or empty")
                    result.success(false)
                }

                // Scan and return.
                MediaScannerConnection.scanFile(context, arrayOf(sPath), null) { _, _ ->
                    Log.i(TAG, "onMethodCall: Scanned $sPath")
                    result.success(true)
                }
            }

            // Logging
            Method.SET_LOG_CONFIG -> {
                // Log level.
                Log.setLogLevel(call.argument<Int>("level")!!)

                // Define if 'warn' level will show more detailed logs.
                PluginProvider.showDetailedLog = call.argument<Boolean>("showDetailedLog")!!
                result.success(true)
            }

            // All other methods
            else -> {
                Log.d(TAG, "Checking permission")
                val hasPermission = permissionController.permissionStatus()
                Log.d(TAG, "Permission status: $hasPermission")
                if (!hasPermission) {
                    Log.w(TAG, "onMethodCall: Permission denied")
                    result.error(
                        "Missing permission",
                        "Application does not have permission to access audio files",
                        "Call the [requestPermission] method to request permission"
                    )
                }
                methodController.find()
            }
        }
        Log.d(TAG, "onMethodCall end")
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Log.i(TAG, "onDetachedFromEngine")
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.i(TAG, "onAttachedToActivity")

        // Init plugin provider with 'activity' and 'context'.
        PluginProvider.set(binding.activity)

        // Add to controller the permission to listen to the request result.
        this.binding = binding
        binding.addRequestPermissionsResultListener(permissionController)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.i(TAG, "onDetachedFromActivityForConfigChanges")
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Log.i(TAG, "onReattachedToActivityForConfigChanges")
        onAttachedToActivity(binding)
    }

    // Detach all parameters.
    override fun onDetachedFromActivity() {
        Log.i(TAG, "onDetachedFromActivity")

        // Remove the permission listener.
        if (binding != null) {
            binding!!.removeRequestPermissionsResultListener(permissionController)
        }

        this.binding = null
        Log.i(TAG, "onDetachedFromActivity end")
    }
}
