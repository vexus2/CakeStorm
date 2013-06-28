package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.vexus2.cakestorm.lib.CakeIdentifier;
import com.vexus2.cakestorm.lib.FileSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartJumper extends Jumper {
  private ControllerAction controllerAction;

  public SmartJumper(AnActionEvent e) throws Exception {
    super(e);
    PhpClass phpClass = PsiTreeUtil.getChildOfType(psiFile.getFirstChild(), PhpClass.class);
    if (phpClass != null) {
      //TODO: controller以外ならも条件に追加したい
      this.controllerAction = new ControllerAction(phpClass, editor.getCaretModel().getOffset());
    }
  }

  @Override
  public void jump() {
    switch (fileSystem.getType()) {
      case Controller:
        // Jump to current action or view's action.
        int viewListCount = this.jumpToCurrentAction();
        if (viewListCount == 0){
          showBalloon("A jump target is not found.");
        }
        break;
      case View:
        this.jumpToControllerFromView();
        break;
      case Model:
        //TODO: Select [testcase] or [fixture]
        this.jumpToModelTestCase();
        break;
      case Helper:
        jumpToHelperTestCase();
        break;
      case Component:
        this.jumpToComponentTestCase();
        break;
      case Behavior:
        this.jumpToBehaviorTestCase();
        break;
      case Shell:
      case Task:
      case Library:
        showBalloon("A jump target is not found.");
        break;
      case Fixture:
        // this.jumpToModelFromFixture();
        showBalloon("A jump target is not found.");
        break;
      case ControllerTestCase:
        this.jumpToControllerFromTest();
        break;
      case ModelTestCase:
        this.jumpToModelFromTest();
        break;
      case BehaviorTestCase:
        this.jumpToBehavior();
        break;
      case ComponentTestCase:
        this.jumpToComponent();
        break;
      case HelperTestCase:
        this.jumpToHelper();
        break;
      default:
        showBalloon("A jump target is not found.");
        break;
    }
  }

  public void jumpToAction() {
    fileSystem.filePopup(controllerAction);
  }

  public int jumpToCurrentAction() {
    Function currentFocusFunction = this.controllerAction.getCurrentAction();
    int targetViewCount = fileSystem.filePopup(currentFocusFunction, controllerAction.getCurrentControllerName());
    if (currentFocusFunction == null || targetViewCount == 0) {
      return fileSystem.filePopup(controllerAction);
    }
    return targetViewCount;
  }

  public void jumpToControllerTestCase() {
    jumpWithFilePath(CakeIdentifier.ControllerTest);
  }

  public void jumpToControllerFromTest() {
    // Test -> Controller
    jumpWithFilePath(CakeIdentifier.Controller);
  }

  public void jumpToControllerFromView() {
    // View -> Controller
    Pattern pattern = Pattern.compile(fileSystem.getDirectorySystem().getCanonicalAppPath()
                                          + fileSystem.getDirectorySystem().getCakeConfig().cakeVersionAbsorption.get(CakeIdentifier.View)
                                          + "(.*?)/");
    Matcher matcher = pattern.matcher(fileSystem.getCurrentFile().getPath());
    String path = "";

    if (matcher.find()) {
      path = directorySystem.convertControllerName(matcher.group(1));
    }

    VirtualFile virtualFile = fileSystem.virtualFileBy(directorySystem.getAppPath().toString() + fileSystem.getDirectorySystem().getCakeConfig().cakeVersionAbsorption.get(CakeIdentifier.Controller) + path + FileSystem.FILE_EXTENSION_PHP);
    openOrNotice(virtualFile);
  }


  public void jumpToModelTestCase() {
    jumpWithFilePath(CakeIdentifier.ModelTest);
  }

  public void jumpToModelFromTest() {
    // Test -> Model
    jumpWithFilePath(CakeIdentifier.Model);
  }

  public void jumpToModelFromFixture() {
    // Fixture -> Model
    //TODO: not implemented.
  }

  public void jumpToHelperTestCase() {
    jumpWithFilePath(CakeIdentifier.HelperTest);
  }

  public void jumpToHelper() {
    jumpWithFilePath(CakeIdentifier.Helper);
  }


  public void jumpToComponentTestCase() {
    jumpWithFilePath(CakeIdentifier.ComponentTest);
  }

  public void jumpToComponent() {
    jumpWithFilePath(CakeIdentifier.Component);
  }

  public void jumpToBehaviorTestCase() {
    jumpWithFilePath(CakeIdentifier.BehaviorTest);
  }

  public void jumpToBehavior() {
    jumpWithFilePath(CakeIdentifier.Behavior);
  }

  public void jumpToFixture() {
    // Model -> Fixture
    //TODO: not implemented.
  }

  public void jumpToTest() {
    //TODO: not implemented.
  }
}
