// ignore_for_file: constant_identifier_names

part of types_controller;

/// Args types for Audios.
enum AudiosArgs {
  /// Uses song [TITLE] as filter.
  TITLE,

  /// Uses song [DISPLAY_NAME] as filter.
  ///
  /// This arg will only work when using [Android], when using [IOS] will use
  /// [TITLE] as filter.
  DISPLAY_NAME,

  /// Uses song [ALBUM] as filter.
  ALBUM,

  /// Uses song [ARTIST] as filter.
  ARTIST,
}

/// Args types for Albums.
enum AlbumsArgs {
  /// Uses [ALBUM] as filter.
  ALBUM,

  /// Uses album [ARTIST] as filter.
  ARTIST,
}

/// Args types for Playlists.
enum PlaylistsArgs {
  /// Uses [PLAYLIST] as filter.
  PLAYLIST,
}

/// Args types for Artists.
enum ArtistsArgs {
  /// Uses [ARTIST] as filter.
  ARTIST,
}

/// Args types for Genres.
enum GenresArgs {
  /// Uses [GENRE] as filter.
  GENRE,
}
