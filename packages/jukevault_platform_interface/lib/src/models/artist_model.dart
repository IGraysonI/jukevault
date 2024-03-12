/// [ArtistModel] that contains all [Artist] Information.
class ArtistModel {
  ArtistModel(this._info);

  // The type dynamic is used for both but, the map is always based in [String, dynamic]
  final Map<dynamic, dynamic> _info;

  /// Return artist [id]
  int get id => _info['_id'] as int;

  /// Return artist [artist]
  String get artist => _info['artist'] as String;

  /// Return artist [numberOfAlbums]
  int? get numberOfAlbums => _info['number_of_albums'] as int?;

  /// Return artist [numberOfTracks]
  int? get numberOfTracks => _info['number_of_tracks'] as int?;

  /// Return a map with all [keys] and [values] from specific artist.
  Map<dynamic, dynamic> get getMap => _info;

  @override
  String toString() => _info.toString();
}
