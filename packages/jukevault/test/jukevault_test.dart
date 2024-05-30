import 'package:flutter_test/flutter_test.dart';
import 'package:jukevault/jukevault.dart';
import 'package:jukevault/jukevault_platform_interface.dart';
import 'package:jukevault/jukevault_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockJukevaultPlatform
    with MockPlatformInterfaceMixin
    implements JukevaultPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final JukevaultPlatform initialPlatform = JukevaultPlatform.instance;

  test('$MethodChannelJukevault is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelJukevault>());
  });

  test('getPlatformVersion', () async {
    Jukevault jukevaultPlugin = Jukevault();
    MockJukevaultPlatform fakePlatform = MockJukevaultPlatform();
    JukevaultPlatform.instance = fakePlatform;

    expect(await jukevaultPlugin.getPlatformVersion(), '42');
  });
}
