import 'package:jukevault_platform_interface/src/models/media_model.dart';

/// [ArtistModel] that contains all [Artist] Information.
class ArtistModel extends MediaModel {
  ArtistModel(this._info) : super(_info['_id']);

  final Map<dynamic, dynamic> _info;

  /// Return artist [artist]
  String get artist => _info["artist"];

  /// Return artist [numberOfAlbums]
  int? get numberOfAlbums => _info["number_of_albums"];

  /// Return artist [numberOfTracks]
  int? get numberOfTracks => _info["number_of_tracks"];

  /// Return a map with all [keys] and [values] from specific artist.
  Map get getMap => _info;

  ArtistModel copyWith({
    int? id,
    String? artist,
    int? numberOfAlbums,
    int? numberOfTracks,
  }) =>
      ArtistModel({
        "_id": id ?? this.id,
        "artist": artist ?? this.artist,
        "number_of_albums": numberOfAlbums ?? this.numberOfAlbums,
        "number_of_tracks": numberOfTracks ?? this.numberOfTracks,
      });

  @override
  String toString() => '$_info';
}
