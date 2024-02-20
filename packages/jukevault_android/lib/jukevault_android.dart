
import 'jukevault_android_platform_interface.dart';

class JukevaultAndroid {
  Future<String?> getPlatformVersion() {
    return JukevaultAndroidPlatform.instance.getPlatformVersion();
  }
}
