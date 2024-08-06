import 'package:jukevault_platform_interface/src/filter/media_filter.dart';

import '../models/models.dart';

extension OnMediaFilter on List<Map> {
  List<T> mediaFilter<T>(
    MediaFilter filter,
    List<String?> projection,
  ) {
    // Filter the list of audios using all defined filters.
    for (int id in filter.toQuery.keys) {
      // If the given [id] doesn't exist. Skip to next.
      if (projection[id] == null) continue;

      // Get all values to filter.
      var values = filter.toQuery[id];

      // Check if values are null.
      if (values == null) continue;

      // For each 'value' inside 'values'. Remove the item if match the projection.
      for (var value in values) {
        removeWhere((audio) {
          // Check if the projection is valid.
          bool isProjectionValid = audio.containsKey(projection[id]);

          // Check if the item contains the defined value.
          bool containsValue = '${audio[projection[id]]}'.contains(value);

          // If projection is valid and contains the value, keep it.
          return isProjectionValid && !containsValue;
        });
      }
    }

    // Filter the list of audios using all defined filters.
    for (int id in filter.toRemove.keys) {
      // If the given [id] doesn't exist. Skip to next.
      if (projection[id] == null) continue;

      // Get all values to filter.
      var values = filter.toRemove[id];

      // Check if values are null.
      if (values == null) continue;

      // For each 'value' inside 'values'. Remove the item if no match is found.
      for (var value in values) {
        removeWhere((audio) {
          // Check if the projection is valid.
          bool isProjectionValid = audio.containsKey(projection[id]);

          // Check if the item contains the defined value.
          bool containsValue = '${audio[projection[id]]}'.contains(value);

          // If projection is valid and contains the value, remove it.
          return isProjectionValid && containsValue;
        });
      }
    }

    // Define the type of list and return.
    // TODO: Potential error place
    switch (T) {
      case AudioModel _:
        return map((e) => AudioModel(e)).toList() as List<T>;
      case AlbumModel _:
        return map((e) => AlbumModel(e)).toList() as List<T>;
      case ArtistModel _:
        return map((e) => ArtistModel(e)).toList() as List<T>;
      case GenreModel _:
        return map((e) => GenreModel(e)).toList() as List<T>;
      default:
        return [];
    }
  }
}
