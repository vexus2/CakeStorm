package com.vexus2.cakestorm.lib;


import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(
    name = "CakeStormCakeSettings",
    reloadable = true,
    storages = {
        @Storage(id = "default", file = "$PROJECT_FILE$"),
        @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/cake_config_setting_v0_5.xml", scheme = StorageScheme.DIRECTORY_BASED)
    }
)


public class CakeConfig implements PersistentStateComponent<CakeConfig> {
  public Map<CakeIdentifier, String> cakeVersionAbsorption = new HashMap<CakeIdentifier, String>();
  public Integer cakeVersion;

  @Nullable
  @Override
  public CakeConfig getState() {
    return this;
  }

  @Override
  public void loadState(CakeConfig cakeConfig) {
    XmlSerializerUtil.copyBean(cakeConfig, this);
  }

  @Nullable
  public static CakeConfig getInstance(Project project) {
    return ServiceManager.getService(project, CakeConfig.class);
  }

  public boolean isEmpty() {
    return cakeVersion == null;
  }

  public void init(VirtualFile vf, CakeIdentifier identifier) {
    if (cakeVersion == null) {
      int version = checkVersion(vf, identifier);
      setDifferences(version);
    }
  }

  public String getPath(CakeIdentifier cakeIdentifier, String betweenDirectory, String fileNameWithoutExtension, String pluginDir) {
    String fileExtension = (cakeIdentifier == CakeIdentifier.View) ? FileSystem.FILE_EXTENSION_TEMPLATE : FileSystem.FILE_EXTENSION_PHP;
    String coreDir = cakeVersionAbsorption.get(cakeIdentifier);
    if (cakeIdentifier.toString().matches(".*?(?i)test.*?")) {
      String dir = ((pluginDir != null) ? "/" + cakeVersionAbsorption.get(CakeIdentifier.Plugin) + pluginDir : "" ) + coreDir + betweenDirectory;
      if (cakeIdentifier.toString().matches(".*?(?i)(helper|behavior).*?")) {
        return dir + Utility.replaceAllIgnoreCase("helper|behavior", "", fileNameWithoutExtension) + cakeVersionAbsorption.get(CakeIdentifier.TestFile) + fileExtension;
      } else {
        return dir + betweenDirectory + fileNameWithoutExtension + cakeVersionAbsorption.get(CakeIdentifier.FileSeparator) + cakeVersionAbsorption.get(CakeIdentifier.TestFile) + fileExtension;
      }
    } else {
      String dir = ((pluginDir != null) ? "/" + cakeVersionAbsorption.get(CakeIdentifier.Plugin) + pluginDir : "" ) + coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("_?test|_?fixture|\\.", "", fileNameWithoutExtension);
      if (cakeIdentifier.toString().matches(".*?(?i)(helper|behavior).*?")) {
        return dir + cakeIdentifier.toString() + fileExtension;
      } else if (cakeIdentifier.toString().matches(".*?(?i)(fixture).*?")) {
        return dir + cakeVersionAbsorption.get(CakeIdentifier.FileWordSeparator) + cakeVersionAbsorption.get(CakeIdentifier.FixtureFile) + fileExtension;
      } else {
        return dir + fileExtension;
      }
    }
  }

  public String getBetweenDirectoryPath(String betweenDirectory) {
    if (betweenDirectory != null) {
      if (cakeVersion == 1) {
        betweenDirectory = Utility.camelToSnake(betweenDirectory);
      }
    }
    betweenDirectory = Utility.replaceAllIgnoreCase("Controller|\\.php", "", betweenDirectory).replaceAll("_\\z", "");
    return betweenDirectory + "/";
  }

  public String convertControllerName(String name) {
    if (cakeVersion == 1) {
      //TODO: use inflector to convert singularize and pluralize.
      name = name + cakeVersionAbsorption.get(CakeIdentifier.FileWordSeparator) + "Controller";
      name = Utility.camelToSnake(name);
    } else {
      name = name + cakeVersionAbsorption.get(CakeIdentifier.FileWordSeparator) + "Controller";
    }
    return name;
  }

  // TODO: add version3 file path
  private void setDifferences(int version) {
    HashMap<CakeIdentifier, String> identifierStringHashMap;
    if (version == 1) {
      identifierStringHashMap = new HashMap<CakeIdentifier, String>() {
        {
          put(CakeIdentifier.Controller, "/controllers/");
          put(CakeIdentifier.View, "/views/");
          put(CakeIdentifier.Model, "/models/");
          put(CakeIdentifier.Element, "elements/");
          put(CakeIdentifier.Layout, "layouts/");
          put(CakeIdentifier.Helper, "/views/helpers/");
          put(CakeIdentifier.Component, "/controllers/components/");
          put(CakeIdentifier.Behavior, "/models/behaviors/");
          put(CakeIdentifier.Plugin, "plugins/");
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
      identifierStringHashMap = new HashMap<CakeIdentifier, String>() {
        {
          put(CakeIdentifier.Controller, "/Controller/");
          put(CakeIdentifier.View, "/View/");
          put(CakeIdentifier.Model, "/Model/");
          put(CakeIdentifier.Element, "Elements/");
          put(CakeIdentifier.Layout, "Layouts/");
          put(CakeIdentifier.Helper, "/View/Helper/");
          put(CakeIdentifier.Component, "/Controller/Component/");
          put(CakeIdentifier.Behavior, "/Model/Behavior/");
          put(CakeIdentifier.Plugin, "Plugin/");
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

    this.cakeVersionAbsorption = identifierStringHashMap;
  }

  private int checkVersion(VirtualFile vf, CakeIdentifier identifier) {
    if (identifier == null) {
      identifier = CakeIdentifier.Other;
    }
    String currentFileName = vf.toString();
    //TODO: set version = 3 when identifier include 'src' directory
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
        version = vf.getName().matches("[a-z\\p{Punct}]+") ? 1 : 2;
    }
    this.cakeVersion = version;
    return version;
  }


}
