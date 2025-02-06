package com.anritsu.intellij.plugin.dtl.folding;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DtlBraceMatcher implements PairedBraceMatcher {
  private final BracePair[] pairs = new BracePair[] {
      new BracePair(DtlTypes.DTL_TOKEN_OPAR, DtlTypes.DTL_TOKEN_CPAR, false),
      new BracePair(DtlTypes.DTL_TOKEN_OSQUARE, DtlTypes.DTL_TOKEN_CSQUARE, false),
      new BracePair(DtlTypes.DTL_TOKEN_OBRA, DtlTypes.DTL_TOKEN_CBRA, true)
  };

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return pairs;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull final IElementType lbraceType, @Nullable final IElementType contextType) {
    return true;
  }


  @Override
  public int getCodeConstructStart(final PsiFile file, int openingBraceOffset) {

    return openingBraceOffset;
  }
}