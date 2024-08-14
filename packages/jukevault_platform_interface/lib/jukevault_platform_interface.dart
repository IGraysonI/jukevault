import 'dart:async';

import 'package:jukevault_platform_interface/src/controllers/types_controller.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'jukevault_platform_interface_method_channel.dart';
import 'src/controllers/models_controller.dart';
import 'src/filter/media_filter.dart';

// External packages
export 'package:id3/id3.dart';
export 'package:path/path.dart';
export 'package:path_provider/path_provider.dart';

// Constants
export 'src/constants.dart';
export 'src/controllers/models_controller.dart';
export 'src/controllers/sorts_controller.dart';
export 'src/controllers/types_controller.dart';
export 'src/filter/columns/media_columns.dart';
// Internal methods/classes
export 'src/filter/media_filter.dart';
// Interfaces
export 'src/interfaces/query_helper_interface.dart';

/// The interface that implementations of jukevault must implement.
///
/// Platform implementations should extend this class rather than implement it as `jukevault`
/// does not consider newly added methods to be breaking changes. Extending this class
/// (using `extends`) ensures that the subclass will get the default implementation, while
/// platform implementations that `implements` this interface will be broken by newly added
/// [JukevaultPlatformInterface] methods.
abstract class JukevaultPlatformInterface extends PlatformInterface {
  /// Constructs a JukevaultPlatformInterface.
  JukevaultPlatformInterface() : super(token: _token);

  static final Object _token = Object();

  static JukevaultPlatformInterface _instance = JukevaultPlatformInterfaceMethodChannel();

  /// The default instance of [JukevaultPlatformInterface] to use.
  ///
  /// Defaults to [JukevaultPlatformInterfaceMethodChannel].
  static JukevaultPlatformInterface get instance => _instance;

  /// Platform-specific plugins should set this with their own platform-specific
  /// class that extends [JukevaultPlatformInterface] when they register themselves.
  static set instance(JukevaultPlatformInterface instance) {
    PlatformInterface.verify(instance, _token);
    _instance = instance;
  }

  /// Used to delete all artworks cached after using [queryArtwork].
  Future<bool> clearCachedArtworks() => throw UnimplementedError('clearCachedArtworks() has not been implemented.');

  /// Used to return Audios Info based in [AudioModel].
  Future<List<AudioModel>> queryAudios({MediaFilter? filter}) =>
      throw UnimplementedError('queryAudios() has not been implemented.');

  /// Used to return Albums Info based in [AlbumModel].
  Future<List<AlbumModel>> queryAlbums({MediaFilter? filter}) =>
      throw UnimplementedError('queryAlbums() has not been implemented.');

  /// Used to return Artists Info based in [ArtistModel].
  Future<List<ArtistModel>> queryArtists({MediaFilter? filter}) =>
      throw UnimplementedError('queryArtists() has not been implemented.');

  /// Used to return Playlists Info based in [PlaylistModel].
  Future<List<PlaylistModel>> queryPlaylists({MediaFilter? filter}) =>
      throw UnimplementedError('queryPlaylists() has not been implemented.');

  /// Used to return Genres Info based in [GenreModel].
  Future<List<GenreModel>> queryGenres({MediaFilter? filter}) =>
      throw UnimplementedError('queryGenres() has not been implemented.');

  /// Used to return Songs Artwork.
  Future<ArtworkModel?> queryArtwork(
    int id,
    ArtworkType type, {
    bool? fromAsset,
    bool? fromAppDir,
    MediaFilter? filter,
  }) =>
      throw UnimplementedError('queryArtwork() has not been implemented.');

  // ------- Playlist Methods -------

  /// Used to create a Playlist.
  Future<int?> createPlaylist(
    String name, {
    String? author,
    String? desc,
  }) =>
      throw UnimplementedError('createPlaylist() has not been implemented.');

  /// Used to remove/delete a Playlist.
  Future<bool> deletePlaylist(int playlistId) => throw UnimplementedError('deletePlaylist() has not been implemented.');

  /// Used to add a specific song/audio to a specific Playlist.
  Future<bool> addToPlaylist(int playlistId, int audioId) =>
      throw UnimplementedError('addToPlaylist() has not been implemented.');

  /// Used to remove a specific song/audio from a specific Playlist.
  Future<bool> removeFromPlaylist(int playlistId, int audioId) =>
      throw UnimplementedError('removeFromPlaylist() has not been implemented.');

  /// Used to change song/audio position from a specific Playlist.
  Future<bool> moveItemTo(int playlistId, int from, int to) =>
      throw UnimplementedError('moveItemTo() has not been implemented.');

  /// Used to rename a specific Playlist.
  Future<bool> renamePlaylist(int playlistId, String newName) =>
      throw UnimplementedError('renamePlaylist() has not been implemented.');

  // ------- Permissions Methods -------

  /// Used to check Android permissions status.
  Future<bool> permissionsStatus() => throw UnimplementedError('permissionsStatus() has not been implemented.');

  /// Used to request Android permissions.
  Future<bool> permissionsRequest() => throw UnimplementedError('permissionsRequest() has not been implemented.');

  // ------- Device Info Methods -------

  /// Used to return Device Info.
  Future<DeviceModel> queryDeviceInfo() => throw UnimplementedError('queryDeviceInfo() has not been implemented.');

  // ------- Others Methods -------

  /// Used to scan the given [path].
  Future<bool> scanMedia(String path) => throw UnimplementedError('scanMedia() has not been implemented.');
}
