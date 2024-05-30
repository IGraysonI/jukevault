import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'jukevault_platform_interface_method_channel.dart';

abstract class JukevaultPlatformInterfacePlatform extends PlatformInterface {
  /// Constructs a JukevaultPlatformInterfacePlatform.
  JukevaultPlatformInterfacePlatform() : super(token: _token);

  static final Object _token = Object();

  static JukevaultPlatformInterfacePlatform _instance = MethodChannelJukevaultPlatformInterface();

  /// The default instance of [JukevaultPlatformInterfacePlatform] to use.
  ///
  /// Defaults to [MethodChannelJukevaultPlatformInterface].
  static JukevaultPlatformInterfacePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [JukevaultPlatformInterfacePlatform] when
  /// they register themselves.
  static set instance(JukevaultPlatformInterfacePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
