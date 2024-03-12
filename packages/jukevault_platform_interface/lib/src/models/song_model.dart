/// [SongModel] that contains all [Song] Information.
class SongModel {
  SongModel(this._info);

  //The type dynamic is used for both but, the map is always based in [String, dynamic]
  final Map<dynamic, dynamic> _info;

  /// Return song [id]
  int get id => _info['_id'] as int;

  /// Return song [data]
  String get data => _info['_data'] as String;

  /// Return song [uri]
  String? get uri => _info['_uri'] as String?;

  /// Return song [displayName]
  String get displayName => _info['_display_name'] as String;

  /// Return song [displayName] without Extension
  String get displayNameWOExt => _info['_display_name_wo_ext'] as String;

  /// Return song [size]
  int get size => _info['_size'] as int;

  /// Return song [album]
  String? get album => _info['album'] as String?;

  /// Return song [albumId]
  int? get albumId => _info['album_id'] as int?;

  /// Return song [artist]
  String? get artist => _info['artist'] as String?;

  //TODO: Chekc if this is correct
  /// Return song [artistId]
  int? get artistId => _info['artist_id'] as int?;

  /// Return song [genre]
  ///
  /// Important:
  ///   * Only Api >= 30/Android 11
  String? get genre => _info['genre'] as String?;

  /// Return song [genreId]
  ///
  /// Important:
  ///   * Only Api >= 30/Android 11
  int? get genreId => _info['genre_id'] as int?;

  /// Return song [bookmark]
  int? get bookmark => _info['bookmark'] as int?;

  /// Return song [composer]
  String? get composer => _info['composer'] as String?;

  /// Return song [dateAdded]
  int? get dateAdded => _info['date_added'] as int?;

  /// Return song [dateModified]
  int? get dateModified => _info['date_modified'] as int?;

  /// Return song [duration]
  int? get duration => _info['duration'] as int?;

  /// Return song [title]
  String get title => _info['title'] as String;

  /// Return song [track]
  int? get track => _info['track'] as int?;

  // String get uri;

  /// Return song only the [fileExtension]
  String get fileExtension => _info['file_extension'] as String;

  // Bool methods

  /// Return song type: [isAlarm]
  ///
  /// Will always return true or false
  bool? get isAlarm => _info['is_alarm'] as bool?;

  /// Return song type: [isAudioBook]
  ///
  /// Will always return true or false
  ///
  /// Important:
  ///   * Only Api >= 29/Android 10
  bool? get isAudioBook => _info['is_audiobook'] as bool?;

  /// Return song type: [isMusic]
  ///
  /// Will always return true or false
  bool? get isMusic => _info['is_music'] as bool?;

  /// Return song type: [isNotification]
  ///
  /// Will always return true or false
  bool? get isNotification => _info['is_notification'] as bool?;

  /// Return song type: [isPodcast]
  ///
  /// Will always return true or false
  bool? get isPodcast => _info['is_podcast'] as bool?;

  /// Return song type: [isRingtone]
  ///
  /// Will always return true or false
  bool? get isRingtone => _info['is_ringtone'] as bool?;

  /// Return a map with all [keys] and [values] from specific song.
  Map<dynamic, dynamic> get getMap => _info;

  @override
  String toString() => _info.toString();
}
