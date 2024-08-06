// ignore_for_file: non_constant_identifier_names

import 'package:jukevault_platform_interface/src/filter/columns/album_columns.dart';
import 'package:jukevault_platform_interface/src/filter/columns/artist_columns.dart';
import 'package:jukevault_platform_interface/src/filter/columns/audio_columns.dart';
import 'package:jukevault_platform_interface/src/filter/columns/genre_columns.dart';
import 'package:jukevault_platform_interface/src/filter/columns/playlist_columns.dart';

/// All media columns used with [MediaFilter].
///
/// Example:
///
/// ```dart
/// // Single media column.
/// int mediaColumn = MediaColumns.Audio.TITLE;
///
/// // List of media column.
/// List<int> mediaColumns = [
///   MediaColumns.Audio.TITLE,
///   MediaColumns.Audio.ARTIST,
///   MediaColumns.Audio.ALBUM,
/// ];
/// ```
// TODO: Add 'MediaFilter' link.
abstract class MediaColumns {
  /// All audio columns used with [MediaFilter].
  static AudioColumns get Audio => AudioColumns();

  /// All album columns used with [MediaFilter].
  static AlbumColumns get Album => AlbumColumns();

  /// All artist columns used with [MediaFilter].
  static ArtistColumns get Artist => ArtistColumns();

  /// All playlist columns used with [MediaFilter].
  static PlaylistColumns get Playlist => PlaylistColumns();

  /// All genre columns used with [MediaFilter].
  static GenreColumns get Genre => GenreColumns();
}
