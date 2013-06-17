package com.vexus2.cakestorm.lib;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystem {

  public static final String FILE_EXTENSION_PHP = "php";

  private static VirtualFileManager virtualFileManager = null;
  private final VirtualFileManager fileManager = VirtualFileManager.getInstance();

  private VirtualFile currentFile = null;

  private ClassType type = null;
  private Project project;

  public ClassType getType() {
    return type;
  }

  public VirtualFile getCurrentFile() {
    return currentFile;
  }

  public FileSystem(VirtualFile vf) {
    virtualFileManager = VirtualFileManager.getInstance();
    currentFile = vf;
    type = ClassType.getClassType(vf);
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
    return fileManager.refreshAndFindFileByUrl(filePath);
  }

  @Nullable
  public void openOrCreate(VirtualFile virtualFile, String path) {
    if (virtualFile == null) {
      // Noticeを表示
      Notifications.Bus.notify(new Notification("CakeStorm", "Notice", path + " is Notfound. create?", NotificationType.INFORMATION));
      //TODO [create?]部分をリンクにして、リンクを押下したらCreate new fileダイアログを表示させる
    } else {
      //TODO: ファイルを開く
      new OpenFileDescriptor(project, virtualFile).navigate(true);
    }
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
