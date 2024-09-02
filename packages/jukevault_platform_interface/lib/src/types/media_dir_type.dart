// ignore_for_file: constant_identifier_names

part of '../controllers/types_controller.dart';

/// Define where the plugin will query the medias:
///   * [DEFAULT]
///     * Android: MediaStore
///     * iOS: MPMediaLibrary(MPMediaQuery)
///     * Web: The app assets folder.
///     * Windows: The user '/Music' directory.
///   * [ASSETS] The app assets folder.
///   * [APP_DIR] The app 'private' directory.
enum MediaDirType {
  /// INTERNAL storage.
  DEFAULT,

  /// EXTERNAL storage.
  ASSETS,

  /// INTERNAL storage.
  APP_DIR,
}
