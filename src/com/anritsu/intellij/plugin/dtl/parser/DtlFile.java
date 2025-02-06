package com.anritsu.intellij.plugin.dtl.parser;

import com.anritsu.intellij.plugin.dtl.DtlFileType;
import com.anritsu.intellij.plugin.dtl.DtlLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DtlFile extends PsiFileBase {
  public DtlFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, DtlLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return DtlFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Dtl File";
  }

  @Override
  public Icon getIcon(int flags) {
    return super.getIcon(flags);
  }
}