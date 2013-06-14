package com.vexus2.cakestorm.ide;


import com.intellij.openapi.vfs.VirtualFile;

import java.util.HashMap;
import java.util.Map;

public class DirectoryManager {

  private static Map<FilePath, String> directoryMap;
  private static Integer version;
  private static String nameSeparator = "";
  private static VirtualFile appPath;


  public DirectoryManager(VirtualFile vf, ClassType type) {
    String currentFileName = vf.toString();
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
    }

    if (version == 1) {
      nameSeparator = ".";
      directoryMap = new HashMap<FilePath, String>() {
        {
          put(FilePath.Controller, "/controllers/");
          put(FilePath.View, "/views/");
          put(FilePath.Model, "/models/");
          put(FilePath.Helper, "/views/helpers/");
          put(FilePath.Component, "/controllers/components/");
          put(FilePath.Behavior, "/models/behaviors/");
          put(FilePath.Shell, "/vendors/shell");
          put(FilePath.Task, "/vendors/Shell/Task");
          put(FilePath.ControllerTest, "//tests/controllers/");
          put(FilePath.ModelTest, "/tests/cases/models/");
          put(FilePath.BehaviorTest, "/tests/cases/behaviors/");
          put(FilePath.ComponentTest, "/tests/cases/cmponents/");
          put(FilePath.HelperTest, "/tests/cases/helpers/");
          put(FilePath.Fixture, "/tests/fixtures/");
        }
      };
    } else {
      directoryMap = new HashMap<FilePath, String>() {
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
        }
      };
    }

  }

  public Map<FilePath, String> getDirectoryMap() {
    return directoryMap;
  }

  public VirtualFile getAppPath() {
    return appPath;
  }

  public void setAppPath(VirtualFile appPath) {
    DirectoryManager.appPath = appPath;
  }
}
