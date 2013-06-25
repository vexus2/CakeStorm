package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;


public class ViewJumper extends Jumper {

  private ControllerAction controllerAction;

  public ViewJumper(AnActionEvent e) throws Exception {
    super(e);
    PhpClass phpClass = PsiTreeUtil.getChildOfType(psiFile.getFirstChild(), PhpClass.class);
    this.controllerAction = new ControllerAction(phpClass, editor.getCaretModel().getOffset());
  }

  @Override
  public void jump() {
    switch (fileSystem.getType()) {
      case Controller:
        Function function = this.controllerAction.getCurrentAction();
        //TODO: Show actions on cursor.
          fileSystem.filePopup(controllerAction);

        break;
    }
  }
}

