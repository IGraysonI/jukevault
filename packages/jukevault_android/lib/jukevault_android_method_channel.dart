import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'jukevault_android_platform_interface.dart';

/// An implementation of [JukevaultAndroidPlatform] that uses method channels.
class MethodChannelJukevaultAndroid extends JukevaultAndroidPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('com.igraysoni.jukevault_android');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool> permissionStatus() async {
    final status = await methodChannel.invokeMethod<bool>('permissionStatus');
    return status ?? false;
  }

  @override
  Future<bool> requestPermission() async {
    final status = await methodChannel.invokeMethod<bool>('permissionRequest');
    return status ?? false;
  }
}
