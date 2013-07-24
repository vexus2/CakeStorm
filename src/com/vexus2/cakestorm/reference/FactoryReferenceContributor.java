package com.vexus2.cakestorm.reference;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SmartList;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpCallbackFunctionUtil;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import com.jetbrains.php.lang.patterns.PhpPatterns;

/**
 * @author Enrique Piatti
 */
public class FactoryReferenceContributor extends PsiReferenceContributor {


  @Override
  public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
    psiReferenceRegistrar.registerReferenceProvider(
//        PlatformPatterns.psiElement(PhpElementTypes.VARIABLE).withText("this"),
        PlatformPatterns.psiElement(PhpElementTypes.METHOD_REFERENCE).withChild(PlatformPatterns
            .psiElement(PhpElementTypes.PARAMETER_LIST)
        ),
        new PsiReferenceProvider() {
          @NotNull
          @Override
          public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
          /*  PsiElement originalElement = getOriginalElement(psiElement);
            PsiReferenceBase ref = new PhpCallbackReference(psiElement, originalElement);
            PsiReference apsireference[];
            apsireference = (new PsiReference[]{
                ref
            });
            return apsireference;*/

            Pattern pattern = Pattern.compile("\\$uses.*?=.*?[array]*?\\((.*?)\\)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(psiElement.getContainingFile().getText());
            PsiReference psiReference;
            while (matcher.find()) {
              String[] modelNames = matcher.group(1).replaceAll("\\s|\\t|'|\"", "").split(",");

              for (String modelName : modelNames) {
                Collection<PhpClass> phpClasses = PhpIndex.getInstance(psiElement.getProject()).getClassesByFQN(modelName);

                PhpIndex phpIndex = PhpIndex.getInstance(psiElement.getProject());


                for (PhpClass phpClass : phpClasses) {
                  if (phpClass != null) {

//                    PsiReference ref = new com.vexus2.cakestorm.lib.ClassReference(phpClass.getOriginalElement(), phpClass.getContainingFile().getVirtualFile(), phpClass.getContainingFile().getVirtualFile().getPath());
//                    return new PsiReference[]{ref};


//                    Method arr$[] = phpClass.getOwnMethods();
//                    int len$ = arr$.length;
//                    for (int i$ = 0; i$ < len$; i$++) {
//                      Method m = arr$[i$];
//                      if (!PhpUnitUtil.isTestMethod(m))
//
//                    }

                  }
                }

              }

            }
            return PsiReference.EMPTY_ARRAY;
          }

          @Nullable
          private PsiElement getOriginalElement(@NotNull PsiElement selectedElement) {
            if (selectedElement == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalElement must not be null");
            FunctionReference functionReference = (FunctionReference) PhpPsiUtil.getParentByCondition(selectedElement, true, FunctionReference.INSTANCEOF);
            PsiElement selectedParameter = PhpCallbackFunctionUtil.getParentParameterElement(selectedElement);
            if ((functionReference == null) || (selectedParameter == null))
              return null;

            int selectedIndex = PhpCallbackFunctionUtil.getSelectedParameterIndex(selectedParameter);
            if (PhpCallbackFunctionUtil.isClassCallbackFunction(functionReference, selectedIndex)) {
              return getOriginalClass(selectedParameter, getCallerClass(functionReference));
            }

            if (PhpCallbackFunctionUtil.isMethodCallbackFunction(functionReference, selectedIndex)) {
              return getOriginalMethod(selectedParameter.getParent(), getCallerClass(functionReference));
            }

            if (PhpCallbackFunctionUtil.isFieldCallbackFunction(functionReference, selectedIndex)) {
              return getOriginalField(selectedParameter.getParent(), getCallerClass(functionReference));
            }

            if (PhpCallbackFunctionUtil.isFunctionCallbackFunction(functionReference, selectedIndex)) {
              PhpCallbackFunctionUtil.PhpCallbackElement callback = PhpCallbackFunctionUtil.constructCallback(functionReference.getName(), selectedParameter);
              if ((callback instanceof PhpCallbackFunctionUtil.PhpCallbackMemberElement)) {
                if (callback.getCallbackElement() == selectedElement) {
                  return getOriginalMethod(selectedParameter, getCallerClass(functionReference));
                }

                return getOriginalClass(selectedElement, getCallerClass(functionReference));
              }

              if (callback != null) {
                String callbackText = callback.getCallbackText();
                PhpNamedElement originalFunction = getOriginalFunction(selectedElement.getProject(), callbackText);
                return originalFunction == null ? getOriginalMethodByClass(callbackText, getCallerClass(functionReference)) : originalFunction;
              }
            }
            return null;
          }

          @Nullable
          private PhpClassMember getOriginalField(@NotNull PsiElement root, @Nullable PhpClass callerClass) {
            if (root == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalField must not be null");
            PsiElement[] children = root.getChildren();
            String propertyName;
            if (children.length >= 2) {
              Collection classes = resolveClass(PhpIndex.getInstance(root.getProject()), children[0], callerClass);
              propertyName = getCallbackContent(children[1]);
              if ((classes != null) && (StringUtil.isNotEmpty(propertyName))) {
                for (Object clazz : classes) {
                  PhpClass phpClass = (PhpClass) clazz;
                  Field field = phpClass.findFieldByName(propertyName, false);
                  if (field != null) {
                    return field;
                  }
                }
              }
            }
            return null;
          }

          @Nullable
          private PhpClassMember getOriginalMethod(@NotNull PsiElement root, @Nullable PhpClass callerClass) {
            if (root == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalMethod must not be null");
            PsiElement[] children = root.getChildren();
            String methodName;
            if (children.length >= 2) {
              Collection classes = resolveClass(PhpIndex.getInstance(root.getProject()), children[0], callerClass);
              methodName = getCallbackContent(children[1]);
              if ((classes != null) && (StringUtil.isNotEmpty(methodName))) {
                for (Object clazz : classes) {
                  PhpClass phpClass = (PhpClass) clazz;
                  Method method = phpClass.findMethodByName(methodName);
                  if (method != null) {
                    return method;
                  }
                }
              }
            }
            return null;
          }

          @Nullable
          private PhpClassMember getOriginalMethodByClass(@NotNull String methodName, @Nullable PhpClass clazz) {
            if (methodName == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalMethodByClass must not be null");
            if ((clazz != null) && (StringUtil.isNotEmpty(methodName))) {
              Method method = clazz.findMethodByName(methodName);
              if (method != null) {
                return method;
              }
            }
            return null;
          }

          @Nullable
          private PhpNamedElement getOriginalClass(@NotNull PsiElement element, @Nullable PhpClass callerClass) {
            if (element == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalClass must not be null");
            String className = PhpCallbackFunctionUtil.getCallbackString(element);
            if (className != null) {
              PhpClass classByKeyword = checkClassKeywords(className, callerClass);
              if (classByKeyword != null) {
                return classByKeyword;
              }

              PhpIndex index = PhpIndex.getInstance(element.getProject());
              List classes = new SmartList(index.getAnyByFQN(PhpLangUtil.toFQN(className)));
              if (classes.size() > 0) {
                return (PhpNamedElement) classes.get(0);
              }
            }
            return null;
          }

          @Nullable
          private PhpNamedElement getOriginalFunction(@NotNull Project project, @NotNull String fqn) {
            if (project == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalFunction must not be null");
            if (fqn == null)
              throw new IllegalArgumentException("Argument 1 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getOriginalFunction must not be null");
            PhpIndex index = PhpIndex.getInstance(project);
            if (StringUtil.isNotEmpty(fqn)) {
              int i = fqn.lastIndexOf('\\');
              String name = i >= 0 ? fqn.substring(i + 1) : fqn;
              String namespace = i >= 0 ? fqn.substring(0, i + 1) : "\\";

              Collection functions = index.getFunctionsByName(name);
              List filteredFunctions = new SmartList(index.filterNamedByNamespace(functions, PhpLangUtil.toFQN(namespace), true));

              if (filteredFunctions.size() > 0) {
                return (PhpNamedElement) filteredFunctions.get(0);
              }
            }
            return null;
          }

          @Nullable
          public Collection<PhpClass> resolveClass(@NotNull PhpIndex index, @NotNull PsiElement element, @Nullable PhpClass callerClass) {
            if (index == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.resolveClass must not be null");
            if (element == null)
              throw new IllegalArgumentException("Argument 1 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.resolveClass must not be null");
            PhpClass referencedClass = PhpCallbackFunctionUtil.getFirstClassByReference(element);
            if (referencedClass != null) {
              return new SmartList(referencedClass);
            }

            String callbackContent = getCallbackContent(element);
            PhpClass classByKeyword = checkClassKeywords(callbackContent, callerClass);
            if (classByKeyword != null) {
              return new SmartList(classByKeyword);
            }

            if (callbackContent == null) {
              PhpExpression classExpression = (element instanceof PhpExpression) ? (PhpExpression) element : (PhpExpression) PhpPsiUtil.getChildByCondition(element, PhpExpression.INSTANCEOF);

              callbackContent = PhpCallbackFunctionUtil.getClassFqnByExpression(classExpression);
            }
            return callbackContent == null ? null : index.getAnyByFQN(callbackContent);
          }

          @Nullable
          private PhpClass checkClassKeywords(@Nullable String callbackContent, @Nullable PhpClass callerClass) {
            if ((PhpLangUtil.equalsClassNames("self", callbackContent)) || (PhpLangUtil.equalsClassNames("static", callbackContent))) {
              return callerClass;
            }
            return (PhpLangUtil.equalsClassNames("parent", callbackContent)) && (callerClass != null) ? callerClass.getSuperClass() : null;
          }


          private String getCallbackContent(@NotNull PsiElement element) {
            if (element == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getCallbackContent must not be null");
            StringLiteralExpression str = (element instanceof StringLiteralExpression) ? (StringLiteralExpression) element : (StringLiteralExpression) PhpPsiUtil.getChildByCondition(element, StringLiteralExpression.INSTANCEOF);

            return str == null ? null : PhpCallbackFunctionUtil.getCallbackString(str);
          }

          @Nullable
          private PhpClass getCallerClass(@NotNull FunctionReference reference) {
            if (reference == null)
              throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/jetbrains/php/lang/PhpReferenceContributor$CallbackReferenceProvider.getCallerClass must not be null");
            return (PhpClass) PhpPsiUtil.getParentByCondition(reference.getElement(), true, PhpClass.INSTANCEOF);
          }

        }
    );
  }


}
