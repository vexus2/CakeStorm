package com.vexus2.cakestorm.logic;

import com.intellij.CommonBundle;
import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.vexus2.cakestorm.event.CakeStormNotificationListener;
import com.vexus2.cakestorm.lib.DirectorySystem;
import com.vexus2.cakestorm.lib.FilePath;
import com.vexus2.cakestorm.lib.FileSystem;

abstract public class Jumper {

  protected Editor editor;
  protected FileSystem fileSystem = null;
  protected static DirectorySystem directorySystem = null;
  protected Project project;
  protected PsiDirectory directory;
  protected PsiFile psiFile;

  protected Jumper(AnActionEvent e) throws Exception {
    VirtualFile currentFile = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
    this.psiFile = DataKeys.PSI_FILE.getData(e.getDataContext());
    this.project = e.getProject();
    this.editor = PlatformDataKeys.EDITOR.getData(e.getDataContext());
    DataContext dataContext = e.getDataContext();

    IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (view != null) {
      this.directory = view.getOrChooseDirectory();
    }

    this.fileSystem = new FileSystem(currentFile, e.getDataContext());
    this.fileSystem.setProject(e.getProject());
    directorySystem = new DirectorySystem(currentFile, fileSystem.getType());
    directorySystem.setAppPath(fileSystem.getAppPath(currentFile));
    this.fileSystem.setProject(e.getProject());
    this.fileSystem.setDirectorySystem(directorySystem);
  }


  public abstract void jump();

  protected void jumpWithFilePath(FilePath filePath) {
    String path = directorySystem.getPath(filePath, null, fileSystem.getCurrentFile().getNameWithoutExtension());
    VirtualFile virtualFile = fileSystem.virtualFileBy(directorySystem.getAppPath().toString() + path);
    if (virtualFile == null) {
      // Noticeを表示
      Notifications.Bus.notify(
          new Notification("CakeStorm", "File Not Found.", path + " is Notfound. <a href='create'>create?</a>",
              NotificationType.INFORMATION, new CakeStormNotificationListener(project, directory)), project);
    } else {
      fileSystem.open(virtualFile);
    }
  }

  protected String getErrorTitle() {
    return CommonBundle.getErrorTitle();
  }
}
