import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'jukevault_android_method_channel.dart';

abstract class JukevaultAndroidPlatform extends PlatformInterface {
  /// Constructs a JukevaultAndroidPlatform.
  JukevaultAndroidPlatform() : super(token: _token);

  static final Object _token = Object();

  static JukevaultAndroidPlatform _instance = MethodChannelJukevaultAndroid();

  /// The default instance of [JukevaultAndroidPlatform] to use.
  ///
  /// Defaults to [MethodChannelJukevaultAndroid].
  static JukevaultAndroidPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [JukevaultAndroidPlatform] when
  /// they register themselves.
  static set instance(JukevaultAndroidPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
