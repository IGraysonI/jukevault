import 'package:flutter/material.dart';
import 'package:jukevault/jukevault.dart';

class MainController {
  final Jukevault query = Jukevault();

  bool get hasPermission => _hasPermission;
  bool get hasError => _hasError;
  bool _hasPermission = false;
  bool _hasError = false;

  void checkPermisison() async {
    // Check if the plugin has permission to read the library.
    try {
      _hasPermission = await query.permissionsStatus();

      // If [permissionsStatus] is called from [Web] or [Desktop] platforms,
      // will throw a [UnimplementedError]. So, we'll use this to disable
      // the grant buttton.
    } on UnimplementedError {
      _hasError = true;

      // Some went wrong.
    } catch (e) {
      _hasPermission = false;
    }
  }

  Function() requestPermission(BuildContext context) => () async {
        bool r = hasPermission ? hasPermission : await query.permissionsRequest();
        if (!context.mounted) return;
        SnackBar snackBar = SnackBar(
          content: Row(
            children: [
              Icon(r ? Icons.done : Icons.error_outline_rounded),
              const SizedBox(width: 18),
              Text(
                r ? 'The plugin has permission!' : 'The plugin has no permission!',
                style: TextStyle(
                  color: Theme.of(context).brightness == Brightness.dark ? Colors.white : Colors.black,
                ),
              ),
            ],
          ),
          backgroundColor: r ? Colors.green : Colors.red,
        );
        if (!context.mounted) return;
        ScaffoldMessenger.of(context)
          ..hideCurrentSnackBar()
          ..showSnackBar(snackBar);
      };
}
