package com.vexus2.cakestorm.lib;


import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(
    name = "CakeStormCakeSettings",
    storages = {
        @Storage(id = "default", file = "$PROJECT_FILE$"),
        @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/cake-config.xml", scheme = StorageScheme.DIRECTORY_BASED)
    }
)


public class CakeConfig implements PersistentStateComponent<CakeConfig> {
  public Map<CakeIdentifier, String> cakeVersionAbsorption = new HashMap<>();
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

}
