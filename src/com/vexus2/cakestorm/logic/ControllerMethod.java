package com.vexus2.cakestorm.logic;


import com.intellij.openapi.util.TextRange;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerMethod {
  private String currentControllerName;
  private String currentActionName;
  private Map<String, Function> actions = new LinkedHashMap<String, Function>();

  public Map<String, Function> getActions() {
    return actions;
  }

  public List<String> getRenderActionsByMethodName(String methodName) {
    Function f = this.actions.get(methodName);
    return f.getRenderViews();
  }

  public ControllerMethod(PhpClass phpClass, int startOffset) {
    this.currentControllerName = phpClass.getName();
    Pattern pattern = Pattern.compile(".*?this->render\\((.*?)[\\)|.*?,].*?");
    Map<String, Function> tmpMap = new HashMap<String, Function>();

    for (Method method : phpClass.getOwnMethods()) {
      TextRange textRange = method.getTextRange();
      final String methodName = method.getName();
      Matcher matcher = pattern.matcher(method.getText());
      List<String> renderViews = new ArrayList<String>() {{
        add(methodName);
      }};

      while (matcher.find()) {
        String actionName = matcher.group(1).replaceAll("\\s|\\t|'|\"", "");
        // Don't add duplicate action.
        if (renderViews.indexOf(actionName) == -1) {
          renderViews.add(actionName);
        }
      }

      if (textRange.getStartOffset() <= startOffset && startOffset <= textRange.getEndOffset()) {
        this.currentActionName = methodName;
        // Current action will appear at the top of the list.
        actions.put(methodName, new Function(method.getName(), textRange, renderViews));
      } else {
        // Method name will be unique. Because PHP doesn't have [overroad] like java.
        tmpMap.put(methodName, new Function(method.getName(), textRange, renderViews));
      }
    }
    for (Map.Entry<String, Function> e : tmpMap.entrySet()) {
      actions.put(e.getKey(), e.getValue());
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

  public void setActions(Map<String, Function> actions) {
    this.actions = actions;
  }
}
