package com.vexus2.cakestorm.lib;

import com.intellij.openapi.vfs.VirtualFile;

public enum ClassType {
  Controller,
  View,
  Model,
  Helper,
  Component,
  Behavior,
  Shell,
  Task,
  Fixture,
  ControllerTestCase,
  ModelTestCase,
  BehaviorTestCase,
  ComponentTestCase,
  HelperTestCase,
  Library;

  private ClassType() {
  }

  public static ClassType getClassType(VirtualFile currentFile) {
    String currentFileStr = currentFile.toString();
    if (currentFileStr.matches(".*?(?i)test.*?")) {
      if (currentFileStr.matches(".*?(?i)component.*?")) return ClassType.ComponentTestCase;
      if (currentFileStr.matches(".*?(?i)controller.*?")) return ClassType.ControllerTestCase;
      if (currentFileStr.matches(".*?(?i)behavior.*?")) return ClassType.BehaviorTestCase;
      if (currentFileStr.matches(".*?(?i)model.*?")) return ClassType.ModelTestCase;
      if (currentFileStr.matches(".*?(?i)helper.*?")) return ClassType.HelperTestCase;
    }
    if (currentFileStr.matches(".*?(?i)component.*?")) return ClassType.Component;
    if (currentFileStr.matches(".*?(?i)controller.*?")) return ClassType.Controller;
    if (currentFileStr.matches(".*?(?i)fixture.*?")) return ClassType.Fixture;
    if (currentFileStr.matches(".*?(?i)behavior.*?")) return ClassType.Behavior;
    if (currentFileStr.matches(".*?(?i)model.*?")) return ClassType.Model;
    if (currentFileStr.matches(".*?(?i)helper.*?")) return ClassType.Helper;
    if (currentFileStr.matches(".*?(?i)view.*?")) return ClassType.View;
    if (currentFileStr.matches(".*?(?i)shell.*?")) return ClassType.Shell;
    if (currentFileStr.matches(".*?(?i)task.*?")) return ClassType.Task;
    if (currentFileStr.matches(".*?(?i)lib.*?")) return ClassType.Library;
    return null;
  }

}
