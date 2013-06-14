package com.vexus2.cakestorm.ide;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {

  private static VirtualFileManager virtualFileManager = null;

  private VirtualFile currentFile = null;

  private ClassType type = null;

  public ClassType getType() {
    return type;
  }

  public VirtualFile getCurrentFile() {
    return currentFile;
  }

  public FileManager(VirtualFile vf) {
    virtualFileManager = VirtualFileManager.getInstance();
    currentFile = vf;
    type = getFileType(vf);
  }

  public VirtualFile getAppPath(VirtualFile currentFile) {
    String appDirPath = null;
    Pattern pattern = Pattern.compile("(.*?/app).*?");
    Matcher matcher = pattern.matcher(currentFile.toString());

    if (matcher.find()) {
      appDirPath = matcher.group(1);
    }

    return virtualFileManager.findFileByUrl(appDirPath);
  }

  public VirtualFile createPath(String path) {
    System.out.println(path);
    return virtualFileManager.findFileByUrl(currentFile.toString() + path);
  }

  public ClassType getFileType(VirtualFile currentFile) {
    String currentFileStr = currentFile.toString();
    if (currentFileStr.matches(".*?(?i)test.*?")) {
      if (currentFileStr.matches(".*?(?i)controller.*?")) return ClassType.ControllerTestCase;
      if (currentFileStr.matches(".*?(?i)model.*?")) return ClassType.ModelTestCase;
      if (currentFileStr.matches(".*?(?i)behavior.*?")) return ClassType.BehaviorTestCase;
      if (currentFileStr.matches(".*?(?i)component.*?")) return ClassType.ComponentTestCase;
      if (currentFileStr.matches(".*?(?i)helper.*?")) return ClassType.HelperTestCase;
    }
    if (currentFileStr.matches(".*?(?i)controller.*?")) return ClassType.Controller;
    if (currentFileStr.matches(".*?(?i)model.*?")) return ClassType.Model;
    if (currentFileStr.matches(".*?(?i)view.*?")) return ClassType.View;
    if (currentFileStr.matches(".*?(?i)fixture.*?")) return ClassType.Fixture;
    if (currentFileStr.matches(".*?(?i)component.*?")) return ClassType.Component;
    if (currentFileStr.matches(".*?(?i)helper.*?")) return ClassType.Helper;
    if (currentFileStr.matches(".*?(?i)behavior.*?")) return ClassType.Behavior;
    if (currentFileStr.matches(".*?(?i)shell.*?")) return ClassType.Shell;
    if (currentFileStr.matches(".*?(?i)task.*?")) return ClassType.Task;
    return null;
  }

}
