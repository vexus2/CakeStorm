package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.vexus2.cakestorm.lib.DirectorySystem;
import com.vexus2.cakestorm.lib.FilePath;
import com.vexus2.cakestorm.lib.FileSystem;

abstract public class Jumper {

  protected FileSystem fileSystem = null;
  protected static DirectorySystem directorySystem = null;

  protected Jumper(AnActionEvent e) throws Exception {
    VirtualFile currentFile = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
    fileSystem = new FileSystem(currentFile);
    fileSystem.setProject(e.getProject());
    directorySystem = new DirectorySystem(currentFile, fileSystem.getType());
    directorySystem.setAppPath(fileSystem.getAppPath(currentFile));
  }

  public abstract void jump();

  protected void jumpWithFilePath(FilePath filePath) {
    String path = directorySystem.getPath(filePath, fileSystem.getCurrentFile().getNameWithoutExtension());
    VirtualFile virtualFile = fileSystem.virtualFileBy(directorySystem.getAppPath().toString() + path);
    fileSystem.openOrCreate(virtualFile, directorySystem.getAppPath().getCanonicalPath() + path);
  }
}
