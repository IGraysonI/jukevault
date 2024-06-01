import 'jukevault_android_platform_interface.dart';

class JukevaultAndroid {
  Future<String?> getPlatformVersion() => JukevaultAndroidPlatform.instance.getPlatformVersion();

  Future<bool> permissionStatus() => JukevaultAndroidPlatform.instance.permissionStatus();

  Future<bool> requestPermission() => JukevaultAndroidPlatform.instance.requestPermission();
}
