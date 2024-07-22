import 'package:jukevault_platform_interface/src/models/media_model.dart';

/// [GenreModel] that contains all [Genre] Information.
class GenreModel extends MediaModel {
  GenreModel(this._info) : super(_info['_id']);

  final Map<dynamic, dynamic> _info;

  /// Return [genre] name
  String get genre => _info["name"];

  ///Return genre [numOfSongs]
  int get numOfSongs => _info["num_of_songs"];

  /// Return a map with all [keys] and [values] from specific genre.
  Map get getMap => _info;

  GenreModel copyWith({
    int? id,
    String? genre,
    int? numOfSongs,
  }) =>
      GenreModel({
        "_id": id ?? this.id,
        "name": genre ?? this.genre,
        "num_of_songs": numOfSongs ?? this.numOfSongs,
      });

  @override
  String toString() => '$_info';
}
