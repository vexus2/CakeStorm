package com.vexus2.cakestorm.logic;

import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewMethod {
  private List<String> renderElements;
  private String currentActionName;

  public ViewMethod(PsiFile psiFile) {
    this.currentActionName = psiFile.getName();
    Pattern pattern = Pattern.compile(".*?this->element\\((.*?)[\\)|.*?,].*?");

    Matcher matcher = pattern.matcher(psiFile.getText());
    List<String> renderElements = new ArrayList<String>();

    while (matcher.find()) {
      String actionName = matcher.group(1).replaceAll("\\n|\\s|\\t|'|\"", "");
      // Don't add duplicate action.
      if (renderElements.indexOf(actionName) == -1) {
        renderElements.add(actionName);
      }
    }
    this.renderElements = renderElements;
  }

  public List<String> getRenderElements() {
    return renderElements;
  }

  public String getCurrentActionName() {
    return currentActionName;
  }
}
