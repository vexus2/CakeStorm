package com.vexus2.cakestorm.event;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AsyncResult;
import com.intellij.psi.PsiDirectory;
import com.jetbrains.php.actions.PhpNewFileDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;

public class CakeStormNotificationListener implements NotificationListener {
  private PsiDirectory directory;
  private Project project;

  public CakeStormNotificationListener(Project project, PsiDirectory directory) {
    this.project = project;
    this.directory = directory;
  }

  @Override
  public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      if (event.getDescription().equals("create") && !project.isDisposed()) {
        // TODO: Create and show Virtual File.
        AsyncResult<Boolean> booleanAsyncResult = new PhpNewFileDialog(project, directory).showAndGetOk();
        Boolean result = booleanAsyncResult.getResult();
        booleanAsyncResult.doWhenDone(new AsyncResult.Handler<Boolean>(){

          @Override
          public void run(Boolean bool) {
            System.out.println(bool);
          }
        });

      }
    }
  }
}
