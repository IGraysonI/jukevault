package com.igraysoni.jukevault_android

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import com.igraysoni.jukevault_android.controller.PermissionController
import com.igraysoni.jukevault_android.controller.QueryController

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** JukevaultAndroidPlugin */
class JukevaultAndroidPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  companion object {
    // Current class name
    private val TAG: String = this::class.java.name

    // Method channel name
    private const val CHANNEL_NAME = "com.igraysoni.jukevault_android"
  }

  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var permissionController: PermissionController
  private lateinit var queryController: QueryController

  // Main parameters
  private var activity: Activity? = null
  private var binding: ActivityPluginBinding? = null


  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    // Define the [context]
    context = flutterPluginBinding.applicationContext

    // Setup the method channel communication
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    this.queryController = QueryController(context, call, result)

    // Both [activity] and [binding] are from [onAttachedToActivity]
    // If one of them are null, then something is really wrong
    if (activity == null || binding == null) {
      result.error(
        "$TAG::onMethodCall",
        "The [activity] or [binding] is null",
        null
      )
    }

    // If user denies permission pop up will immediately shows up.
    // If [retryRequest] is null, the message will only show after calling the method again.
    val retryRequest = call.argument<Boolean>("retryRequest") ?: false

    // Setup the [permissionController]
    permissionController = PermissionController(retryRequest)

    when (call.method) {
      "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
      "permissionStatus" -> result.success(permissionController.permissionStatus(context))
      "permissionRequest" -> {
        // Add to controller the ability to listen the permission request result
        binding!!.addRequestPermissionsResultListener(permissionController)

        // Request permission
        permissionController.permissionRequest(activity!!, result)
      }
      else -> queryController.call()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) =
    channel.setMethodCallHandler(null)


  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.activity = binding.activity
    this.binding = binding
  }

  override fun onDetachedFromActivityForConfigChanges() = onDetachedFromActivity()

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) =
    onAttachedToActivity(binding)

  override fun onDetachedFromActivity() {
    if (binding != null) {
      binding!!.removeRequestPermissionsResultListener(permissionController)
    }
    this.activity = null
    this.binding = null
  }
}
