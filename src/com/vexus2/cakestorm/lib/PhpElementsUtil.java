package com.vexus2.cakestorm.lib;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PhpElementsUtil {
  @Nullable
  public static PhpClass getFirstClassFromFile(PsiFile phpFile) {
    Collection<PhpClass> phpClasses = PsiTreeUtil.collectElementsOfType(phpFile, PhpClass.class);
    return phpClasses.size() == 0 ? null : phpClasses.iterator().next();
  }
}
