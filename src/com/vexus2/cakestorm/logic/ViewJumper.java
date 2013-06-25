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
    //current methodフラグを立てておく
  }

  @Override
  public void jump() {
    switch (fileSystem.getType()) {
      case Controller:
        Function function = this.controllerAction.getCurrentAction();
        //TODO: 現在は全Viewが出てしまうが、view内にカーソルがあれば直にジャンプ出来るように設定する
          fileSystem.filePopup(controllerAction);

        break;
    }
  }
}

