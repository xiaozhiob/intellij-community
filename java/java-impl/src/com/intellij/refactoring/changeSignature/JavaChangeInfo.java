// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.refactoring.changeSignature;

import com.intellij.psi.*;
import com.intellij.refactoring.util.CanonicalTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author Maxim.Medvedev
 */
public interface JavaChangeInfo extends ChangeInfo {
  boolean isVisibilityChanged();

  boolean isExceptionSetChanged();

  boolean isExceptionSetOrOrderChanged();

  @Override
  @NotNull
  PsiMethod getMethod();

  CanonicalTypes.Type getNewReturnType();
   
  default @Nullable String getOldReturnType() {
    PsiType type = getMethod().getReturnType();
    return type != null ? type.getCanonicalText() : null;
  }

  /**
   * @return true if it's desired to fix conflicts between new parameters and field names (adding {@code this.} qualifier to field accesses)
   */
  default boolean isFixFieldConflicts() {
    return true;
  }

  @Override
  JavaParameterInfo @NotNull [] getNewParameters();

  @PsiModifier.ModifierConstant
  @NotNull
  String getNewVisibility();

  String @NotNull [] getOldParameterNames();

  String @NotNull [] getOldParameterTypes();

  ThrownExceptionInfo[] getNewExceptions();

  boolean isRetainsVarargs();

  boolean isObtainsVarags();

  boolean isArrayToVarargs();

  PsiIdentifier getNewNameIdentifier();

  String getOldName();

  boolean wasVararg();

  boolean[] toRemoveParm();

  PsiExpression getValue(int i, PsiCallExpression callExpression);

  void updateMethod(@NotNull PsiMethod psiMethod);

  @NotNull
  Collection<PsiMethod> getMethodsToPropagateParameters();

  default boolean checkUnusedParameter() {
    return false;
  }

}
