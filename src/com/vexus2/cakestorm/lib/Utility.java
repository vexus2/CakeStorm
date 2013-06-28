package com.vexus2.cakestorm.lib;


import java.util.regex.Pattern;

public class Utility {
  public static String replaceAllIgnoreCase(String regex, String reql, String text) {
    String retStr = "";
    retStr = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text).replaceAll(reql);
    return retStr;
  }

  static String toCamelCase(String s) {
    String[] parts = s.split("_");
    String camelCaseString = "";
    for (String part : parts) {
      camelCaseString = camelCaseString + toProperCase(part);
    }
    return camelCaseString;
  }

  static String toProperCase(String s) {
    return s.substring(0, 1).toUpperCase() +
        s.substring(1).toLowerCase();
  }

  public static String camelToSnake(String targetStr) {
    String convertedStr = targetStr
        .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
        .replaceAll("([a-z])([A-Z])", "$1_$2");
    return convertedStr.toLowerCase();
  }
}
