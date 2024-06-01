package com.igraysoni.jukevault_android.controller

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

interface PermissionManagerInterface {
    /** Check permission status and return Boolean as result */
    fun permissionStatus(context: Context) : Boolean
    /** Request permission for media files */
    fun permissionRequest(activity: Activity, result: MethodChannel.Result)
    /** Retry request permission, this method has "Never Ask Again" option */
    fun permissionRetryRequest()
}

/** Controller for permission management */
class PermissionController(
    private var retryRequest: Boolean = false,
) : PermissionManagerInterface ,PluginRegistry.RequestPermissionsResultListener{
    private lateinit var activity: Activity
    private lateinit var result: MethodChannel.Result
    private val requestCode: Int = 88569
    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    // After "leaving" this class, context will be null so, we need this context argument to
    // call the [checkSelfPermission].
    override fun permissionStatus(context: Context): Boolean = permissions.all {
        return ContextCompat.checkSelfPermission(
            context,
            it,
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun permissionRequest(activity: Activity, result: MethodChannel.Result) {
        this.activity = activity
        this.result = result
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    override fun permissionRetryRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])
            || ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[1])) {
            retryRequest = false
            if (this::activity.isInitialized && this::result.isInitialized) {
                permissionRequest(activity, result)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        // When result is not initialized the permission request did not originate from the
        // plugin, we return [false] to indicate the  plugin is not handling the request result
        // and Android should continue executing other registered handlers.
        if (!this::result.isInitialized) return false

        // When the incoming request code doesn't match the request codes defined by the plugin
        // return [false] to indicate the plugin is not handling the request result and Android
        // should continue executing other registered handlers.
        if (requestCode != this.requestCode) return false

        // Check permission
        val isPermissionGranted = (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)

        // After all checks we can handle the permission request
        when {
            isPermissionGranted -> result.success(true)
            retryRequest -> permissionRetryRequest()
            else -> result.success(false)
        }

        // Return [true] to indicate the plugin handled the permission request result and Android
        // should stop executing other registered handlers.
        return true
    }
}