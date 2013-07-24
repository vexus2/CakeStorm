package com.vexus2.cakestorm.lib;


import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(
    name = "CakeStormCakeSettings",
    reloadable = true,
    storages = {
        @Storage(id = "default", file = "$PROJECT_FILE$"),
        @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/cake-config_0.2.xml", scheme = StorageScheme.DIRECTORY_BASED)
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

  public String getPath(CakeIdentifier cakeIdentifier, String betweenDirectory, String fileNameWithoutExtension) {
    String fileExtension = (cakeIdentifier == CakeIdentifier.View) ? FileSystem.FILE_EXTENSION_TEMPLATE : FileSystem.FILE_EXTENSION_PHP;
    String coreDir = cakeVersionAbsorption.get(cakeIdentifier);
    if (cakeIdentifier.toString().matches(".*?(?i)test.*?")) {
      if (cakeIdentifier.toString().matches(".*?(?i)(helper|behavior).*?")) {
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("helper|behavior", "", fileNameWithoutExtension) + cakeVersionAbsorption.get(CakeIdentifier.TestFile) + fileExtension;
      } else {
        return coreDir + betweenDirectory + fileNameWithoutExtension + cakeVersionAbsorption.get(CakeIdentifier.FileSeparator) + cakeVersionAbsorption.get(CakeIdentifier.TestFile) + fileExtension;
      }
    } else {
      if (cakeIdentifier.toString().matches(".*?(?i)(helper|behavior).*?")) {
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("_?test|_?fixture|\\.", "", fileNameWithoutExtension) + cakeIdentifier.toString() + fileExtension;
      } else if (cakeIdentifier.toString().matches(".*?(?i)(fixture).*?")) {
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("_?test|_?fixture|\\.", "", fileNameWithoutExtension) + cakeVersionAbsorption.get(CakeIdentifier.FileWordSeparator) + cakeVersionAbsorption.get(CakeIdentifier.FixtureFile) + fileExtension;
      } else {
        return coreDir + betweenDirectory + Utility.replaceAllIgnoreCase("_?test|_?fixture|\\.", "", fileNameWithoutExtension) + fileExtension;
      }
    }
  }

  public String getBetweenDirectoryPath(String betweenDirectory) {
    if (betweenDirectory != null) {
      if (cakeVersion == 1) {
        betweenDirectory = betweenDirectory.toLowerCase();
      }
    }
    betweenDirectory = Utility.replaceAllIgnoreCase("Controller|_|\\.php", "", betweenDirectory);
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


}
