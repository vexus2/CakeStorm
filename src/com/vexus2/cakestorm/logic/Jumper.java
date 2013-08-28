package com.vexus2.cakestorm.logic;

import com.intellij.CommonBundle;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import com.vexus2.cakestorm.lib.CakeIdentifier;
import com.vexus2.cakestorm.lib.DirectorySystem;
import com.vexus2.cakestorm.lib.FileSystem;
import com.vexus2.cakestorm.lib.OpenType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

abstract public class Jumper {

  protected Editor editor;
  protected FileSystem fileSystem = null;
  protected static DirectorySystem directorySystem = null;
  protected Project project;
  protected PsiDirectory directory;
  protected PsiFile psiFile;

  protected Jumper(AnActionEvent e, OpenType openType) throws Exception {
    VirtualFile currentFile = PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
    this.psiFile = LangDataKeys.PSI_FILE.getData(e.getDataContext());
    this.project = e.getProject();
    this.editor = PlatformDataKeys.EDITOR.getData(e.getDataContext());
    DataContext dataContext = e.getDataContext();

    IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (view != null) {
      this.directory = view.getOrChooseDirectory();
    }

    this.fileSystem = new FileSystem(currentFile, e.getDataContext());
    this.fileSystem.setProject(e.getProject());
    directorySystem = new DirectorySystem(e.getProject(), currentFile, fileSystem.getIdentifier());
    directorySystem.setAppPath(fileSystem.getAppFile(currentFile));
    this.fileSystem.setProject(e.getProject());
    this.fileSystem.setDirectorySystem(directorySystem);
    this.fileSystem.setOpenType(openType);
  }


  public abstract void jump();

  protected void jumpWithFilePath(CakeIdentifier cakeIdentifier) {
    String path = directorySystem.getCakeConfig().getPath(cakeIdentifier, "", fileSystem.getCurrentFile().getNameWithoutExtension(), this.fileSystem.getPluginDir());
    VirtualFile virtualFile = fileSystem.virtualFileBy(directorySystem.getAppPath().toString() + path);
    openOrNotice(virtualFile);
  }

  protected void openOrNotice(@Nullable VirtualFile virtualFile) {
    if (virtualFile == null) {
      showBalloon("A jump target is not found.");
    } else {
      fileSystem.open(virtualFile);
    }
  }

  protected void showBalloon(String message) {
//      Notifications.Bus.notify(
//          new Notification("CakeStorm", "File Not Found.", path + " is Notfound. <a href='create'>create?</a>",
//              NotificationType.INFORMATION, new CakeStormNotificationListener(project, directory)), project);

    final JFrame frame = WindowManager.getInstance().getFrame(project.isDefault() ? null : project);
    if (frame == null) return;
    final JComponent component = frame.getRootPane();
    if (component == null) return;
    final Rectangle rect = component.getVisibleRect();
    final Point p = new Point(rect.x + rect.width - 10, rect.y + 20);
    final RelativePoint point = new RelativePoint(component, p);

    final BalloonBuilder balloonBuilder = JBPopupFactory.getInstance().
        createHtmlTextBalloonBuilder(message, MessageType.INFO.getDefaultIcon(),
                                     MessageType.INFO.getPopupBackground(), null);
    balloonBuilder.setShowCallout(false).setCloseButtonEnabled(true)
                  .createBalloon().show(point, Balloon.Position.atLeft);

  }

  protected String getErrorTitle() {
    return CommonBundle.getErrorTitle();
  }
}
