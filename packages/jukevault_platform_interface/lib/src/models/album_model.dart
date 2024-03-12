/// [AlbumModel] that contains all [Album] Information.
class AlbumModel {
  AlbumModel(this._info);

  // The type dynamic is used for both but, the map is always based in [String, dynamic]
  final Map<dynamic, dynamic> _info;

  /// Return album [id]
  int get id => _info['_id'] as int;

  /// Return album [album]
  String get album => _info['album'] as String;

  /// Return album [artist]
  String? get artist => _info['artist'] as String?;

  /// Return album [artistId]
  int? get artistId => _info['artist_id'] as int?;

  /// Return album [numOfSongs]
  int get numOfSongs => _info['numsongs'] as int;

  /// Return a map with all [keys] and [values] from specific album.
  Map<dynamic, dynamic> get getMap => _info;

  @override
  String toString() => _info.toString();
}
