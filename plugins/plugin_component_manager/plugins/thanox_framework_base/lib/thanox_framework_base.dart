import 'dart:async';

import 'package:flutter/services.dart';

class ThanoxFrameworkBase {
  static const MethodChannel _channel =
      const MethodChannel('thanox_framework_base');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> get isServiceInstalled async {
    final bool installed = await _channel.invokeMethod("isServiceInstalled");
    return installed;
  }

  static Future<String> get fingerPrint async {
    final String fp = await _channel.invokeMethod('fingerPrint');
    return fp;
  }
}
