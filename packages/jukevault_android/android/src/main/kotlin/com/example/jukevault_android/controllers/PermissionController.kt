package com.example.jukevault_android.controllers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jukevault_android.providers.PluginProvider
import io.flutter.Log
import io.flutter.plugin.common.PluginRegistry

interface PermissionManagerInterface {
    fun permissionStatus(): Boolean
    fun requestPermission()

    fun retryRequestPermission()
}

class PermissionController : PermissionManagerInterface,
    PluginRegistry.RequestPermissionsResultListener {

    companion object {
        // Get the current class name.
        private const val TAG: String = "PermissionController"

        // Request code for the permission request.
        private const val REQUEST_CODE: Int = 88560
    }

    var retryRequest: Boolean = false

    // Permission for specific android versions.
    private var permissions: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    override fun permissionStatus(): Boolean = permissions.all {
        // After "leaving" this class, context will be null. We need this context argument
        // to call the [checkSelfPermission] method.
        return ContextCompat.checkSelfPermission(
            PluginProvider.context(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission() {
        Log.d(TAG, "Requesting permission")
        Log.d(TAG, "SDK:: ${Build.VERSION.SDK_INT}, Should retry request: $retryRequest")
        val activity = PluginProvider.activity()
        ActivityCompat.requestPermissions(
            activity,
            permissions,
            REQUEST_CODE
        )
    }

    /// Second requestPermission, this one with the option "Never Ask Again".
    override fun retryRequestPermission() {
        val activity = PluginProvider.activity()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])
            || ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[1])
        ) {
            Log.d(TAG, "Retrying permission request")
            retryRequest = false
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        // When the incoming request code doesn't match the request code defined by the plugin
        // return [false] to indicate the plugin is not handling the request result and
        // Android should continue executing other registered handlers.
        if (REQUEST_CODE != requestCode) return false

        // Check permission
        val isPermissionGranted =
            (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)

        Log.d(TAG, "Permission granted: $isPermissionGranted")

        // After all checks, we can handle the permission request.
        val result = PluginProvider.result()
        when {
            isPermissionGranted -> result.success(true)
            retryRequest -> retryRequestPermission()
            else -> result.success(false)
        }

        // Return [true] here to indicate that the plugin handled the permission request result
        // and Android should not continue executing other registered handlers.
        return true
    }
}


