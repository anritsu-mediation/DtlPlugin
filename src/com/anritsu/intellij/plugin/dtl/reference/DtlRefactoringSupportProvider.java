package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlId;
import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class DtlRefactoringSupportProvider extends RefactoringSupportProvider {
  @Override
  public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
    return element instanceof DtlId;
  }
}