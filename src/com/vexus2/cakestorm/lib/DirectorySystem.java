package com.vexus2.cakestorm.lib;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@State(
    name = "CakeStorm.Settings",
    storages = {
        @Storage(id = "CakeStormSettings", file = "$WORKSPACE_FILE$")
    }
)
public class DirectorySystem implements PersistentStateComponent<DirectorySystem.State> {

  private static VirtualFile appPath;

  private State myState = new State();


  public DirectorySystem(VirtualFile vf, ClassType type) throws Exception {
    if (myState.version != null) return;
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

    setCakeVersionAbsorption(cakeVersionAbsorption);
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
    setVersion(version);
    return version;
  }

  public VirtualFile getAppPath() {
    return appPath;
  }

  public void setAppPath(VirtualFile appPath) {
    DirectorySystem.appPath = appPath;
  }

  @Nullable
  @Override
  public State getState() {
    return myState;
  }

  @Override
  public void loadState(State state) {
    XmlSerializerUtil.copyBean(state, myState);
  }

  public class State {
    private Map<FilePath, String> cakeVersionAbsorption;
    private Integer version;
  }

  public void setCakeVersionAbsorption(Map<FilePath, String> cakeVersionDifferrence) {
    this.myState.cakeVersionAbsorption = cakeVersionDifferrence;
  }

  public void setVersion(Integer version) {
    this.myState.version = version;
  }

  public Integer getVersion() {
    return myState.version;
  }

  public Map<FilePath, String> getCakeVersionAbsorption() {
    return myState.cakeVersionAbsorption;
  }

  public String getPath(FilePath filePath, String betweenDirectory, String fileNameWithoutExtension) {
    String fileExtension = (filePath == FilePath.View) ? FileSystem.FILE_EXTENSION_TEMPLATE : FileSystem.FILE_EXTENSION_PHP;
    if (filePath.toString().matches(".*?(?i)test.*?")) {
      return getCakeVersionAbsorption().get(filePath) + betweenDirectory + fileNameWithoutExtension + getCakeVersionAbsorption().get(FilePath.FileSeparator) + getCakeVersionAbsorption().get(FilePath.TestFile) + fileExtension;
    } else {
      return getCakeVersionAbsorption().get(filePath) + betweenDirectory + fileNameWithoutExtension + fileExtension;
    }
  }

  public String getBetweenDirectoryPath(String betweenDirectory) {
    if (betweenDirectory != null) {
      if (myState.version == 1) {
        betweenDirectory = betweenDirectory.toLowerCase();
      }
    }
    betweenDirectory = Utility.replaceAllIgnoreCase("Controller|_", "", betweenDirectory);
    return betweenDirectory + "/";
  }



}
