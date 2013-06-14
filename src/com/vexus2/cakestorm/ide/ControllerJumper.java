package com.vexus2.cakestorm.ide;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;


public class ControllerJumper extends Jumper {

  protected ControllerJumper(AnActionEvent e) {
    super(e);
  }

  @Override
  public void jump() {
//            Notifications.Bus.notify(new Notification("Jump To Controller", "Notice", "This action can be used only by [ControllerTestCase].", NotificationType.INFORMATION));
    switch (fileManager.getType()) {
      case Controller:
        jumpToControllerTestCase();
        break;
      case ControllerTestCase:
        jumpToController();
        break;
    }

  }

  public void jumpToControllerTestCase() {
    VirtualFile virtualFile = fileManager.createPath(directoryManager.getAppPath().toString() + directoryManager.getDirectoryMap().get(FilePath.ControllerTest) + fileManager.getCurrentFile().getName());
    if (virtualFile == null) {

      //TODO ファイルがあれば開く、なければ新規作成ポップアップ
    } else {

    }
//    NewFileAction newFileAction = new NewFileAction();
//    NewVirtualFileSystem newVirtualFileSystem = new NewVirtualFile();

  }

  public void jumpToController() {
  }
}

