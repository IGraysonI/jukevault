import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'jukevault_platform.dart';

const String _channelName = "com.igraysoni.jukevault_android";
const MethodChannel _channel = MethodChannel(_channelName);

/// An implementation of [JukevaultPlatform] that uses method channels.
class MethodChannelJukevaultPlatformInterface extends JukevaultPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('jukevault_platform_interface');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
