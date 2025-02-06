package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.parser.DtlLexerAdapter;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlId;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DtlFindUsagesProvider implements FindUsagesProvider {
  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    return new DefaultWordsScanner(new DtlLexerAdapter(),
                                   TokenSet.create(DtlTypes.IDENTIFIER) ,
            TokenSet.create(DtlTypes.IDENTIFIER),
                                   TokenSet.EMPTY);
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof PsiNamedElement;
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
      return "Dtl native method";
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
   return element.getText();
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof DtlId) {
      return Objects.requireNonNull(((DtlId) element).getNameIdentifier()).getText();
    }
    return "";
  }
}