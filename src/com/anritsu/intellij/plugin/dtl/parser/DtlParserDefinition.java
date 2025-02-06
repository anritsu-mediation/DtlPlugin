package com.anritsu.intellij.plugin.dtl.parser;

import com.anritsu.intellij.plugin.dtl.DtlLanguage;
import com.anritsu.intellij.plugin.dtl.highlighter.DtlTokenSet;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class DtlParserDefinition implements ParserDefinition {

  public static final IFileElementType FILE = new IFileElementType(DtlLanguage.INSTANCE);

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new DtlLexerAdapter();
  }

  @NotNull
  public TokenSet getWhitespaceTokens() {
    return DtlTokenSet.DTL_WS_SET;
  }

  @NotNull
  public TokenSet getCommentTokens() {
    return DtlTokenSet.DTL_COMMENT_SET;
  }

  @NotNull
  public TokenSet getStringLiteralElements() {
    return TokenSet.EMPTY;
  }

  @NotNull
  public PsiParser createParser(final Project project) {
    return new DtlParser();
  }

  @NotNull
  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  @NotNull
  public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new DtlFile(viewProvider);
  }

  @NotNull
  public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return DtlTypes.Factory.createElement(node);
  }
}