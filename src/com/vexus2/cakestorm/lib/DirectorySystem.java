package com.vexus2.cakestorm.lib;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


public class DirectorySystem {

  private static VirtualFile appPath;

  @Nullable
  private CakeConfig cakeConfig;


  public DirectorySystem(Project project, VirtualFile vf, CakeIdentifier identifier) throws Exception {
    this.cakeConfig = CakeConfig.getInstance(project);
    if (this.cakeConfig != null && this.cakeConfig.cakeVersion != null)
      return;
    int version = checkVersion(vf, identifier);
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
          put(CakeIdentifier.Element, "elements/");
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
          put(CakeIdentifier.FixtureFile, "fixture");
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
          put(CakeIdentifier.Element, "Elements/");
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
          put(CakeIdentifier.FixtureFile, "Fixture");
          put(CakeIdentifier.FileSeparator, "");
          put(CakeIdentifier.FileWordSeparator, "");
        }
      };
    }

    cakeConfig.cakeVersionAbsorption = cakeVersionAbsorption;
  }

  private int checkVersion(VirtualFile vf, CakeIdentifier identifier) throws Exception {
    String currentFileName = vf.toString();
    int version = 0;
    switch (identifier) {
      case Controller:
      case ControllerTest:
      case Component:
      case ComponentTest:
        version = currentFileName.matches(".*?controllers.*?") ? 1 : 2;
        break;
      case View:
      case Helper:
      case HelperTest:
        version = currentFileName.matches(".*?views.*?") ? 1 : 2;
        break;
      case Model:
      case ModelTest:
      case Behavior:
      case BehaviorTest:
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


  public CakeConfig getCakeConfig() {
    return cakeConfig;
  }
}
