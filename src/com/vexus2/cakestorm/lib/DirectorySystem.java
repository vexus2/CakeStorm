package com.vexus2.cakestorm.lib;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DirectorySystem {

  private static VirtualFile appPath;

  @Nullable
  private CakeConfig cakeConfig;

  public DirectorySystem(Project project, VirtualFile vf, CakeIdentifier identifier) throws Exception {
    this.cakeConfig = CakeConfig.getInstance(project);
    if (this.cakeConfig.isEmpty()) {
      this.cakeConfig.init(vf, identifier);
    }
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
