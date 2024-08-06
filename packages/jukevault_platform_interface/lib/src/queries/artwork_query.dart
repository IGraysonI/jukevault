import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:id3/id3.dart';
import 'package:jukevault_platform_interface/src/enums/artwork_type_enum.dart';
import 'package:jukevault_platform_interface/src/filter/media_filter.dart';
import 'package:jukevault_platform_interface/src/queries/query_helper.dart';

import '../models/models.dart';

class ArtworkQuery {
  final QueryHelper _helper = QueryHelper();
  static final MediaFilter _defaultFilter = MediaFilter.forArtwork();

  Future<ArtworkModel?> queryArtwork(
    List<AudioModel> audios,
    int id,
    ArtworkTypeEnum type, {
    bool? fromAsset,
    bool? fromAppDir,
    MediaFilter? filter,
  }) async {
    filter ??= _defaultFilter;
    if (!(filter.overrideCache ?? false)) {
      ArtworkModel? cache = await _helper.getCachedArtwork(
        id: id,
        temporary: filter.cacheTemporarily ?? true,
      );
      return cache;
    }

    try {
      AudioModel audio = audios.singleWhere(
        (audio) => id == audio.id || id == audio.albumId || id == audio.artistId || id == audio.genreId,
      );
      MP3Instance mp3instance = await _helper.loadMP3(
        audio.data,
        fromAsset: fromAsset,
        fromAppDir: fromAppDir,
      );
      if (mp3instance.parseTagsSync()) {
        String? artwork = mp3instance.getMetaTags()?["APIC"]["base64"];
        if (artwork == null) return null;
        String? fileType;
        if (artwork.startsWith('iVBORw0KGgo')) {
          fileType = '.png';
        } else if (artwork.startsWith('/9j')) {
          fileType = '.jpeg';
        }
        if (fileType == null) return null;
        Uint8List artAsByte = base64Decode(artwork);
        String? path;
        if (filter.cacheArtwork ?? true) {
          path = await _helper.saveArtworks(
            id: id,
            artwork: artAsByte,
            fileType: fileType,
            temporary: filter.cacheTemporarily ?? true,
          );
        }
        return ArtworkModel({
          '_id': id,
          'artwork': artAsByte,
          'path': path,
          'type': fileType,
        });
      }
    } catch (e) {
      // Do nothing.
    }
    return null;
  }
}
