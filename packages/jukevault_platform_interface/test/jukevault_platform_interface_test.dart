import 'package:flutter_test/flutter_test.dart';
import 'package:jukevault_platform_interface/jukevault_platform_interface.dart';
import 'package:jukevault_platform_interface/jukevault_platform_interface_platform_interface.dart';
import 'package:jukevault_platform_interface/jukevault_platform_interface_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockJukevaultPlatformInterfacePlatform
    with MockPlatformInterfaceMixin
    implements JukevaultPlatformInterfacePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final JukevaultPlatformInterfacePlatform initialPlatform = JukevaultPlatformInterfacePlatform.instance;

  test('$MethodChannelJukevaultPlatformInterface is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelJukevaultPlatformInterface>());
  });

  test('getPlatformVersion', () async {
    JukevaultPlatformInterface jukevaultPlatformInterfacePlugin = JukevaultPlatformInterface();
    MockJukevaultPlatformInterfacePlatform fakePlatform = MockJukevaultPlatformInterfacePlatform();
    JukevaultPlatformInterfacePlatform.instance = fakePlatform;

    expect(await jukevaultPlatformInterfacePlugin.getPlatformVersion(), '42');
  });
}
