import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:jukevault/jukevault.dart';

import 'dialog_widget.dart';

class QueryAlbums extends StatefulWidget {
  const QueryAlbums({super.key});

  @override
  State<QueryAlbums> createState() => _QueryAlbumsState();
}

class _QueryAlbumsState extends State<QueryAlbums> {
  final Jukevault _jukevault = Jukevault();

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: const Text("AlbumsQueryExample"),
          elevation: 0,
          actions: [
            IconButton(
              onPressed: () => buildDialog(
                context,
                'Constant list',
                'The only way to \'update\' this list is calling: setState',
              ),
              icon: const Icon(Icons.info_outline),
            )
          ],
        ),
        body: Center(
          child: FutureBuilder<List<AlbumModel>>(
            // Default values:
            //
            // sortType: null,
            // orderType: OrderType.ASC_OR_SMALLER,
            // uriType: UriType.EXTERNAL,
            // ignoreCase: true,
            // toQuery: const {},
            // toRemove: const {},
            // type: const {AudioType.IS_MUSIC : true},
            future: _jukevault.queryAlbums(filter: MediaFilter.forAlbums()),
            builder: (context, item) {
              // When you try 'query' without asking for [READ] permission the plugin
              // will throw a [PlatformException].
              //
              // This 'no permission' code exception is: 403.
              if (item.hasError) {
                // If the error is a [PlatformException] send the default message.
                if (item.error is PlatformException) {
                  // Define error as PlatformException.
                  var error = item.error as PlatformException;

                  // Return this information or call [permissionsRequest] method.
                  return Text(error.message!);
                } else {
                  // Send the 'unknown' exception.
                  return Text('${item.error}');
                }
              }

              // Waiting content.
              if (item.data == null) return const CircularProgressIndicator();

              // 'Library' is empty.
              if (item.data!.isEmpty) return const Text("Nothing found!");

              // You can use [item.data!] direct or you can create a:
              // List<SongModel> songs = item.data!;
              return ListView.builder(
                itemCount: item.data!.length,
                itemBuilder: (_, index) => ListTile(
                  title: Text(item.data![index].album),
                  subtitle: Text(item.data![index].artist ?? '<Unknown>'),
                  // This Widget will query/load image. Just add the id and type.
                  // You can use/create your own widget/method using [queryArtwork].
                  leading: QueryArtworkWidget(id: item.data![index].id, type: ArtworkType.AUDIO),
                  onTap: () => debugPrint('${item.data![index]}'),
                ),
              );
            },
          ),
        ),
      );
}
