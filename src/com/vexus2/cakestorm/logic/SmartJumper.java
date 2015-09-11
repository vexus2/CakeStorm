package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.vexus2.cakestorm.lib.CakeIdentifier;
import com.vexus2.cakestorm.lib.FileSystem;
import com.vexus2.cakestorm.lib.OpenType;
import com.vexus2.cakestorm.lib.PhpElementsUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartJumper extends Jumper {
  private ViewMethod viewMethod;
  private ControllerMethod controllerMethod;

  public SmartJumper(AnActionEvent e) throws Exception {
    this(e, OpenType.DEFAULT);
  }

  public SmartJumper(AnActionEvent e, OpenType openType) throws Exception {
    super(e, openType);
    if (this.fileSystem.getIdentifier() == CakeIdentifier.Controller) {
      PhpClass phpClass = PhpElementsUtil.getFirstClassFromFile(psiFile);

      if (phpClass != null) {
        this.controllerMethod = new ControllerMethod(phpClass, editor.getCaretModel().getOffset());
      }
    } else if (this.fileSystem.getIdentifier() == CakeIdentifier.View
        || this.fileSystem.getIdentifier() == CakeIdentifier.Template) {
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
        fileSystem.addGroupChild(group, virtualFile.getPath().replaceAll(fileSystem.getAppFile(virtualFile).getPath(), ""), virtualFile);
      }
    }
    fileSystem.showPopup(group);
  }

  // ViewからController, Elementなどジャンプ対象のファイルを追加
  private void addViewGroups(DefaultActionGroup group) {
    if(viewMethod == null) return;
    // view -> controller
    Map<CakeIdentifier, String> cakeVersionAbsorption = fileSystem.getDirectorySystem().getCakeConfig().cakeVersionAbsorption;
    String identifier = (fileSystem.getDirectorySystem().getCakeConfig().cakeVersion == 3) ? cakeVersionAbsorption.get(CakeIdentifier.Template) : cakeVersionAbsorption.get(CakeIdentifier.View);
    Pattern pattern = Pattern.compile(fileSystem.getDirectorySystem().getCanonicalAppPath()
        + ((this.fileSystem.getPluginDir() != null) ? "/" + cakeVersionAbsorption.get(CakeIdentifier.Plugin) + this.fileSystem.getPluginDir() : "")
        + identifier
        + "(.*?)/");
    Matcher matcher = pattern.matcher(fileSystem.getCurrentFile().getPath());
    String path = "";

    if (matcher.find()) {
      path = directorySystem.getCakeConfig().convertControllerName(matcher.group(1));
    }

    VirtualFile virtualFile = fileSystem.virtualFileBy(directorySystem.getAppPath().toString()
        + ((this.fileSystem.getPluginDir() != null) ? "/" + cakeVersionAbsorption.get(CakeIdentifier.Plugin) + this.fileSystem.getPluginDir() : "")
        + cakeVersionAbsorption.get(CakeIdentifier.Controller)
        + path
        + FileSystem.FILE_EXTENSION_PHP);
    if (virtualFile != null) {
      group.addSeparator(CakeIdentifier.Controller.toString());
      fileSystem.addGroupChild(group, virtualFile.getPath().replaceAll(fileSystem.getAppFile(virtualFile).getPath(), ""), virtualFile);
    }

    // view -> element
    List<String> renderElements = viewMethod.getRenderElements();

    fileSystem.createViewPopupActions(renderElements,
        group,
        directorySystem.getCakeConfig().cakeVersionAbsorption.get(CakeIdentifier.Element),
        CakeIdentifier.Element.toString());
  }

  // ControllerからView, Testなどジャンプ対象のファイルを追加
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
}
