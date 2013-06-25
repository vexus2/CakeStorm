package com.vexus2.cakestorm.logic;


import com.intellij.openapi.util.TextRange;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerAction {
  private String currentControllerName;
  private String currentActionName;
  private Map<String, Function> actions = new HashMap<>();

  public Map<String, Function> getActions() {
    return actions;
  }

  public List<String> getRenderActionsByMethodName(String methodName) {
    Function f = this.actions.get(methodName);
    return f.getRenderViews();
  }

  public ControllerAction(PhpClass phpClass, int startOffset) {
    this.currentControllerName = phpClass.getName();
    Pattern pattern = Pattern.compile(".*?this->render\\((.*?)\\).*?");

    for (Method method : phpClass.getOwnMethods()) {
      TextRange textRange = method.getTextRange();
      final String methodName = method.getName();
      Matcher matcher = pattern.matcher(method.getText());
      List<String> renderViews = new ArrayList<String>() {{
        add(methodName);
      }};

      while (matcher.find()) {
        //TODO: シングルクォート、ダブルクォートを取り除く
        renderViews.add(matcher.group(1).replace("'", ""));
      }

      if (textRange.getStartOffset() <= startOffset && startOffset <= textRange.getEndOffset()) {
        this.currentActionName = methodName;
      }

      // Method name will be unique. Because PHP doesn't have [overroad] like java.
      actions.put(methodName, new Function(method.getName(), textRange, renderViews));
    }
  }

  public Function getCurrentAction() {
    return this.actions.get(this.currentActionName);
  }

  public String getCurrentActionName() {
    return currentActionName;
  }

  public String getCurrentControllerName() {
    return currentControllerName;
  }
}
