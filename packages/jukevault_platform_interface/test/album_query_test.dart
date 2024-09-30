// // test the queryAlbums function from the AlbumsQuery class.
// import 'package:flutter_test/flutter_test.dart';
// import 'package:jukevault_platform_interface/jukevault_platform_interface.dart';
// import 'package:jukevault_platform_interface/src/methods/queries/albums_query.dart';

// void main() {
//   group('AlbumsQuery', () {
//     late List<AudioModel> audios;
//     late AlbumsQuery albumsQuery;

//     setUp(() {
//       albumsQuery = AlbumsQuery();
//     });

//     test('queryAlbums', () async {
//       final albums = await albumsQuery.queryAlbums(audios);

//       expect(albums.length, 3);
//       expect(albums[0].id, 1);
//       expect(albums[0].album, 'album1');
//       expect(albums[0].artist, 'artist1');
//       expect(albums[0].artistId, 1);
//       expect(albums[0].numSongs, 1);

//       expect(albums[1].id, 2);
//       expect(albums[1].album, 'album2');
//       expect(albums[1].artist, 'artist2');
//       expect(albums[1].artistId, 2);
//       expect(albums[1].numSongs, 2);

//       expect(albums[2].id, 3);
//       expect(albums[2].album, 'album3');
//       expect(albums[2].artist, 'artist3');
//       expect(albums[2].artistId, 3);
//       expect(albums[2].numSongs, 3);
//     });
//   });
// }
