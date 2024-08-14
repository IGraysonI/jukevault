import 'dart:async';

import 'package:flutter/services.dart';

import 'jukevault_platform_interface.dart';

const String _channelName = 'com.igraysonl.jukevault';
const MethodChannel _channel = MethodChannel(_channelName);

/// An implementation of [JukevaultPlatformInterface] that uses method channels.
class JukevaultPlatformInterfaceMethodChannel extends JukevaultPlatformInterface {
  /// The MethodChannel that is being used by this implementation of the plugin.
  MethodChannel get channel => _channel;

  /// Default filter for all methods.
  static final MediaFilter _defaultFilter = MediaFilter.init();

  @override
  Future<bool> clearCachedArtworks() async => await _channel.invokeMethod('clearCachedArtworks');

  @override
  Future<List<AudioModel>> queryAudios({
    MediaFilter? filter,
  }) async {
    // If the filter is null, use the 'default'.
    filter ??= _defaultFilter;

    // Fix the 'type' filter.
    //
    // Convert the 'AudioType' into 'int'.
    // The 'true' and 'false' value into '1' or '2'.
    Map<int, int> fixedMap = {};
    filter.type.forEach((key, value) {
      //
      fixedMap[key.index] = value == true ? 1 : 0;
    });

    // Invoke the channel.
    final List<dynamic> resultSongs = await _channel.invokeMethod(
      "queryAudios",
      {
        "sortType": filter.audioSortType?.index,
        "orderType": filter.orderType.index,
        "uri": filter.uriType.index,
        "ignoreCase": filter.ignoreCase,
        "toQuery": filter.toQuery,
        "toRemove": filter.toRemove,
        "type": fixedMap,
        "limit": filter.limit,
      },
    );

    // Convert the result into a list of [SongModel] and return.
    return resultSongs.map((e) => AudioModel(e)).toList();
  }

  @override
  Future<List<AlbumModel>> queryAlbums({
    MediaFilter? filter,
  }) async {
    // If the filter is null, use the 'default'.
    filter ??= _defaultFilter;

    // Invoke the channel.
    final List<dynamic> resultAlbums = await _channel.invokeMethod(
      "queryAlbums",
      {
        "sortType": filter.albumSortType?.index,
        "orderType": filter.orderType.index,
        "uri": filter.uriType.index,
        "ignoreCase": filter.ignoreCase,
        "toQuery": filter.toQuery,
        "toRemove": filter.toRemove,
        "limit": filter.limit,
      },
    );

    // Convert the result into a list of [AlbumModel] and return.
    return resultAlbums.map((albumInfo) => AlbumModel(albumInfo)).toList();
  }

  @override
  Future<List<ArtistModel>> queryArtists({
    MediaFilter? filter,
  }) async {
    // If the filter is null, use the 'default'.
    filter ??= _defaultFilter;

    // Invoke the channel.
    final List<dynamic> resultArtists = await _channel.invokeMethod(
      "queryArtists",
      {
        "sortType": filter.albumSortType?.index,
        "orderType": filter.orderType.index,
        "uri": filter.uriType.index,
        "ignoreCase": filter.ignoreCase,
        "toQuery": filter.toQuery,
        "toRemove": filter.toRemove,
        "limit": filter.limit,
      },
    );

    // Convert the result into a list of [ArtistModel] and return.
    return resultArtists.map((artistInfo) => ArtistModel(artistInfo)).toList();
  }

  @override
  Future<List<PlaylistModel>> queryPlaylists({
    MediaFilter? filter,
  }) async {
    // If the filter is null, use the 'default'.
    filter ??= _defaultFilter;

    // Invoke the channel.
    final List<dynamic> resultPlaylists = await _channel.invokeMethod(
      "queryPlaylists",
      {
        "sortType": filter.albumSortType?.index,
        "orderType": filter.orderType.index,
        "uri": filter.uriType.index,
        "ignoreCase": filter.ignoreCase,
        "toQuery": filter.toQuery,
        "toRemove": filter.toRemove,
        "limit": filter.limit,
      },
    );

    // Convert the result into a list of [PlaylistModel] and return.
    return resultPlaylists.map((playlistInfo) => PlaylistModel(playlistInfo)).toList();
  }

  @override
  Future<List<GenreModel>> queryGenres({
    MediaFilter? filter,
  }) async {
    // If the filter is null, use the 'default'.
    filter ??= _defaultFilter;

    // Invoke the channel.
    final List<dynamic> resultGenres = await _channel.invokeMethod(
      "queryGenres",
      {
        "sortType": filter.albumSortType?.index,
        "orderType": filter.orderType.index,
        "uri": filter.uriType.index,
        "ignoreCase": filter.ignoreCase,
        "toQuery": filter.toQuery,
        "toRemove": filter.toRemove,
        "limit": filter.limit,
      },
    );

    // Convert the result into a list of [GenreModel] and return.
    return resultGenres.map((genreInfo) => GenreModel(genreInfo)).toList();
  }

  @override
  Future<ArtworkModel?> queryArtwork(
    int id,
    ArtworkType type, {
    bool? fromAsset,
    bool? fromAppDir,
    MediaFilter? filter,
  }) async {
    // If the filter is null, use the 'default'.
    filter ??= _defaultFilter;

    //
    return _channel.invokeMethod(
      "queryArtwork",
      {
        "type": type.index,
        "id": id,
        "format": filter.artworkFormat?.index ?? ArtworkFormatType.JPEG.index,
        "size": filter.artworkSize ?? 100,
        "quality": (filter.artworkQuality != null && filter.artworkQuality! <= 100) ? filter.artworkQuality : 50,
        "cacheArtwork": filter.cacheArtwork ?? true,
        "cacheTemporarily": filter.cacheTemporarily ?? true,
        "overrideCache": filter.overrideCache ?? false,
      },
    ).then(
      (resultArtwork) => ArtworkModel(resultArtwork),
    );
  }

  @override
  Future<int?> createPlaylist(
    String name, {
    String? author,
    String? desc,
  }) async =>
      await _channel.invokeMethod(
        "createPlaylist",
        {
          "playlistName": name,
          "playlistAuthor": author,
          "playlistDesc": desc,
        },
      );

  @override
  Future<bool> deletePlaylist(int playlistId) async => await _channel.invokeMethod(
        "deletePlaylist",
        {
          "playlistId": playlistId,
        },
      );

  @override
  Future<bool> addToPlaylist(
    int playlistId,
    int audioId,
  ) async =>
      await _channel.invokeMethod(
        "addToPlaylist",
        {
          "playlistId": playlistId,
          "audioId": audioId,
        },
      );

  @override
  Future<bool> removeFromPlaylist(
    int playlistId,
    int audioId,
  ) async =>
      await _channel.invokeMethod(
        "removeFromPlaylist",
        {
          "playlistId": playlistId,
          "audioId": audioId,
        },
      );

  @override
  Future<bool> moveItemTo(
    int playlistId,
    int from,
    int to,
  ) async =>
      await _channel.invokeMethod(
        "moveItemTo",
        {
          "playlistId": playlistId,
          "from": from,
          "to": to,
        },
      );

  @override
  Future<bool> renamePlaylist(
    int playlistId,
    String newName,
  ) async =>
      await _channel.invokeMethod(
        "renamePlaylist",
        {
          "playlistId": playlistId,
          "newPlName": newName,
        },
      );

  @override
  Future<bool> permissionsStatus() async => await _channel.invokeMethod("permissionsStatus");

  @override
  Future<bool> permissionsRequest() async => await _channel.invokeMethod("permissionsRequest");

  @override
  Future<DeviceModel> queryDeviceInfo() async =>
      _channel.invokeMethod("queryDeviceInfo").then((deviceResult) => DeviceModel(deviceResult));

  @override
  Future<bool> scanMedia(String path) async => await _channel.invokeMethod('scan', {"path": path});
}
