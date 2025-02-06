package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class DtlNamedElementImpl extends DtlASTWrapperPsiElement implements DtlNamedElement {
  public DtlNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }
}