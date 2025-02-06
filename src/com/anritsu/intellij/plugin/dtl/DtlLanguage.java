package com.anritsu.intellij.plugin.dtl;

import com.intellij.lang.Language;

public class DtlLanguage extends Language {
  public static final DtlLanguage INSTANCE = new DtlLanguage();

  private DtlLanguage() {
    super("Dtl");
  }
}