import 'dart:typed_data';

import 'package:id3/id3.dart';
import 'package:jukevault_platform_interface/src/interface/query_helper_interface.dart';
import 'package:jukevault_platform_interface/src/models/artwork_model.dart';

class QueryHelper extends QueryHelperInterface {
  @override
  Future<MP3Instance> loadMP3(
    String audio, {
    bool? fromAsset,
    bool? fromAppDir,
  }) =>
      throw UnsupportedError('Stub Class');

  /// This method will get all files and paths from [Assets] or [AppDirectory].
  @override
  Future<List<Map<String, Object>>> getFiles({
    bool? fromAsset,
    bool? fromAppDir,
    bool lookSubs = true,
    int? limit,
  }) async =>
      throw UnsupportedError('Stub Class');

  @override
  Future<String?> saveArtworks({
    required int id,
    required Uint8List? artwork,
    required String fileType,
    bool temporary = true,
  }) =>
      throw UnsupportedError('Stub Class');

  @override
  Future<ArtworkModel?> getCachedArtwork({
    required int id,
    bool temporary = true,
  }) =>
      throw UnsupportedError('Stub Class');
}
