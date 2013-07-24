package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.vexus2.cakestorm.lib.CakeIdentifier;
import com.vexus2.cakestorm.lib.FileSystem;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartJumper extends Jumper {
  private ViewMethod viewMethod;
  private ControllerMethod controllerMethod;

  public SmartJumper(AnActionEvent e) throws Exception {
    super(e);
    if (this.fileSystem.getIdentifier() == CakeIdentifier.Controller) {
      PhpClass phpClass = PsiTreeUtil.getChildOfType(psiFile.getFirstChild(), PhpClass.class);
      if (phpClass != null) {
        this.controllerMethod = new ControllerMethod(phpClass, editor.getCaretModel().getOffset());
      }
    } else if (this.fileSystem.getIdentifier() == CakeIdentifier.View) {
      this.viewMethod = new ViewMethod(psiFile);
    }
  }

  @Override
  public void jump() {
    DefaultActionGroup group = new DefaultActionGroup();

   addControllerGroups(group);
    addViewGroups(group);

    for (CakeIdentifier identifier : CakeIdentifier.values()) {
      if (identifier == this.fileSystem.getIdentifier())
        continue;

      VirtualFile virtualFile = fileSystem.getVirtualFile(identifier);
      if (virtualFile != null) {
        group.addSeparator(identifier.toString());
        fileSystem.addGroupChild(group, virtualFile.getPath().toString().replaceAll(fileSystem.getAppFile(virtualFile).getPath().toString(), ""), virtualFile);
      }
    }
    fileSystem.showPopup(group);
  }

  private void addViewGroups(DefaultActionGroup group) {
    if (viewMethod != null) {
      // view -> controller
      Pattern pattern = Pattern.compile(fileSystem.getDirectorySystem().getCanonicalAppPath()
          + fileSystem.getDirectorySystem().getCakeConfig().cakeVersionAbsorption.get(CakeIdentifier.View)
          + "(.*?)/");
      Matcher matcher = pattern.matcher(fileSystem.getCurrentFile().getPath());
      String path = "";

      if (matcher.find()) {
        path = directorySystem.getCakeConfig().convertControllerName(matcher.group(1));
      }

      VirtualFile virtualFile = fileSystem.virtualFileBy(directorySystem.getAppPath().toString()
          + fileSystem.getDirectorySystem().getCakeConfig().cakeVersionAbsorption.get(CakeIdentifier.Controller)
          + path
          + FileSystem.FILE_EXTENSION_PHP);
      if (virtualFile != null) {
        group.addSeparator(CakeIdentifier.Controller.toString());
        fileSystem.addGroupChild(group, virtualFile.getPath().toString().replaceAll(fileSystem.getAppFile(virtualFile).getPath().toString(), ""), virtualFile);
      }

      // view -> element
      List<String> renderElements = viewMethod.getRenderElements();

      fileSystem.createViewPopupActions(renderElements,
          group,
          directorySystem.getCakeConfig().cakeVersionAbsorption.get(CakeIdentifier.Element),
          CakeIdentifier.Element.toString());
    }
  }

  private void addControllerGroups(DefaultActionGroup group) {
    if (controllerMethod != null) {
      Map<String, Function> currentActions = controllerMethod.getActions();
      String betweenDirectory = directorySystem.getCakeConfig().getBetweenDirectoryPath(controllerMethod.getCurrentControllerName());

      for (Map.Entry<String, Function> e : currentActions.entrySet()) {
        String actionName = e.getValue().getName();
        List<String> renderViews = e.getValue().getRenderViews();
        fileSystem.createViewPopupActions(renderViews, group, betweenDirectory, actionName);
      }
    }
  }

  public void jumpToAction() {
    fileSystem.filePopup(controllerMethod);
  }

  public int jumpToCurrentAction() {
    Function currentFocusFunction = this.controllerMethod.getCurrentAction();
    int targetViewCount = fileSystem.filePopup(currentFocusFunction, controllerMethod.getCurrentControllerName());
    if (currentFocusFunction == null || targetViewCount == 0) {
      return fileSystem.filePopup(controllerMethod);
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
      path = directorySystem.getCakeConfig().convertControllerName(matcher.group(1));
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
