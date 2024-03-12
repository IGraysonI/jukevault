import 'dart:async';

import 'package:flutter/services.dart';
import 'package:jukevault_platform_interface/jukevault_platform_interface.dart';

const MethodChannel _channel = MethodChannel('com.example.jukevault_android');

/// An implementation of [OnAudioQueryPlatform] that uses method channels.
class MethodChannelOnAudioQuery extends OnAudioQueryPlatform {
  /// The MethodChannel that is being used by this implementation of the plugin.
  MethodChannel get channel => _channel;

  LogConfig _logConfig = LogConfig();

  @override
  Future<void> setLogConfig(LogConfig? logConfig) async {
    // Override log configuration
    if (logConfig != null) _logConfig = logConfig;
    await _channel.invokeMethod('setLogConfig', {
      'level': _logConfig.logType.value,
      'showDetailedLog': _logConfig.showDetailedLog,
    });
  }

  @override
  Future<List<SongModel>> querySongs({
    SongSortType? sortType,
    OrderType? orderType,
    UriType? uriType,
    bool? ignoreCase,
    String? path,
  }) async {
    final resultSongs = await _channel.invokeMapMethod<dynamic, dynamic>(
      'querySongs',
      {
        'sortType': sortType?.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'uri': uriType != null ? uriType.index : UriType.EXTERNAL.index,
        'ignoreCase': ignoreCase ?? true,
        'path': path,
      },
    );
    // return resultSongs!.values.map(SongModel).toList();
    return [];
  }

  @override
  Future<List<AlbumModel>> queryAlbums({
    AlbumSortType? sortType,
    OrderType? orderType,
    UriType? uriType,
    bool? ignoreCase,
  }) async {
    final resultAlbums = await _channel.invokeMethod(
      'queryAlbums',
      {
        'sortType': sortType?.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'uri': uriType != null ? uriType.index : UriType.EXTERNAL.index,
        'ignoreCase': ignoreCase ?? true,
      },
    ) as List<dynamic>;
    // return resultAlbums.map(AlbumModel).toList();
    return [];
  }

  @override
  Future<List<ArtistModel>> queryArtists({
    ArtistSortType? sortType,
    OrderType? orderType,
    UriType? uriType,
    bool? ignoreCase,
  }) async {
    final resultArtists = await _channel.invokeMethod(
      'queryArtists',
      {
        'sortType': sortType?.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'uri': uriType != null ? uriType.index : UriType.EXTERNAL.index,
        'ignoreCase': ignoreCase ?? true,
      },
    ) as List<dynamic>;
    // return resultArtists.map(ArtistModel).toList();
    return [];
  }

  @override
  Future<List<PlaylistModel>> queryPlaylists({
    PlaylistSortType? sortType,
    OrderType? orderType,
    UriType? uriType,
    bool? ignoreCase,
  }) async {
    final resultPlaylists = await _channel.invokeMethod(
      'queryPlaylists',
      {
        'sortType': sortType?.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'uri': uriType != null ? uriType.index : UriType.EXTERNAL.index,
        'ignoreCase': ignoreCase ?? true,
      },
    ) as List<dynamic>;
    // return resultPlaylists.map(PlaylistModel).toList();
    return [];
  }

  @override
  Future<List<GenreModel>> queryGenres({
    GenreSortType? sortType,
    OrderType? orderType,
    UriType? uriType,
    bool? ignoreCase,
  }) async {
    final resultGenres = await _channel.invokeMethod(
      'queryGenres',
      {
        'sortType': sortType?.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'uri': uriType != null ? uriType.index : UriType.EXTERNAL.index,
        'ignoreCase': ignoreCase ?? true,
      },
    ) as List<dynamic>;
    // return resultGenres.map(GenreModel).toList();
    return [];
  }

  @override
  Future<List<SongModel>> queryAudiosFrom(
    AudiosFromType type,
    Object where, {
    SongSortType? sortType,
    OrderType? orderType,
    bool? ignoreCase,
  }) async {
    final resultSongsFrom = await _channel.invokeMethod(
      'queryAudiosFrom',
      {
        'type': type.index,
        'where': where,
        'sortType': sortType?.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'ignoreCase': ignoreCase ?? true,
      },
    ) as List<dynamic>;
    // return resultSongsFrom.map(SongModel).toList();
    return [];
  }

  @override
  Future<List<dynamic>> queryWithFilters(
    String argsVal,
    WithFiltersType withType,
    args,
  ) async {
    final resultFilters = await _channel.invokeMethod(
      'queryWithFilters',
      {'withType': withType.index, 'args': args.index ?? 0, 'argsVal': argsVal},
    ) as List<dynamic>;
    return resultFilters;
  }

  @override
  Future<Uint8List?> queryArtwork(
    int id,
    ArtworkType type, {
    ArtworkFormat? format,
    int? size,
    int? quality,
  }) async {
    final finalArtworks = await _channel.invokeMethod(
      'queryArtwork',
      {
        'type': type.index,
        'id': id,
        'format': format != null ? format.index : ArtworkFormat.JPEG.index,
        'size': size ?? 200,
        'quality': (quality != null && quality <= 100) ? quality : 50,
      },
    );
    // return finalArtworks;
    return null;
  }

  @override
  Future<List<SongModel>> queryFromFolder(
    String path, {
    SongSortType? sortType,
    OrderType? orderType,
    UriType? uriType,
  }) async {
    final resultFromFolder = await _channel.invokeMethod(
      'queryFromFolder',
      {
        'sortType':
            sortType != null ? sortType.index : SongSortType.TITLE.index,
        'orderType': orderType != null
            ? orderType.index
            : OrderType.ASC_OR_SMALLER.index,
        'uri': uriType != null ? uriType.index : UriType.EXTERNAL.index,
        'path': path
      },
    ) as List<dynamic>;
    // return resultFromFolder.map(SongModel).toList();
    return [];
  }

  @override
  Future<List<String>> queryAllPath() async {
    final resultAllPath = await _channel.invokeMethod(
      'queryAllPath',
    ) as List<dynamic>;
    return resultAllPath.cast<String>();
  }

  @override
  Future<bool> createPlaylist(
    String name, {
    String? author,
    String? desc,
  }) async {
    // final bool resultCreatePl = await _channel.invokeMethod(
    //   'createPlaylist',
    //   {
    //     'playlistName': name,
    //     'playlistAuthor': author,
    //     'playlistDesc': desc,
    //   },
    // );
    // return resultCreatePl;
    return false;
  }

  @override
  Future<bool> removePlaylist(int playlistId) async {
    // final bool resultRemovePl = await _channel.invokeMethod(
    //   'removePlaylist',
    //   {
    //     'playlistId': playlistId,
    //   },
    // );
    // return resultRemovePl;
    return false;
  }

  @override
  Future<bool> addToPlaylist(int playlistId, int audioId) async {
    // final bool resultAddToPl = await _channel.invokeMethod(
    //   'addToPlaylist',
    //   {
    //     'playlistId': playlistId,
    //     'audioId': audioId,
    //   },
    // );
    // return resultAddToPl;
    return false;
  }

  @override
  Future<bool> removeFromPlaylist(int playlistId, int audioId) async {
    // final bool resultRemoveFromPl = await _channel.invokeMethod(
    //   'removeFromPlaylist',
    //   {
    //     'playlistId': playlistId,
    //     'audioId': audioId,
    //   },
    // );
    // return resultRemoveFromPl;
    return false;
  }

  @override
  Future<bool> moveItemTo(int playlistId, int from, int to) async {
    // final bool resultMoveItem = await _channel.invokeMethod(
    //   'moveItemTo',
    //   {
    //     'playlistId': playlistId,
    //     'from': from,
    //     'to': to,
    //   },
    // );
    // return resultMoveItem;
    return false;
  }

  @override
  Future<bool> renamePlaylist(int playlistId, String newName) async {
    // final bool resultRenamePl = await _channel.invokeMethod(
    //   'renamePlaylist',
    //   {
    //     'playlistId': playlistId,
    //     'newPlName': newName,
    //   },
    // );
    // return resultRenamePl;
    return false;
  }

  @override
  Future<bool> permissionsStatus() async {
    // final bool resultStatus = await _channel.invokeMethod('permissionsStatus');
    // return resultStatus;
    return false;
  }

  @override
  Future<bool> permissionsRequest({bool retryRequest = false}) async {
    // final bool resultRequest = await _channel.invokeMethod(
    //   'permissionsRequest',
    //   {
    //     'retryRequest': retryRequest,
    //   },
    // );
    // return resultRequest;
    return false;
  }

  @override
  Future<DeviceModel> queryDeviceInfo() async {
    // final Map deviceResult = await _channel.invokeMethod('queryDeviceInfo');
    // return DeviceModel(deviceResult);
    throw UnimplementedError();
  }

  @override
  Future<bool> scanMedia(String path) async =>
      // await _channel.invokeMethod('scan', {
      //   'path': path,
      // });
      false;
}
