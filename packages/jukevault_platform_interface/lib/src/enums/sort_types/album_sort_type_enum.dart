// ignore_for_file: constant_identifier_names
part of 'sort_types.dart';

/// Defines the sort type used for [queryAlbums].
enum AlbumSortTypeEnum {
  /// [ALBUM] will return album list based in [album] names.
  ALBUM,

  /// [ARTIST] will return album list based in [artist] names.
  ARTIST,

  /// [NUM_OF_SONGS] will return album list based in [number_of_songs].
  NUM_OF_SONGS,
}
