package com.vexus2.cakestorm.lib;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.jetbrains.php.PhpIcons;
import com.vexus2.cakestorm.logic.ControllerMethod;
import com.vexus2.cakestorm.logic.Function;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystem {

  public static final String FILE_EXTENSION_PHP = ".php";
  public static final String FILE_EXTENSION_TEMPLATE = ".ctp";

  private static VirtualFileManager virtualFileManager = null;
  private DirectorySystem directorySystem;

  private VirtualFile currentFile = null;

  // Current File
  private CakeIdentifier identifier = null;
  private Project project;
  private DataContext context;
  private OpenType openType;


  public FileSystem(VirtualFile vf, DataContext context) {
    virtualFileManager = VirtualFileManager.getInstance();
    this.context = context;
    this.currentFile = vf;
    this.identifier = CakeIdentifier.getIdentifier(vf);
  }

  public int filePopup(ControllerMethod controllerMethod) {
    Map<String, Function> currentActions = controllerMethod.getActions();
    DefaultActionGroup group = new DefaultActionGroup();
    String betweenDirectory = directorySystem.getCakeConfig().getBetweenDirectoryPath(controllerMethod.getCurrentControllerName());

    for (Map.Entry<String, Function> e : currentActions.entrySet()) {
      String actionName = e.getValue().getName();
      List<String> renderViews = e.getValue().getRenderViews();
      createViewPopupActions(renderViews, group, betweenDirectory, actionName);
    }

    if (group.getChildrenCount() != 0) {
      showPopup(group);
    }
    return group.getChildrenCount();
  }

  public int filePopup(@Nullable Function function, String controllerName) {
    if (function == null)
      return 0;
    List<String> renderViews = function.getRenderViews();
    DefaultActionGroup group = new DefaultActionGroup();
    String betweenDirectory = directorySystem.getCakeConfig().getBetweenDirectoryPath(controllerName);
    String actionName = function.getName();

    createViewPopupActions(renderViews, group, betweenDirectory, actionName);

    if (group.getChildrenCount() != 0) {
      showPopup(group);
    }

    return group.getChildrenCount();
  }

  public void showPopup(DefaultActionGroup group) {
    final ListPopup popup = JBPopupFactory.getInstance()
        .createActionGroupPopup("Open File",
            group,
            context,
            JBPopupFactory.ActionSelectionAid.NUMBERING,
            true);

    popup.showCenteredInCurrentWindow(project);
  }

  public void createViewPopupActions(List<String> renderViews, DefaultActionGroup group, String betweenDirectory, String actionName) {
    Boolean grouped = false;
    for (String templateName : renderViews) {
      String actionPath = directorySystem.getCakeConfig().getPath(CakeIdentifier.View, betweenDirectory, templateName);
      VirtualFile virtualFile = this.virtualFileBy(directorySystem.getAppPath().toString() + actionPath);
      if (virtualFile == null)
        continue;
      if (!grouped) {
        group.addSeparator(actionName);
        grouped = true;
      }
      addGroupChild(group, actionPath, virtualFile);
    }
  }

  public void addGroupChild(DefaultActionGroup group, final String actionPath, final VirtualFile virtualFile) {
    group.add(new AnAction(actionPath, virtualFile.getPath(), PhpIcons.PHP_FILE) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        VirtualFile fileByUrl = virtualFileManager.refreshAndFindFileByUrl("file://" + e.getPresentation().getDescription());
        FileEditorManagerEx fileEditorManagerEx = FileEditorManagerEx.getInstanceEx(e.getProject());
        switch (openType) {
          case VERTICAL:
            fileEditorManagerEx.createSplitter(1, EditorWindow.DATA_KEY.getData(e.getDataContext()));
            openNextTab(e.getDataContext(), fileByUrl);
            break;
          case HORIZONTAL:
            fileEditorManagerEx.createSplitter(1, EditorWindow.DATA_KEY.getData(e.getDataContext()));
            fileEditorManagerEx.changeSplitterOrientation();
            openNextTab(e.getDataContext(), fileByUrl);
            break;
          case DEFAULT:
            open(fileByUrl);
            break;
        }
      }

      private void openNextTab(DataContext dataContext, VirtualFile file) {
        final FileEditorManagerEx fileEditorManager = FileEditorManagerEx.getInstanceEx(project);
        final EditorWindow activeWindow = EditorWindow.DATA_KEY.getData(dataContext);
        if (activeWindow == null)
          return;
        final EditorWindow nextWindow = fileEditorManager.getNextWindow(activeWindow);
        if (nextWindow == null)
          return;
        nextWindow.getManager().openFileImpl2(nextWindow, file, true);
        nextWindow.closeAllExcept(file);
      }
    });
  }

  public CakeIdentifier getIdentifier() {
    return identifier;
  }

  public VirtualFile getCurrentFile() {
    return currentFile;
  }

  public VirtualFile getAppFile(VirtualFile currentFile) {
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
    return virtualFileManager.refreshAndFindFileByUrl(filePath);
  }

  @Nullable
  public void open(VirtualFile virtualFile) {
    new OpenFileDescriptor(project, virtualFile).navigate(true);
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public DirectorySystem getDirectorySystem() {
    return directorySystem;
  }

  public void setDirectorySystem(DirectorySystem directorySystem) {
    this.directorySystem = directorySystem;
  }

  public VirtualFile getVirtualFile(CakeIdentifier identifier) {
    String path = directorySystem.getCakeConfig().getPath(identifier, "", this.getCurrentFile().getNameWithoutExtension());
    return this.virtualFileBy(directorySystem.getAppPath().toString() + path);
  }

  public static String getAppPath(VirtualFile virtualFile) {
    String appDirPath = null;
    Pattern pattern = Pattern.compile("(.*?/app).*?");
    Matcher matcher = pattern.matcher(virtualFile.toString());

    if (matcher.find()) {
      appDirPath = matcher.group(1);
    }

    return appDirPath;
  }

  public void setOpenType(OpenType openType) {
    this.openType = openType;
  }
}
