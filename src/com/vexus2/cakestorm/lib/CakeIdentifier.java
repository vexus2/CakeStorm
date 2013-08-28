package com.vexus2.cakestorm.lib;

import com.intellij.openapi.vfs.VirtualFile;

public enum CakeIdentifier {
  Controller,
  View,
  Model,
  Helper,
  Component,
  Behavior,
  Plugin,
  Shell,
  Task,
  ControllerTest,
  ModelTest,
  BehaviorTest,
  ComponentTest,
  HelperTest,
  TestFile,
  FileSeparator,
  FileWordSeparator,
  Fixture,
  Library,
  Element,
  FixtureFile,
  Layout,
  Other;

  private CakeIdentifier() {
  }

  public static CakeIdentifier getIdentifier(VirtualFile currentFile) {
    String currentFileStr = currentFile.toString();
    if (currentFileStr.matches(".*?(?i)test.*?")) {
      if (currentFileStr.matches(".*?(?i)component.*?")) return CakeIdentifier.ComponentTest;
      if (currentFileStr.matches(".*?(?i)controller.*?")) return CakeIdentifier.ControllerTest;
      if (currentFileStr.matches(".*?(?i)behavior.*?")) return CakeIdentifier.BehaviorTest;
      if (currentFileStr.matches(".*?(?i)model.*?")) return CakeIdentifier.ModelTest;
      if (currentFileStr.matches(".*?(?i)helper.*?")) return CakeIdentifier.HelperTest;
    }
    if (currentFileStr.matches(".*?(?i)component.*?")) return CakeIdentifier.Component;
    if (currentFileStr.matches(".*?(?i)controller.*?")) return CakeIdentifier.Controller;
    if (currentFileStr.matches(".*?(?i)fixture.*?")) return CakeIdentifier.Fixture;
    if (currentFileStr.matches(".*?(?i)behavior.*?")) return CakeIdentifier.Behavior;
    if (currentFileStr.matches(".*?(?i)model.*?")) return CakeIdentifier.Model;
    if (currentFileStr.matches(".*?(?i)helper.*?")) return CakeIdentifier.Helper;
    if (currentFileStr.matches(".*?(?i)view.*?")) return CakeIdentifier.View;
    if (currentFileStr.matches(".*?(?i)plugin.*?")) return CakeIdentifier.Plugin;
    if (currentFileStr.matches(".*?(?i)shell.*?")) return CakeIdentifier.Shell;
    if (currentFileStr.matches(".*?(?i)task.*?")) return CakeIdentifier.Task;
    if (currentFileStr.matches(".*?(?i)lib.*?")) return CakeIdentifier.Library;
    if (currentFileStr.matches(".*?(?i)lib.*?")) return CakeIdentifier.Layout;
    return null;
  }
}
