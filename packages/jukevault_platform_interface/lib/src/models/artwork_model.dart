import 'dart:typed_data';

import 'package:jukevault_platform_interface/src/models/media_model.dart';

/// [ArtworkModel] that contains all [Image] information.
class ArtworkModel extends MediaModel {
  ArtworkModel(this._info) : super(_info['_id']);

  final Map<dynamic, dynamic> _info;

  /// Return the [artwork]
  Uint8List? get artwork => _info["artwork"];

  /// Return artwork [path].
  String? get path => _info["path"];

  /// Return artwork [extension]
  Stream? get type => _info["type"];

  /// Return a map with all [keys] and [values] from specific artwork.
  Map get getMap => _info;

  ArtworkModel copyWith({
    int? id,
    Uint8List? artwork,
    String? path,
    String? type,
  }) =>
      ArtworkModel({
        "_id": id ?? this.id,
        "artwork": artwork ?? this.artwork,
        "path": path ?? this.path,
        "ext": type ?? this.type,
      });

  @override
  String toString() => '$_info';
}
