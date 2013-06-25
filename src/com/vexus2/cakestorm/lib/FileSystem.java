package com.vexus2.cakestorm.lib;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.vexus2.cakestorm.logic.ControllerAction;
import com.vexus2.cakestorm.logic.Function;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystem {

  public static final String FILE_EXTENSION_PHP = ".php";
  public static final String FILE_EXTENSION_TEMPLATE = ".ctp";

  private static VirtualFileManager virtualFileManager = null;
  //  private VirtualFileManager fileManager = VirtualFileManager.getInstance();
  private DirectorySystem directorySystem;

  private VirtualFile currentFile = null;

  private ClassType type = null;
  private Project project;
  private DataContext context;


  public FileSystem(VirtualFile vf, DataContext context) {
    virtualFileManager = VirtualFileManager.getInstance();
    this.context = context;
    this.currentFile = vf;
    this.type = ClassType.getClassType(vf);
  }

  public void filePopup(ControllerAction controllerAction) {
    Map<String, Function> currentActions = controllerAction.getActions();
    DefaultActionGroup group = new DefaultActionGroup();
    String betweenDirectory = directorySystem.getBetweenDirectoryPath(controllerAction.getCurrentControllerName());

    for (Map.Entry<String, Function> e : currentActions.entrySet()) {
      Boolean grouped = false;
      List<String> renderViews = e.getValue().getRenderViews();
      for (String templateName : renderViews) {
        String actionPath = directorySystem.getPath(FilePath.View, betweenDirectory, templateName);
        VirtualFile virtualFile = this.virtualFileBy(directorySystem.getAppPath().toString() + actionPath);
        if (virtualFile == null)
          continue;
        if (!grouped) {
          group.addSeparator(e.getValue().getName());
          grouped = true;
        }
        group.add(new AnAction(actionPath, virtualFile.getPath(), null) {
          @Override
          public void actionPerformed(AnActionEvent e) {
            VirtualFile fileByUrl = virtualFileManager.refreshAndFindFileByUrl("file://" + e.getPresentation().getDescription());
            open(fileByUrl);
          }
        });
      }
    }

    if (group.getChildrenCount() == 0)
      return;
    final ListPopup popup = JBPopupFactory.getInstance()
        .createActionGroupPopup("Jump to...",
            group,
            context,
            JBPopupFactory.ActionSelectionAid.NUMBERING,
            true);

    popup.showCenteredInCurrentWindow(project);
  }

  public ClassType getType() {
    return type;
  }

  public VirtualFile getCurrentFile() {
    return currentFile;
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
    return virtualFileManager.findFileByUrl(currentFile.toString() + path);
  }

  @Nullable
  public VirtualFile virtualFileBy(String filePath) {
    return virtualFileManager.refreshAndFindFileByUrl(filePath);
  }

  @Nullable
  public void open(VirtualFile virtualFile) {
    new OpenFileDescriptor(project, virtualFile).navigate(true);
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public DirectorySystem getDirectorySystem() {
    return directorySystem;
  }

  public void setDirectorySystem(DirectorySystem directorySystem) {
    this.directorySystem = directorySystem;
  }
}
