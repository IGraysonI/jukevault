import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:jukevault/jukevault.dart';
import 'package:jukevault_example/src/widgets/dialog_widget.dart';

class QueryAudios extends StatefulWidget {
  const QueryAudios({Key? key}) : super(key: key);

  @override
  _QueryAudiosState createState() => _QueryAudiosState();
}

class _QueryAudiosState extends State<QueryAudios> {
  final JukeVault _jukeVault = JukeVault();

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: const Text("QueryExample"),
          elevation: 0,
          actions: [
            // A 'Dialog' explaining about this 'constant' list.
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
          child: FutureBuilder<List<AudioModel>>(
            // Default values:
            //
            // sortType: null,
            // orderType: OrderType.ASC_OR_SMALLER,
            // uriType: UriType.EXTERNAL,
            // ignoreCase: true,
            // toQuery: const {},
            // toRemove: const {},
            // type: const {AudioType.IS_MUSIC : true},
            future: _jukeVault.queryAudios(
              filter: MediaFilter.forAudios(),
            ),
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
                  title: Text(item.data![index].title),
                  subtitle: Text(item.data![index].artist ?? '<Unknown>'),
                  // This Widget will query/load image. Just add the id and type.
                  // You can use/create your own widget/method using [queryArtwork].
                  leading: QueryArtworkWidget(
                    id: item.data![index].id,
                    type: ArtworkTypeEnum.AUDIO,
                  ),
                  onTap: () => debugPrint('${item.data![index]}'),
                ),
              );
            },
          ),
        ),
      );
}
