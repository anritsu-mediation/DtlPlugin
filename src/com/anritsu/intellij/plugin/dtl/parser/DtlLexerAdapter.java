package com.anritsu.intellij.plugin.dtl.parser;

import com.intellij.lexer.FlexAdapter;

public class DtlLexerAdapter extends FlexAdapter {
  public DtlLexerAdapter() {
    super(new DtlLexer(null));
  }
}
