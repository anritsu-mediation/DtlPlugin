package com.anritsu.intellij.plugin.dtl.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;

abstract public class DtlReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

  public DtlReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
  }

}