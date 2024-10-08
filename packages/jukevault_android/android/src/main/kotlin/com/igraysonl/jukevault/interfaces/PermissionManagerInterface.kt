package com.igraysonl.jukevault.interfaces

import android.app.Activity
import android.content.Context
import io.flutter.plugin.common.MethodChannel

/** PermissionManagerInterface */
interface PermissionManagerInterface {
    fun permissionStatus(context: Context) : Boolean
    fun requestPermission(activity: Activity, result: MethodChannel.Result)
    fun retryRequestPermission()
}