import 'package:flutter/material.dart';
import 'package:jukevault/jukevault.dart';

const BorderRadius borderRadius = BorderRadius.all(Radius.circular(10));

void showSettingsDialog(BuildContext context) => showDialog(
      context: context,
      builder: (_) => AlertDialog(
        shape: const RoundedRectangleBorder(borderRadius: borderRadius),
        backgroundColor: Theme.of(context).primaryColor,
        title: const Center(child: Text("Settings")),
        content: ConstrainedBox(
          constraints: const BoxConstraints(
            minHeight: 40,
            minWidth: 80,
            maxHeight: 80,
            maxWidth: 80,
          ),
          child: ListTile(
            horizontalTitleGap: 0,
            title: const Text('Delete all cache'),
            leading: const Icon(Icons.cached_rounded),
            shape: const RoundedRectangleBorder(borderRadius: borderRadius),
            onTap: () async {
              bool result = await Jukevault.clearCachedArtworks();
              if (!context.mounted) return;
              SnackBar snackBar = SnackBar(
                content: Row(
                  children: [
                    Icon(result ? Icons.done : Icons.error_outline_rounded),
                    const SizedBox(width: 18),
                    Text(
                      result ? 'All artworks have been deleted' : 'Oops, Something went wrong!',
                      style: TextStyle(
                        color: Theme.of(context).brightness == Brightness.dark ? Colors.white : Colors.black,
                      ),
                    ),
                  ],
                ),
                backgroundColor: result ? Colors.green : Colors.red,
              );
              Navigator.of(context).pop();
              ScaffoldMessenger.of(context)
                ..hideCurrentSnackBar()
                ..showSnackBar(snackBar);
            },
          ),
        ),
      ),
    );
