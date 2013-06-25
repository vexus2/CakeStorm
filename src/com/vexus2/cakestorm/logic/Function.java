package com.vexus2.cakestorm.logic;

import com.intellij.openapi.util.TextRange;

import java.util.List;

public class Function {
  private String name;
  private TextRange textRange;
  private List<String> renderViews;

  public Function(String name, TextRange textRange, List<String> renderViews) {
    this.name = name;
    this.textRange = textRange;
    this.renderViews = renderViews;
  }

  public String getName() {
    return name;
  }

  public TextRange getTextRange() {
    return textRange;
  }

  public List<String> getRenderViews() {
    return renderViews;
  }
}
