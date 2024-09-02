// ignore_for_file: constant_identifier_names

part of '../../controllers/sorts_controller.dart';

/// Defines the sort type used for [queryPlaylists].
enum PlaylistSortType {
  /// [PLAYLIST] will return playlist based in [playlist].
  PLAYLIST,

  /// [DATE_ADDED] will return playlist based in [data_added].
  DATE_ADDED,
}
