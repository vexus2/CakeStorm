package com.vexus2.cakestorm.ide;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;

abstract public class Jumper {

  protected AnActionEvent event = null;
  protected FileManager fileManager = null;
  protected static DirectoryManager directoryManager = null;

  protected Jumper(AnActionEvent e) {
    event = e;
    VirtualFile currentFile = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
    fileManager = new FileManager(currentFile);
    directoryManager = new DirectoryManager(currentFile, fileManager.getType());
    directoryManager.setAppPath(fileManager.getAppPath(currentFile));
  }


  public abstract void jump();
}
