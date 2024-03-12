/// [DeviceModel] that contains all [Device] Information.
class DeviceModel {
  DeviceModel(this._info);

  // The type dynamic is used for both but, the map is always based in [String, dynamic]
  final Map<dynamic, dynamic> _info;

  /// Return device [model]
  String get model => _info['device_model'] as String;

  /// Return device [type]
  String get type => _info['device_sys_type'] as String;

  /// Return device [version]
  int get version => _info['device_sys_version'] as int;

  /// Return a map with all [keys] and [values] from device.
  Map<dynamic, dynamic> get getMap => _info;

  @override
  String toString() => _info.toString();
}
