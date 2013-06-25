package com.vexus2.cakestorm.lib;


import java.util.regex.Pattern;

public class Utility {
  public static String replaceAllIgnoreCase(String regex ,String reql,String text){
    String retStr = "";
    retStr = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text).replaceAll(reql);
    return retStr;
  }
}
