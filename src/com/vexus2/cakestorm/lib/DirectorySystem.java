package com.vexus2.cakestorm.lib;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.HashMap;


public class DirectorySystem {

  private static VirtualFile appPath;

  private CakeConfig cakeConfig;


  public DirectorySystem(Project project, VirtualFile vf, ClassType type) throws Exception {
    this.cakeConfig = CakeConfig.getInstance(project);
    if (this.cakeConfig.getCakeVersion() != null) return;
    int version = checkVersion(vf, type);
    setDifferences(version);
  }

  private void setDifferences(int version) {
    HashMap<FilePath, String> cakeVersionAbsorption;
    if (version == 1) {
      cakeVersionAbsorption = new HashMap<FilePath, String>() {
        {
          put(FilePath.Controller, "/controllers/");
          put(FilePath.View, "/views/");
          put(FilePath.Model, "/models/");
          put(FilePath.Helper, "/views/helpers/");
          put(FilePath.Component, "/controllers/components/");
          put(FilePath.Behavior, "/models/behaviors/");
          put(FilePath.Shell, "/vendors/shell");
          put(FilePath.Task, "/vendors/Shell/Task");
          put(FilePath.ControllerTest, "/tests/cases/controllers/");
          put(FilePath.ModelTest, "/tests/cases/models/");
          put(FilePath.BehaviorTest, "/tests/cases/behaviors/");
          put(FilePath.ComponentTest, "/tests/cases/cmponents/");
          put(FilePath.HelperTest, "/tests/cases/helpers/");
          put(FilePath.Fixture, "/tests/fixtures/");
          put(FilePath.TestFile, "test");
          put(FilePath.FileSeparator, ".");
        }
      };
    } else {
      cakeVersionAbsorption = new HashMap<FilePath, String>() {
        {
          put(FilePath.Controller, "/Controller/");
          put(FilePath.View, "/View/");
          put(FilePath.Model, "/Model/");
          put(FilePath.Helper, "/View/Helper/");
          put(FilePath.Component, "/Controller/Component/");
          put(FilePath.Behavior, "/Model/Behavior/");
          put(FilePath.Shell, "/Vendor/Shell");
          put(FilePath.Task, "/Vendor/Shell/Task");
          put(FilePath.ControllerTest, "/Test/Case/Controller/");
          put(FilePath.ModelTest, "/Test/Case/Model/");
          put(FilePath.BehaviorTest, "/Test/Case/Model/Behavior/");
          put(FilePath.ComponentTest, "/Test/Case/Controller/Cmponent/");
          put(FilePath.HelperTest, "/Test/Case/View/Helper/");
          put(FilePath.Fixture, "/Test/Fixture/");
          put(FilePath.TestFile, "Test");
          put(FilePath.FileSeparator, "");
        }
      };
    }

    cakeConfig.setCakeVersionAbsorption(cakeVersionAbsorption);
  }

  private int checkVersion(VirtualFile vf, ClassType type) throws Exception {
    String currentFileName = vf.toString();
    int version = 0;
    switch (type) {
      case Controller:
      case ControllerTestCase:
      case Component:
      case ComponentTestCase:
        version = currentFileName.matches(".*?controllers.*?") ? 1 : 2;
        break;
      case View:
      case Helper:
      case HelperTestCase:
        version = currentFileName.matches(".*?controllers.*?") ? 1 : 2;
        break;
      case Model:
      case ModelTestCase:
      case Behavior:
      case BehaviorTestCase:
        version = currentFileName.matches(".*?models.*?") ? 1 : 2;
        break;
      case Shell:
      case Task:
        version = currentFileName.matches(".*?vendors.*?") ? 1 : 2;
        break;
      case Fixture:
        break;
      default:
        throw new Exception("File type is not defined.");
    }
    cakeConfig.setCakeVersion(version);
    return version;
  }

  public VirtualFile getAppPath() {
    return appPath;
  }

  public void setAppPath(VirtualFile appPath) {
    DirectorySystem.appPath = appPath;
  }


  public String getPath(FilePath filePath, String betweenDirectory, String fileNameWithoutExtension) {
    String fileExtension = (filePath == FilePath.View) ? FileSystem.FILE_EXTENSION_TEMPLATE : FileSystem.FILE_EXTENSION_PHP;
    if (filePath.toString().matches(".*?(?i)test.*?")) {
      return cakeConfig.getCakeVersionAbsorption().get(filePath) + betweenDirectory + fileNameWithoutExtension + cakeConfig.getCakeVersionAbsorption().get(FilePath.FileSeparator) + cakeConfig.getCakeVersionAbsorption().get(FilePath.TestFile) + fileExtension;
    } else {
      return cakeConfig.getCakeVersionAbsorption().get(filePath) + betweenDirectory + fileNameWithoutExtension + fileExtension;
    }
  }

  public String getBetweenDirectoryPath(String betweenDirectory) {
    if (betweenDirectory != null) {
      if (cakeConfig.getCakeVersion() == 1) {
        betweenDirectory = betweenDirectory.toLowerCase();
      }
    }
    betweenDirectory = Utility.replaceAllIgnoreCase("Controller|_", "", betweenDirectory);
    return betweenDirectory + "/";
  }


}
