package com.vexus2.cakestorm.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.vexus2.cakestorm.lib.CakeConfig;
import com.vexus2.cakestorm.lib.CakeIdentifier;
import com.vexus2.cakestorm.lib.ClassReference;
import com.vexus2.cakestorm.lib.FileSystem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringLiteralReferenceProvider extends PsiReferenceProvider {

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
    String cursorText = psiElement.getText();

    int indexOf = cursorText.indexOf(".");
    String jumpFileName = (indexOf != -1) ? cursorText.substring(0, cursorText.indexOf(".")) : cursorText;
    jumpFileName = jumpFileName.replaceAll("\\s|\\t|'|\"", "");
    TextRange textRange = getTextRange(cursorText, jumpFileName);
    if (jumpFileName.isEmpty())
      return PsiReference.EMPTY_ARRAY;

    Collection<PhpClass> phpClasses = PhpIndex.getInstance(psiElement.getProject()).getClassesByFQN(jumpFileName);

    if (phpClasses.isEmpty()) {
      CakeConfig cakeConfig = CakeConfig.getInstance(psiElement.getProject());
      if (cakeConfig.isEmpty()) {
        VirtualFile tmpVirtualFile = psiElement.getContainingFile().getVirtualFile();
        cakeConfig.init(tmpVirtualFile, CakeIdentifier.getIdentifier(tmpVirtualFile));
      }
      VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();
      if (virtualFile == null)
        return PsiReference.EMPTY_ARRAY;
      String controllerName = cakeConfig.getBetweenDirectoryPath(virtualFile.getName());
      String filePath = cakeConfig.getPath(CakeIdentifier.View, controllerName, jumpFileName);
      virtualFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(FileSystem.getAppPath(psiElement.getContainingFile().getVirtualFile()) + filePath);
      if (virtualFile == null) {
        virtualFile = getVirtualFile(psiElement, jumpFileName, cakeConfig, CakeIdentifier.Element);
      }
      if (virtualFile != null) {
        PsiReference ref = new ClassReference(
            virtualFile,
            cursorText.substring(textRange.getStartOffset(), textRange.getEndOffset()),
            psiElement,
            textRange,
            psiElement.getProject(),
            virtualFile);
        return new PsiReference[]{ref};
      }
      return PsiReference.EMPTY_ARRAY;
    }

    ArrayList<PsiReference> refList = new ArrayList<PsiReference>();

    for (PhpClass phpClass : phpClasses) {
      if (phpClass != null) {
        PsiReference ref = new ClassReference(
            phpClass.getContainingFile().getVirtualFile(),
            cursorText.substring(textRange.getStartOffset(), textRange.getEndOffset()),
            psiElement,
            textRange,
            psiElement.getProject(),
            phpClass.getContainingFile().getVirtualFile());

        refList.add(ref);
      }
    }
    PsiReference[] psiReference = new PsiReference[refList.size()];
    int i = 0;
    for (PsiReference ref : refList) {
      psiReference[i] = ref;
      i++;
    }
    return psiReference;

  }

  private VirtualFile getVirtualFile(PsiElement psiElement, String jumpFileName, CakeConfig cakeConfig, CakeIdentifier identifier) {
    String controllerName = cakeConfig.cakeVersionAbsorption.get(identifier);
    String filePath = cakeConfig.getPath(CakeIdentifier.View, controllerName, jumpFileName);
    VirtualFile virtualFile = VirtualFileManager.getInstance().refreshAndFindFileByUrl(FileSystem.getAppPath(psiElement.getContainingFile().getVirtualFile()) + filePath);
    return (virtualFile == null) ? (identifier == CakeIdentifier.Element) ? getVirtualFile(psiElement, jumpFileName, cakeConfig, CakeIdentifier.Layout) : null : virtualFile;
  }

  public static TextRange getTextRange(String originalStr, String str) {
    try {
      TextRange textRange = null;
      Pattern p = Pattern.compile(str);
      Matcher m = p.matcher(originalStr);
      if (m.find()) {
        textRange = new TextRange(m.start(), m.end());
      }
      return textRange;
    } catch (Exception e) {
      return null;
    }
  }

}
