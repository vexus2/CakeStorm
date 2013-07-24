package com.vexus2.cakestorm.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

public class CakeStormReferenceContributor extends PsiReferenceContributor {

  @Override
  public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
    psiReferenceRegistrar.registerReferenceProvider(
        PlatformPatterns.psiElement(StringLiteralExpression.class).withLanguage(PhpLanguage.INSTANCE),
        new StringLiteralReferenceProvider()
    );

//    psiReferenceRegistrar.registerReferenceProvider(
//        PlatformPatterns.psiElement(StringLiteralExpression.class).withLanguage(PhpLanguage.INSTANCE),
//        new TemplateReferenceProvider()
//    );
  }


}

