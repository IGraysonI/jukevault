import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:jukevault_android/jukevault_android.dart';
import 'package:l/l.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _jukevaultAndroidPlugin = JukevaultAndroid();
  String _platformVersion = 'Unknown';
  bool _isPermissionGranted = false;

  @override
  void initState() {
    super.initState();
    _initPlatformState();
    _checkPermissionStatus();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> _initPlatformState() async {
    String platformVersion;

    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion = await _jukevaultAndroidPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() => _platformVersion = platformVersion);
  }

  /// Check media permission status
  Future<void> _checkPermissionStatus() async {
    bool status = false;
    try {
      status = await _jukevaultAndroidPlugin.permissionStatus();
    } on Exception {
      status = false;
      l.e('Failed to get permission status.');
    }
    if (!mounted) return;
    setState(() => _isPermissionGranted = status);
  }

  /// Request media permission and show a snackbar with the result.
  Future<void> _requestPermission(BuildContext context) async {
    bool permissionStatus =
        _isPermissionGranted ? _isPermissionGranted : await _jukevaultAndroidPlugin.requestPermission();
    SnackBar snackBar = SnackBar(
      content: Text(permissionStatus ? 'Permission granted.' : 'Permission denied.'),
      duration: const Duration(seconds: 3),
      backgroundColor: permissionStatus ? Colors.green : Colors.red,
    );
    if (!context.mounted) return;
    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(snackBar);
    setState(() => _isPermissionGranted = permissionStatus);
  }

  @override
  Widget build(BuildContext context) => MaterialApp(
        home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Center(
            child: Column(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text('Running on: $_platformVersion\n'),
                const SizedBox(height: 8),
                Text('Permission status: $_isPermissionGranted\n'),
                const SizedBox(height: 8),
                ElevatedButton(
                  onPressed: () => _requestPermission(context),
                  child: const Text('Request Permission'),
                ),
              ],
            ),
          ),
        ),
      );
}
