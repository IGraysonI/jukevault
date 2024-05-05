import 'package:jukevault_platform_interface/jukevault_platform_interface.dart';

/// Main method to use the [jukevault] plugin.
///
/// Example:
///
/// Init the plugin using:
///
/// ```dart
/// final JukeVault _jukeVault = JukeVault();
/// ```
class JukeVault {
  /// The platform interface that drives this plugin
  static JukevaultPlatform get platform => JukevaultPlatform.instance;

  /// The default path used to store or cache the 'queried' images/artworks.
  ///
  /// **Note: All images are stored inside the app directory or device temporariy
  /// directory, you can use the `path_provider` to get this path.**
  ///
  /// Example:
  ///
  /// ```dart
  /// var appDir = await getApplicationSupportDirectory();
  ///
  /// var appDir = await getTemporaryDirectory();
  ///
  /// var artworksDir = appDir + artworksPath;
  /// ```
  static const String artworksPath = defaultArtworksPath;

  /// Used to delete all artworks cached after using [queryArtwork].
  ///
  /// Note: This method will delete **ONLY** files inside the app directory, all
  /// artworks cached inside the temp folder will be delete automatically after
  /// some time.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `❌` | `✔️` | `❌` | `✔️` | <br>
  ///
  static Future<bool> clearCachedArtworks() async => platform.clearCachedArtworks();

  /// Used to return songs info.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forSongs].
  ///   * If [fromAsset] is null, will be set to false.
  ///
  /// Example:
  ///
  /// * Using await/async:
  ///
  /// ```dart
  /// Future<List<AudioModel>> getAllSongs() async {
  ///  MediaFilter _filter = MediaFilter.forSongs(
  ///    songSortType: SongSortType.TITLE,
  ///    limit: 30,
  ///    orderType: OrderType.ASC_OR_SMALLER,
  ///    uriType: UriType.EXTERNAL,
  ///    ignoreCase: true,
  ///    toQuery: const {},
  ///    toRemove: const {},
  ///    type: const {AudioType.IS_MUSIC: true},
  ///  );
  ///  return await _audioQuery.querySongs(filter: _filter);
  /// }
  /// ```
  ///
  /// * Using [FutureBuilder]: [Plugin example][1]
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<List<AudioModel>> querySongs({
    MediaFilter? filter,
    bool fromAsset = false,
    bool fromAppDir = false,
  }) async =>
      queryAudios(
        filter: filter,
        fromAsset: fromAsset,
        fromAppDir: fromAppDir,
      );

  /// Used to return audios info based in [AudioModel].
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forAudios].
  ///   * If [fromAsset] is null, will be set to false.
  ///
  /// Example:
  ///
  /// * Using await/async:
  ///
  /// ```dart
  /// Future<List<AudioModel>> getAllAudios() async {
  ///  MediaFilter _filter = MediaFilter.forAudios(
  ///    songSortType: SongSortType.TITLE,
  ///    limit: 30,
  ///    orderType: OrderType.ASC_OR_SMALLER,
  ///    uriType: UriType.EXTERNAL,
  ///    ignoreCase: true,
  ///    toQuery: const {},
  ///    toRemove: const {},
  ///    type: const {},
  ///  );
  ///  return await _audioQuery.queryAudios(filter: _filter);
  /// }
  /// ```
  ///
  /// * Using [FutureBuilder]: [Plugin example][1]
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `✔️` | `✔️` | <br>
  ///
  Future<List<AudioModel>> queryAudios({
    MediaFilter? filter,
    bool fromAsset = false,
    bool fromAppDir = false,
  }) async =>
      platform.queryAudios(filter: filter);

  /// Used to observe(listen) the audios changes.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forAudios].
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `✔️` | <br>
  ///
  Stream<List<AudioModel>> observeAudios({MediaFilter? filter}) => platform.observeAudios(filter: filter);

  /// Used to return albums info.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forAlbums].
  ///   * If [fromAsset] is null, will be set to false.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `✔️` | `✔️` | <br>
  ///
  Future<List<AlbumModel>> queryAlbums({
    MediaFilter? filter,
    bool fromAsset = false,
    bool fromAppDir = false,
  }) async =>
      platform.queryAlbums(filter: filter);

  /// Used to observe(listen) the albums changes.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forAlbums].
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `✔️` | <br>
  ///
  Stream<List<AlbumModel>> observeAlbums({MediaFilter? filter}) => platform.observeAlbums(filter: filter);

  /// Used to return artists info.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forArtists].
  ///   * If [fromAsset] is null, will be set to false.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `✔️` | `✔️` | <br>
  ///
  Future<List<ArtistModel>> queryArtists({
    MediaFilter? filter,
    bool fromAsset = false,
    bool fromAppDir = false,
  }) async =>
      platform.queryArtists(filter: filter);

  /// Used to observe(listen) the artists changes.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forArtists].
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `✔️` | <br>
  ///
  Stream<List<ArtistModel>> observeArtists({MediaFilter? filter}) => platform.observeArtists(filter: filter);

  /// Used to return playlists info.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forPlaylists].
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<List<PlaylistModel>> queryPlaylists({MediaFilter? filter}) async => platform.queryPlaylists(filter: filter);

  /// Used to observe(listen) the playlists changes.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forAlbums].
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Stream<List<PlaylistModel>> observePlaylists({MediaFilter? filter}) => platform.observePlaylists(filter: filter);

  /// Used to return genres info.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forGenres].
  ///   * If [fromAsset] is null, will be set to false.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `✔️` | `✔️` | <br>
  ///
  Future<List<GenreModel>> queryGenres({
    MediaFilter? filter,
    bool fromAsset = false,
    bool fromAppDir = false,
  }) async =>
      platform.queryGenres(filter: filter);

  /// Used to observe(listen) the genres changes.
  ///
  /// Important:
  ///   * If [filter] is null, will be used the [MediaFilter.forGenres].
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `✔️` | <br>
  ///
  Stream<List<GenreModel>> observeGenres({MediaFilter? filter}) => platform.observeGenres(filter: filter);

  /// Used to return Songs Artwork.
  ///
  /// Parameters:
  ///
  /// * [type] is used to define if artwork is from audios or albums.
  /// * [format] is used to define type [PNG] or [JPEG].
  /// * [size] is used to define image quality.
  ///
  /// Usage and Performance:
  ///
  /// * Using [PNG] will return a better image quality but a slow performance.
  /// * Using [Size] greater than 200 probably won't make difference in quality but will cause a slow performance.
  ///
  /// Important:
  ///
  /// * This method is only necessary for API >= 29 [Android Q/10].
  /// * If [queryArtwork] is called in Android below Q/10, will return null.
  /// * If [format] is null, will be set to [JPEG] for better performance.
  /// * If [size] is null, will be set to [200] for better performance
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<ArtworkModel?> queryArtwork(
    int id,
    ArtworkType type, {
    MediaFilter? filter,
  }) async =>
      platform.queryArtwork(
        id,
        type,
        filter: filter,
      );

  // Playlist methods

  /// Used to create a Playlist
  ///
  /// Parameters:
  ///
  /// * [name] the playlist name.
  /// * [author] the playlist author. (IOS only)
  /// * [desc] the playlist description. (IOS only)
  ///
  /// Important:
  ///
  /// * This method create a playlist using [External Storage], all apps will be able to see this playlist
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<int?> createPlaylist(
    String name, {
    String? author,
    String? desc,
  }) async =>
      platform.createPlaylist(
        name,
        author: author,
        desc: desc,
      );

  /// Used to remove/delete a Playlist
  ///
  /// Parameters:
  ///
  /// * [playlistId] is used to check if Playlist exist.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> removePlaylist(int playlistId) async => platform.removePlaylist(playlistId);

  /// Used to add a specific song/audio to a specific Playlist
  ///
  /// Parameters:
  ///
  /// * [playlistId] is used to check if Playlist exist.
  /// * [audioId] is used to add specific audio to Playlist.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> addToPlaylist(int playlistId, int audioId) async => platform.addToPlaylist(playlistId, audioId);

  /// Used to remove a specific song/audio from a specific Playlist
  ///
  /// Parameters:
  ///
  /// * [playlistId] is used to check if Playlist exist.
  /// * [audioId] is used to remove specific audio from Playlist.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> removeFromPlaylist(int playlistId, int audioId) async =>
      platform.removeFromPlaylist(playlistId, audioId);

  /// Used to change song/audio position from a specific Playlist
  ///
  /// Parameters:
  ///
  /// * [playlistId] is used to check if Playlist exist.
  /// * [from] is the old position from a audio/song.
  /// * [to] is the new position from a audio/song.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> moveItemTo(int playlistId, int from, int to) async => platform.moveItemTo(playlistId, from, to);

  /// Used to rename a specific Playlist
  ///
  /// Parameters:
  ///
  /// * [playlistId] is used to check if Playlist exist.
  /// * [newName] is used to add a new name to a Playlist.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> renamePlaylist(int playlistId, String newName) async => renamePlaylist(playlistId, newName);

  // Permissions methods

  /// Used to check Android permissions status
  ///
  /// Important:
  ///
  /// * This method will always return a bool.
  /// * If return true `[READ]` and `[WRITE]` permissions is Granted, else `[READ]` and `[WRITE]` is Denied.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> permissionsStatus() async => platform.permissionsStatus();

  /// Used to request Android permissions.
  ///
  /// Important:
  ///
  /// * This method will always return a bool.
  /// * If return true `[READ]` and `[WRITE]` permissions is Granted, else `[READ]` and `[WRITE]` is Denied.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<bool> permissionsRequest() async => platform.permissionsRequest();

  // Device Information

  /// Used to return Device Info
  ///
  /// Will return:
  ///
  /// * Device SDK.
  /// * Device Release.
  /// * Device Code.
  /// * Device Type.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `❌` | <br>
  ///
  Future<DeviceModel> queryDeviceInfo() async => platform.queryDeviceInfo();

  // Others

  /// Used to scan the given [path]
  ///
  /// Will return:
  ///
  /// * A boolean indicating if the path was scanned or not.
  ///
  /// Usage:
  ///
  /// * When using the [Android] platform. After deleting a media using the [dart:io],
  /// call this method to update the media. If the media was successfully and the path
  /// not scanned. Will keep showing on [querySongs].
  ///
  /// Example:
  ///
  /// ```dart
  /// JukeVault _audioQuery = JukeVault();
  /// File file = File('path');
  ///
  /// try {
  ///   if (file.existsSync()) {
  ///     file.deleteSync();
  ///     _audioQuery.scanMedia(file.path); // Scan the media 'path'
  ///   }
  /// } catch (e) {
  ///   debugPrint('$e');
  /// }
  /// ```
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `❌` | `❌` | `❌` | <br>
  ///
  Future<bool> scanMedia(String path) async => platform.scanMedia(path);

  /// Used to check the observers(listeners) status of:
  ///   * [observeAudios]
  ///   * [observeAlbums]
  ///   * [observePlaylists]
  ///   * [observeArtists]
  ///   * [observeGenres]
  ///
  /// Will return:
  ///
  /// * A [ObserversModel], every parameters from this model will return a boolean
  /// indicating if the observers is **running** or not.
  ///
  /// Platforms:
  ///
  /// |`   Android   `|`   IOS   `|`   Web   `|`   Windows   `|
  /// |:----------:|:----------:|:----------:|:----------:|
  /// | `✔️` | `✔️` | `❌` | `✔️` | <br>
  ///
  Future<ObserversModel> observersStatus() async => platform.observersStatus();
}
