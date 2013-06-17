package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.vexus2.cakestorm.lib.FilePath;


public class ControllerJumper extends Jumper {

  public ControllerJumper(AnActionEvent e) throws Exception {
    super(e);
  }

  @Override
  public void jump() {
//            Notifications.Bus.notify(new Notification("Jump To Controller", "Notice", "This action can be used only by [ControllerTestCase].", NotificationType.INFORMATION));
    switch (fileSystem.getType()) {
      case Controller:
        jumpToControllerTestCase();
        break;
      case ControllerTestCase:
        jumpToController();
        break;
    }

  }

  public void jumpToControllerTestCase() {
    jumpWithFilePath(FilePath.ControllerTest);
  }

  public void jumpToController() {
    jumpWithFilePath(FilePath.Controller);
  }
}

