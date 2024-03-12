import 'package:jukevault_platform_interface/src/types/log_type.dart';

/// Used to configure the logging behavior.
class LogConfig {
  LogConfig({
    this.logType = LogType.WARN,
    this.showDetailedLog = false,
  });

  /// Define the logging level.
  final LogType logType;

  /// Define if detailed log will be shown (Disable by default to avoid spam).
  final bool showDetailedLog;
}
