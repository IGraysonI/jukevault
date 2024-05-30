import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'jukevault_method_channel.dart';

abstract class JukevaultPlatform extends PlatformInterface {
  /// Constructs a JukevaultPlatform.
  JukevaultPlatform() : super(token: _token);

  static final Object _token = Object();

  static JukevaultPlatform _instance = MethodChannelJukevault();

  /// The default instance of [JukevaultPlatform] to use.
  ///
  /// Defaults to [MethodChannelJukevault].
  static JukevaultPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [JukevaultPlatform] when
  /// they register themselves.
  static set instance(JukevaultPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
