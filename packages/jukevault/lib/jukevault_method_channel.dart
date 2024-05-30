import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'jukevault_platform_interface.dart';

/// An implementation of [JukevaultPlatform] that uses method channels.
class MethodChannelJukevault extends JukevaultPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('jukevault');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
