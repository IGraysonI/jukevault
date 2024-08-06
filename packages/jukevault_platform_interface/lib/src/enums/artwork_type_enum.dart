// ignore_for_file: constant_identifier_names

part of 'enums.dart';

/// Defines where artwork will be acquired.
enum ArtworkTypeEnum {
  /// Artwork from Audios.
  AUDIO,

  /// Artwork from Albums.
  ALBUM,

  /// Artwork from Playlists.
  ///
  /// Important:
  ///
  /// * The artwork will be the artwork from the first audio inside the playlist.
  PLAYLIST,

  /// Artwork from Artists.
  ///
  /// Important:
  ///
  /// * There's no native support for [Artists] artwork so, we take the artwork from
  /// the first audio.
  ARTIST,

  /// Artwork from Genres.
  ///
  /// * There's no native support for [Genres] artwork so, we take the artwork from
  /// the first audio.
  GENRE,
}
