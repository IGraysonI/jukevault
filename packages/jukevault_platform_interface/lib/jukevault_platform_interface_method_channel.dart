import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'jukevault_platform_interface_platform_interface.dart';

/// An implementation of [JukevaultPlatformInterfacePlatform] that uses method channels.
class MethodChannelJukevaultPlatformInterface extends JukevaultPlatformInterfacePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('jukevault_platform_interface');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
