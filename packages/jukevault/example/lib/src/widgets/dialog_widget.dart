import 'package:flutter/material.dart';

const BorderRadius defaultBorder = BorderRadius.all(Radius.circular(10));

void buildDialog(BuildContext context, String title, String message) => showDialog(
      context: context,
      builder: (_) => AlertDialog(
        shape: const RoundedRectangleBorder(borderRadius: defaultBorder),
        backgroundColor: Theme.of(context).primaryColor,
        title: Center(child: Text(title)),
        content: ConstrainedBox(
          constraints: const BoxConstraints(maxHeight: 80, maxWidth: 80),
          child: Center(
            child: Text(message, textAlign: TextAlign.center),
          ),
        ),
      ),
    );
