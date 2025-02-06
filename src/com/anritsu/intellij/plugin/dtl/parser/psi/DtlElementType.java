package com.anritsu.intellij.plugin.dtl.parser.psi;

import com.anritsu.intellij.plugin.dtl.DtlLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DtlElementType extends IElementType {
  public DtlElementType(@NotNull @NonNls String debugName) {
    super(debugName, DtlLanguage.INSTANCE);
  }
}