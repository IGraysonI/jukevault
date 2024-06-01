import 'package:flutter_test/flutter_test.dart';
import 'package:jukevault_android/jukevault_android.dart';
import 'package:jukevault_android/jukevault_android_method_channel.dart';
import 'package:jukevault_android/jukevault_android_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockJukevaultAndroidPlatform with MockPlatformInterfaceMixin implements JukevaultAndroidPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<bool> permissionStatus() {
    // TODO: implement permissionStatus
    throw UnimplementedError();
  }

  @override
  Future<bool> requestPermission() {
    // TODO: implement requestPermission
    throw UnimplementedError();
  }
}

void main() {
  final JukevaultAndroidPlatform initialPlatform = JukevaultAndroidPlatform.instance;

  test('$MethodChannelJukevaultAndroid is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelJukevaultAndroid>());
  });

  test('getPlatformVersion', () async {
    JukevaultAndroid jukevaultAndroidPlugin = JukevaultAndroid();
    MockJukevaultAndroidPlatform fakePlatform = MockJukevaultAndroidPlatform();
    JukevaultAndroidPlatform.instance = fakePlatform;

    expect(await jukevaultAndroidPlugin.getPlatformVersion(), '42');
  });
}
