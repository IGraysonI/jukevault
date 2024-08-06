import 'package:jukevault_platform_interface/src/enums/enums.dart';
import 'package:jukevault_platform_interface/src/filter/media_filter.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'jukevault_platform_interface_method_channel.dart';
import 'src/models/models.dart';

// Constants
export 'src/const/constants.dart';
// Other
export 'src/enums/enums.dart';
export 'src/enums/sort_types/sort_types.dart';
export 'src/filter/columns/media_columns.dart';
// Internal methods
export 'src/filter/media_filter.dart';
// Interface
export 'src/interface/query_helper_interface.dart';
export 'src/models/models.dart';

abstract class JukevaultPlatform extends PlatformInterface {
  /// Constructs a JukevaultPlatformInterfacePlatform.
  JukevaultPlatform() : super(token: _token);

  static final Object _token = Object();

  static JukevaultPlatform _instance = MethodChannelJukevaultPlatformInterface();

  /// The default instance of [JukevaultPlatform] to use.
  ///
  /// Defaults to [MethodChannelJukevaultPlatformInterface].
  static JukevaultPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [JukevaultPlatform] when
  /// they register themselves.
  static set instance(JukevaultPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() => throw UnimplementedError('platformVersion() has not been implemented.');

  /// Delete all artwork cached after using [queryArtwork]
  Future<bool> clearCachedArtworks() => throw UnimplementedError('clearCachedArtwork() has not been implemented.');

  /// Query audios from the media store with optional [MediaFilter] filter
  Future<List<AudioModel>> queryAudios({MediaFilter? filter}) =>
      throw UnimplementedError('queryAudios() has not been implemented.');

  /// Query albums from the media store with optional [MediaFilter] filter
  Future<List<AlbumModel>> queryAlbums({MediaFilter? filter}) =>
      throw UnimplementedError('queryAlbums() has not been implemented.');

  /// Query artists from the media store with optional [MediaFilter] filter
  Future<List<ArtistModel>> queryArtists({MediaFilter? filter}) =>
      throw UnimplementedError('queryArtists() has not been implemented.');

  /// Query playlists from the media store with optional [MediaFilter] filter
  Future<List<PlaylistModel>> queryPlaylists({MediaFilter? filter}) =>
      throw UnimplementedError('queryPlaylists() has not been implemented.');

  /// Query genres from the media store with optional [MediaFilter] filter
  Future<List<GenreModel>> queryGenres({MediaFilter? filter}) =>
      throw UnimplementedError('queryGenres() has not been implemented.');

  /// Query to return songs artwork
  Future<ArtworkModel?> queryArtwork(
    int id,
    ArtworkTypeEnum type, {
    bool? fromAsset,
    bool? fromAppDir,
    MediaFilter? filter,
  }) =>
      throw UnimplementedError('queryArtwork() has not been implemented.');

  /// ------ Playlist methods ------

  /// Create a new playlist
  Future<int?> createPlaylist(
    String name, {
    String? author,
    String? description,
  }) =>
      throw UnimplementedError('createPlaylist() has not been implemented.');

  /// Delete a playlist
  Future<bool> deletePlaylist(int playlistId) => throw UnimplementedError('deletePlaylist() has not been implemented.');

  /// Add a song to a playlist
  Future<bool> addToPlaylist(int playlistId, int audioId) =>
      throw UnimplementedError('addToPlaylist() has not been implemented.');

  /// Remove a song from a playlist
  Future<bool> removeFromPlaylist(int playlistId, int audioId) =>
      throw UnimplementedError('removeFromPlaylist() has not been implemented.');

  /// Move a song to a new position in a playlist
  Future<bool> moveItemTo(int playlistId, int from, int to) =>
      throw UnimplementedError('moveItem() has not been implemented.');

  /// Rename a playlist
  Future<bool> renamePlaylist(int playlistId, String name) =>
      throw UnimplementedError('renamePlaylist() has not been implemented.');

  /// ------ Permission methods ------

  /// Get the permission status of the app
  Future<bool> permissionStatus() => throw UnimplementedError('permissionStatus() has not been implemented.');

  /// Request permission to access the media store
  Future<bool> requestPermission() => throw UnimplementedError('requestPermission() has not been implemented.');
}
