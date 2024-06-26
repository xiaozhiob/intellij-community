// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.psi.impl.source;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.Language;
import com.intellij.openapi.command.undo.BasicUndoableAction;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.file.impl.FileManager;
import com.intellij.psi.impl.source.resolve.JavaResolveUtil;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.JavaElementType;
import com.intellij.psi.scope.ElementClassHint;
import com.intellij.psi.scope.NameHint;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.scope.util.PsiScopesUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class PsiCodeFragmentImpl extends PsiFileImpl implements JavaCodeFragment, IntentionFilterOwner {
  private final PsiElement myContext;
  private boolean myPhysical;
  private PsiType myThisType;
  private PsiType mySuperType;
  private LinkedHashMap<String, String> myPseudoImports = new LinkedHashMap<>();
  private VisibilityChecker myVisibilityChecker;
  private ExceptionHandler myExceptionHandler;
  private GlobalSearchScope myResolveScope;
  private IntentionActionsFilter myIntentionActionsFilter;

  public PsiCodeFragmentImpl(Project project,
                             IElementType contentElementType,
                             boolean isPhysical,
                             @NonNls String name,
                             CharSequence text,
                             @Nullable PsiElement context) {
    super(TokenType.CODE_FRAGMENT,
          contentElementType,
          PsiManagerEx.getInstanceEx(project).getFileManager().createFileViewProvider(
            new LightVirtualFile(name, FileTypeManager.getInstance().getFileTypeByFileName(name), text), isPhysical)
    );
    myContext = context;
    ((SingleRootFileViewProvider)getViewProvider()).forceCachedPsi(this);
    myPhysical = isPhysical;
  }

  @Override
  public @NotNull Language getLanguage() {
    return getContentElementType().getLanguage();
  }

  @Override
  protected PsiCodeFragmentImpl clone() {
    final PsiCodeFragmentImpl clone = (PsiCodeFragmentImpl)cloneImpl((FileElement)calcTreeElement().clone());
    clone.myPhysical = false;
    clone.myOriginalFile = this;
    clone.myPseudoImports = new LinkedHashMap<>(myPseudoImports);
    FileManager fileManager = ((PsiManagerEx)getManager()).getFileManager();
    SingleRootFileViewProvider cloneViewProvider = (SingleRootFileViewProvider)fileManager.createFileViewProvider(new LightVirtualFile(
      getName(),
      getLanguage(),
      getText()), false);
    cloneViewProvider.forceCachedPsi(clone);
    clone.myViewProvider = cloneViewProvider;
    return clone;
  }

  private FileViewProvider myViewProvider;

  @Override
  public @NotNull FileViewProvider getViewProvider() {
    if (myViewProvider != null) return myViewProvider;
    return super.getViewProvider();
  }

  @Override
  public @NotNull FileType getFileType() {
    return JavaFileType.INSTANCE;
  }

  @Override
  public PsiElement getContext() {
    return myContext != null && myContext.isValid() ? myContext : super.getContext();
  }

  @Override
  public PsiType getThisType() {
    return myThisType;
  }

  @Override
  public void setThisType(PsiType psiType) {
    myThisType = psiType;
  }

  @Override
  public PsiType getSuperType() {
    return mySuperType;
  }

  @Override
  public void setSuperType(final PsiType superType) {
    mySuperType = superType;
  }

  @Override
  public String importsToString() {
    return StringUtil.join(myPseudoImports.values(), ",");
  }

  @Override
  public void addImportsFromString(String imports) {
    StringTokenizer tokenizer = new StringTokenizer(imports, ",");
    while (tokenizer.hasMoreTokens()) {
      String qName = tokenizer.nextToken();
      String name = PsiNameHelper.getShortClassName(qName);
      myPseudoImports.put(name, qName);
    }
  }

  @Override
  public void setVisibilityChecker(VisibilityChecker checker) {
    myVisibilityChecker = checker;
  }

  @Override
  public VisibilityChecker getVisibilityChecker() {
    return myVisibilityChecker;
  }

  @Override
  public boolean isPhysical() {
    return myPhysical;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JavaElementVisitor) {
      ((JavaElementVisitor)visitor).visitCodeFragment(this);
    }
    else {
      visitor.visitFile(this);
    }
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    final ElementClassHint classHint = processor.getHint(ElementClassHint.KEY);

    if (classHint == null || classHint.shouldProcess(ElementClassHint.DeclarationKind.CLASS)) {
      final NameHint nameHint = processor.getHint(NameHint.KEY);
      final String name = nameHint != null ? nameHint.getName(state) : null;
      if (name != null) {
        String qNameImported = myPseudoImports.get(name);
        if (qNameImported != null) {
          PsiClass imported = JavaPsiFacade.getInstance(myManager.getProject()).findClass(qNameImported, getResolveScope());
          if (imported != null) {
            if (!processor.execute(imported, state)) return false;
          }
        }
      }
      else {
        for (String qNameImported : myPseudoImports.values()) {
          PsiClass aClass = JavaPsiFacade.getInstance(myManager.getProject()).findClass(qNameImported, getResolveScope());
          if (aClass != null) {
            if (!processor.execute(aClass, state)) return false;
          }
        }
      }

      if (myContext == null) {
        return JavaResolveUtil.processImplicitlyImportedPackages(processor, state, place, getManager());
      }
    }

    IElementType i = myContentElementType;
    if (i == JavaElementType.TYPE_WITH_CONJUNCTIONS_TEXT ||
        i == JavaElementType.TYPE_WITH_DISJUNCTIONS_TEXT ||
        i == JavaElementType.EXPRESSION_STATEMENT ||
        i == JavaElementType.REFERENCE_TEXT) {
      return true;
    }
    else {
      processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this);
      if (lastParent == null) {
        // Parent element should not see our vars
        return true;
      }

      return PsiScopesUtil.walkChildrenScopes(this, processor, state, lastParent, place);
    }
  }

  @Override
  public String toString() {
    return "PsiCodeFragment:" + getName();
  }

  @Override
  public boolean importClass(@NotNull PsiClass aClass) {
    final String className = aClass.getName();
    final String qName = aClass.getQualifiedName();
    if (qName == null) return false;
    //if (!myPseudoImports.containsKey(className)){
    myPseudoImports.put(className, qName);
    myManager.beforeChange(false); // to clear resolve caches!
    if (isPhysical()) {
      final Project project = myManager.getProject();
      final Document document = PsiDocumentManager.getInstance(project).getDocument(this);
      UndoManager.getInstance(project).undoableActionPerformed(new ImportClassUndoableAction(className, qName, document, myPseudoImports));
    }
    return true;
    //}
    //else{
    //  return false;
    //}
  }

  private static class ImportClassUndoableAction extends BasicUndoableAction {
    private final String myClassName;
    private final String myQName;
    private final LinkedHashMap<String, String> myPseudoImports;

    ImportClassUndoableAction(final String className,
                                     final String qName,
                                     final Document document,
                                     final LinkedHashMap<String, String> pseudoImportsMap) {
      super(document);
      myClassName = className;
      myQName = qName;
      myPseudoImports = pseudoImportsMap;
    }

    @Override
    public void undo() {
      myPseudoImports.remove(myClassName);
    }

    @Override
    public void redo() {
      myPseudoImports.put(myClassName, myQName);
    }
  }

  @Override
  public ExceptionHandler getExceptionHandler() {
    return myExceptionHandler;
  }

  @Override
  public void setIntentionActionsFilter(final @NotNull IntentionActionsFilter filter) {
    myIntentionActionsFilter = filter;
  }

  @Override
  public IntentionActionsFilter getIntentionActionsFilter() {
    return myIntentionActionsFilter;
  }

  @Override
  public void forceResolveScope(GlobalSearchScope scope) {
    myResolveScope = scope;
  }

  @Override
  public GlobalSearchScope getForcedResolveScope() {
    return myResolveScope;
  }

  @Override
  public @NotNull GlobalSearchScope getResolveScope() {
    if (myResolveScope != null) return myResolveScope;
    return super.getResolveScope();
  }

  @Override
  public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
    myExceptionHandler = exceptionHandler;
  }
}
