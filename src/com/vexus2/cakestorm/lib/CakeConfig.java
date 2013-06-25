package com.vexus2.cakestorm.lib;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@State(
    name = "CakeStormCakeSettings",
    storages = {
        @Storage(
            id = "settings",
            file = "$APP_CONFIG$/cake-storm.xml"
        )
    }
)

public class CakeConfig implements PersistentStateComponent<CakeConfig> {
  private Map<FilePath, String> cakeVersionAbsorption;
  private Integer cakeVersion;

  @Nullable
  @Override
  public CakeConfig getState() {
    return this;
  }

  @Override
  public void loadState(CakeConfig cakeConfig) {
    XmlSerializerUtil.copyBean(cakeConfig, this);
  }

  public static CakeConfig getInstance(Project project) {
    return ServiceManager.getService(project, CakeConfig.class);
  }

  public Map<FilePath, String> getCakeVersionAbsorption() {
    return cakeVersionAbsorption;
  }

  public void setCakeVersionAbsorption(Map<FilePath, String> cakeVersionAbsorption) {
    this.cakeVersionAbsorption = cakeVersionAbsorption;
  }

  public Integer getCakeVersion() {
    return cakeVersion;
  }

  public void setCakeVersion(Integer cakeVersion) {
    this.cakeVersion = cakeVersion;
  }
}
