package com.vexus2.cakestorm.ide;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.vexus2.cakestorm.lib.OpenType;
import com.vexus2.cakestorm.logic.SmartJumper;

@SuppressWarnings("ComponentNotRegistered")
public class Actions {

  public static void notification(String message) {
    Notifications.Bus.notify(
        new Notification("CakeStorm", "CakeStorm Error", message, NotificationType.INFORMATION));
  }

  public static class SmartJump extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
      try {
        new SmartJumper(event).jump();
      } catch (Exception e) {
        notification(e.getMessage());
      }
    }

    @Override
    public void update(AnActionEvent e) {
      e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
    }
  }

  public static class HorizontalSmartJump extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
      try {
        new SmartJumper(event, OpenType.HORIZONTAL).jump();
      } catch (Exception e) {
        notification(e.getMessage());
      }
    }

    @Override
    public void update(AnActionEvent e) {
      e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
    }
  }

  public static class VerticalSmartJump extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
      try {
        new SmartJumper(event, OpenType.VERTICAL).jump();
      } catch (Exception e) {
        notification(e.getMessage());
      }
    }

    @Override
    public void update(AnActionEvent e) {
      e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
    }
  }

}
