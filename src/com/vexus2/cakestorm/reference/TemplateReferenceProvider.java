package com.vexus2.cakestorm.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class TemplateReferenceProvider extends PsiReferenceProvider {

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
    String cursorText = psiElement.getText();

    int indexOf = cursorText.indexOf(".");
    String jumpFileName = (indexOf != -1) ? cursorText.substring(0, cursorText.indexOf(".")) : cursorText;
    jumpFileName = jumpFileName.replaceAll("\\s|\\t|'|\"", "");
//    TextRange textRange = getTextRange(cursorText, jumpFileName);
    if (jumpFileName.isEmpty())
      return PsiReference.EMPTY_ARRAY;

    Collection<PhpClass> phpClasses = PhpIndex.getInstance(psiElement.getProject()).getClassesByFQN(jumpFileName);
    ArrayList<PsiReference> refList = new ArrayList<PsiReference>();

//    for (PhpClass phpClass : phpClasses) {
//      if (phpClass != null) {
//        PsiReference ref = new ClassReference(
//            phpClass.getContainingFile().getVirtualFile(),
//            cursorText.substring(textRange.getStartOffset(), textRange.getEndOffset()),
//            psiElement,
//            textRange,
//            psiElement.getProject(),
//            phpClass.getContainingFile().getVirtualFile());
//
//        refList.add(ref);
//      }
//    }
    PsiReference[] psiReference = new PsiReference[refList.size()];
    int i = 0;
    for (PsiReference ref : refList) {
      psiReference[i] = ref;
      i++;
    }
    return psiReference;

  }

}
