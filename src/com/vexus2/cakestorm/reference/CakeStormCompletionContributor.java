package com.vexus2.cakestorm.reference;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.completion.PhpLookupElement;
import com.jetbrains.php.completion.insert.PhpReferenceInsertHandler;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpClassIndex;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpFunctionIndex;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: Not using this class.
 */
public class CakeStormCompletionContributor extends CompletionContributor {

  public CakeStormCompletionContributor() {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement().afterLeaf ("->"), new PhpControllerCompletionProvider());
  }

  private class PhpControllerCompletionProvider extends CompletionProvider<CompletionParameters> {

    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
      if (parameters == null)
        throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/completion/PhpCompletionContributor$PhpControllerCompletionProvider.addCompletions must not be null");
      if (result == null)
        throw new IllegalArgumentException("Argument 2 for @NotNull parameter of com/jetbrains/php/completion/PhpCompletionContributor$PhpControllerCompletionProvider.addCompletions must not be null");

      Pattern pattern = Pattern.compile("\\$uses.*?=.*?[array]*?\\((.*?)\\)", Pattern.DOTALL);
      Matcher matcher = pattern.matcher(parameters.getOriginalFile().getText());
      PsiElement position = parameters.getPosition().getOriginalElement();

      while (matcher.find()) {
        String[] modelNames = matcher.group(1).replaceAll("\\s|\\t|'|\"", "").split(",");

        for (String modelName : modelNames) {
          Collection<PhpClass> containingClasses = PhpIndex.getInstance(parameters.getOriginalFile().getProject()).getClassesByFQN(modelName);

          result.addElement(new PhpLookupElement(modelName, PhpClassIndex.KEY, position.getProject(), PhpReferenceInsertHandler.getInstance()));


          for (PhpClass containingClass : containingClasses) {
            if (containingClass != null) {
              Method arr$[] = containingClass.getOwnMethods();
              int len$ = arr$.length;
              for (int i$ = 0; i$ < len$; i$++) {
                Method m = arr$[i$];
                if (!PhpUnitUtil.isTestMethod(m))
                  result.addElement(new PhpLookupElement(m.getName(), PhpFunctionIndex.KEY, position.getProject(), AppendSemiColonInsertHandler.getInstance()));
              }

            }
          }

        }

      }

    }

  }

  private static class AppendSemiColonInsertHandler implements InsertHandler<LookupElement>
  {
    private static final AppendSemiColonInsertHandler INSTANCE = new AppendSemiColonInsertHandler();

    public static AppendSemiColonInsertHandler getInstance() {
      return INSTANCE;
    }
    @Override
    public void handleInsert (InsertionContext context, LookupElement lookupElement)
    {
      Document document = context.getDocument();
      Editor editor = context.getEditor();
      CaretModel caretModel = editor.getCaretModel();
      CharSequence cs = document.getText ();
      int offset = caretModel.getOffset();

      for (int i = offset; i < cs.length (); i++) {
        if (cs.charAt (i) == ';') return;
        if ( ! Character.isWhitespace (cs.charAt (i))) break;
      }

      document.insertString (offset, ";");
      caretModel.moveToOffset (offset + 1);
    }
  }

}