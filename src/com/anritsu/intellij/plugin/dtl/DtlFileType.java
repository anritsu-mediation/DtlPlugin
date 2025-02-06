package com.anritsu.intellij.plugin.dtl;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class DtlFileType extends LanguageFileType {
  public static final DtlFileType INSTANCE = new DtlFileType();

  private DtlFileType() {
    super(DtlLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Dtl file";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Dtl language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "dtl";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return DtlIcons.FILE;
  }
}