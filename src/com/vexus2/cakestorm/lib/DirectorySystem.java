package com.vexus2.cakestorm.lib;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


public class DirectorySystem {

  private static VirtualFile appPath;

  @Nullable
  private CakeConfig cakeConfig;


  public DirectorySystem(Project project, VirtualFile vf, ClassType type) throws Exception {
    this.cakeConfig = CakeConfig.getInstance(project);
    if (this.cakeConfig != null && this.cakeConfig.cakeVersion != null)
      return;
    int version = checkVersion(vf, type);
    setDifferences(version);
  }

  private void setDifferences(int version) {
    //TODO: add lib pattern.
    HashMap<CakeIdentifier, String> cakeVersionAbsorption;
    if (version == 1) {
      cakeVersionAbsorption = new HashMap<CakeIdentifier, String>() {
        {
          put(CakeIdentifier.Controller, "/controllers/");
          put(CakeIdentifier.View, "/views/");
          put(CakeIdentifier.Model, "/models/");
          put(CakeIdentifier.Helper, "/views/helpers/");
          put(CakeIdentifier.Component, "/controllers/components/");
          put(CakeIdentifier.Behavior, "/models/behaviors/");
          put(CakeIdentifier.Shell, "/vendors/shell");
          put(CakeIdentifier.Task, "/vendors/Shell/Task");
          put(CakeIdentifier.ControllerTest, "/tests/cases/controllers/");
          put(CakeIdentifier.ModelTest, "/tests/cases/models/");
          put(CakeIdentifier.BehaviorTest, "/tests/cases/behaviors/");
          put(CakeIdentifier.ComponentTest, "/tests/cases/components/");
          put(CakeIdentifier.HelperTest, "/tests/cases/helpers/");
          put(CakeIdentifier.Fixture, "/tests/fixtures/");
          put(CakeIdentifier.TestFile, "test");
          put(CakeIdentifier.FileSeparator, ".");
          put(CakeIdentifier.FileWordSeparator, "_");
        }
      };
    } else {
      cakeVersionAbsorption = new HashMap<CakeIdentifier, String>() {
        {
          put(CakeIdentifier.Controller, "/Controller/");
          put(CakeIdentifier.View, "/View/");
          put(CakeIdentifier.Model, "/Model/");
          put(CakeIdentifier.Helper, "/View/Helper/");
          put(CakeIdentifier.Component, "/Controller/Component/");
          put(CakeIdentifier.Behavior, "/Model/Behavior/");
          put(CakeIdentifier.Shell, "/Vendor/Shell");
          put(CakeIdentifier.Task, "/Vendor/Shell/Task");
          put(CakeIdentifier.ControllerTest, "/Test/Case/Controller/");
          put(CakeIdentifier.ModelTest, "/Test/Case/Model/");
          put(CakeIdentifier.BehaviorTest, "/Test/Case/Model/Behavior/");
          put(CakeIdentifier.ComponentTest, "/Test/Case/Controller/Component/");
          put(CakeIdentifier.HelperTest, "/Test/Case/View/Helper/");
          put(CakeIdentifier.Fixture, "/Test/Fixture/");
          put(CakeIdentifier.TestFile, "Test");
          put(CakeIdentifier.FileSeparator, "");
          put(CakeIdentifier.FileWordSeparator, "");
        }
      };
    }

    cakeConfig.cakeVersionAbsorption = cakeVersionAbsorption;
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
        version = currentFileName.matches(".*?views.*?") ? 1 : 2;
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
        version = currentFileName.matches(".*?fixtures.*?") ? 1 : 2;
        break;
      case Library:
        version = currentFileName.matches(".*?libs.*?") ? 1 : 2;
        break;
      default:
        throw new Exception("File type is not defined.");
    }
    cakeConfig.cakeVersion = version;
    return version;
  }

  public VirtualFile getAppPath() {
    return appPath;
  }

  public String getCanonicalAppPath() {
    return appPath.toString().replace("file://", "");
  }


  public void setAppPath(VirtualFile appPath) {
    DirectorySystem.appPath = appPath;
  }


  public String getPath(CakeIdentifier cakeIdentifier, String betweenDirectory, String fileNameWithoutExtension) {
    String fileExtension = (cakeIdentifier == CakeIdentifier.View) ? FileSystem.FILE_EXTENSION_TEMPLATE : FileSystem.FILE_EXTENSION_PHP;
    String coreDir = cakeConfig.cakeVersionAbsorption.get(cakeIdentifier);
    if (cakeIdentifier.toString().matches(".*?(?i)test.*?")) {
      if (cakeIdentifier.toString().matches(".*?(?i)(helper|behavior).*?")) {
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("helper|behavior", "", fileNameWithoutExtension) + cakeConfig.cakeVersionAbsorption.get(CakeIdentifier.TestFile) + fileExtension;
      } else {
        return coreDir + betweenDirectory + fileNameWithoutExtension + cakeConfig.cakeVersionAbsorption.get(CakeIdentifier.FileSeparator) + cakeConfig.cakeVersionAbsorption.get(CakeIdentifier.TestFile) + fileExtension;
      }
    } else {
      if (cakeIdentifier.toString().matches(".*?(?i)(helper|behavior).*?")) {
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("test|\\.", "", fileNameWithoutExtension) + cakeIdentifier.toString() + fileExtension;
      }else{
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("test|\\.", "", fileNameWithoutExtension) + fileExtension;
      }
    }
  }

  public String getBetweenDirectoryPath(String betweenDirectory) {
    if (betweenDirectory != null) {
      if (cakeConfig.cakeVersion == 1) {
        betweenDirectory = betweenDirectory.toLowerCase();
      }
    }
    betweenDirectory = Utility.replaceAllIgnoreCase("Controller|_", "", betweenDirectory);
    return betweenDirectory + "/";
  }

  public String convertControllerName(String name) {
    if (cakeConfig.cakeVersion == 1) {
      //TODO: use inflector to convert singularize and pluralize.
      name = name + cakeConfig.cakeVersionAbsorption.get(CakeIdentifier.FileWordSeparator) + "Controller";
      name = Utility.camelToSnake(name);
    } else {
      name = name + cakeConfig.cakeVersionAbsorption.get(CakeIdentifier.FileWordSeparator) + "Controller";
    }
    return name;
  }

  public CakeConfig getCakeConfig() {
    return cakeConfig;
  }
}
