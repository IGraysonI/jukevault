/// [GenreModel] that contains all [Genre] Information.
class GenreModel {
  GenreModel(this._info);

  //The type dynamic is used for both but, the map is always based in [String, dynamic]
  final Map<dynamic, dynamic> _info;

  /// Return genre [id]
  int get id => _info['_id'] as int;

  /// Return [genre] name
  String get genre => _info['name'] as String;

  ///Return genre [numOfSongs]
  int get numOfSongs => _info['num_of_songs'] as int;

  /// Return a map with all [keys] and [values] from specific genre.
  Map<dynamic, dynamic> get getMap => _info;

  @override
  String toString() => _info.toString();
}
