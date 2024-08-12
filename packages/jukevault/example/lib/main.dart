import 'package:flutter/material.dart';
import 'package:jukevault_example/main_controller.dart';
import 'package:jukevault_example/src/widgets/query_songs.dart';
import 'package:jukevault_example/src/widgets/settings_dialog_widget.dart';

void main() => runApp(
      MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: ThemeData.light().copyWith(primaryColor: Colors.grey[200]),
        darkTheme: ThemeData.dark(),
        home: const Main(),
      ),
    );

class Main extends StatefulWidget {
  const Main({Key? key}) : super(key: key);

  @override
  _MainState createState() => _MainState();
}

class _MainState extends State<Main> {
  // Default border to all app.
  final BorderRadius borderRadius = BorderRadius.circular(10);
  final MainController _controller = MainController();

  @override
  void initState() {
    super.initState();
    _controller.checkPermisison();
  }

  Widget _titleWidget(BuildContext context, String title) => Align(
        alignment: Alignment.centerLeft,
        child: Padding(
          padding: EdgeInsets.only(
            left: MediaQuery.of(context).size.width * 0.06,
          ),
          child: Text(title),
        ),
      );

  Widget _messageWidget(BuildContext context) => Container(
        margin: const EdgeInsets.only(top: 15),
        padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 20),
        width: MediaQuery.of(context).size.width * 0.9,
        decoration: BoxDecoration(
          color: Theme.of(context).primaryColor,
          borderRadius: borderRadius,
        ),
        child: Column(
          children: [
            const Text(
              'This plugin require: \nLibrary (IOS) and READ (Android) permissions.',
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: _controller.hasError ? null : _controller.requestPermission(context),
              style: ElevatedButton.styleFrom(
                elevation: 0,
                backgroundColor: Theme.of(context).scaffoldBackgroundColor,
              ),
              child: const Text('Grant permission'),
            )
          ],
        ),
      );

  Widget _queriesWidget(BuildContext context) => SizedBox(
      width: MediaQuery.of(context).size.width * 0.9,
      child: ListTile(
        shape: RoundedRectangleBorder(borderRadius: borderRadius),
        tileColor: Theme.of(context).primaryColor,
        title: Text('Static Query'),
        trailing: Icon(Icons.navigate_next_rounded),
        onTap: () => Navigator.of(context).push(
          MaterialPageRoute(builder: (_) => const QueryAudios()),
        ),
      ));

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          elevation: 1.5,
          shadowColor: Colors.black.withOpacity(0.5),
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
          title: Text(
            "JukeVault",
            style: TextStyle(
              fontSize: 20,
              color: Theme.of(context).brightness == Brightness.dark ? Colors.white : Colors.black,
            ),
          ),
          centerTitle: true,
          actions: [
            IconButton(
              onPressed: () => showSettingsDialog(context),
              icon: const Icon(Icons.more_vert),
            )
          ],
        ),
        body: SizedBox(
          width: MediaQuery.of(context).size.width,
          child: SingleChildScrollView(
            child: Wrap(
              runSpacing: 15,
              alignment: WrapAlignment.center,
              children: [
                _messageWidget(context),
                _titleWidget(context, 'Queries'),
                _queriesWidget(context),
                _titleWidget(context, 'Help'),
              ],
            ),
          ),
        ),
      );
}
