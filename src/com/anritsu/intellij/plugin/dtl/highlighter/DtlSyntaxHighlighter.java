package com.anritsu.intellij.plugin.dtl.highlighter;

import com.anritsu.intellij.plugin.dtl.parser.DtlLexerAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class DtlSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey DTL_COMMENT_SET = createTextAttributesKey("DTL_COMMENT_SET", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey DTL_KEYWORD_SET = createTextAttributesKey("DTL_KEYWORD_SET", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey DTL_STRING_SET = createTextAttributesKey("DTL_STRING_SET", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey DTL_NUMBER_SET = createTextAttributesKey("DTL_NUMBER_SET", DefaultLanguageHighlighterColors.NUMBER);

    private static final TextAttributesKey[] DTL_COMMENT_SET_KEYS = new TextAttributesKey[]{DTL_COMMENT_SET};
    private static final TextAttributesKey[] DTL_KEYWORD_SET_KEYS = new TextAttributesKey[]{DTL_KEYWORD_SET};
    private static final TextAttributesKey[] DTL_STRING_SET_KEYS = new TextAttributesKey[]{DTL_STRING_SET};
    private static final TextAttributesKey[] DTL_NUMBER_SET_KEYS = new TextAttributesKey[]{DTL_NUMBER_SET};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new DtlLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (DtlTokenSet.DTL_STRING_SET.contains(tokenType)) {
            return DTL_STRING_SET_KEYS;
        }
        if (DtlTokenSet.DTL_COMMENT_SET.contains(tokenType)) {
            return DTL_COMMENT_SET_KEYS;
        }
        if (DtlTokenSet.DTL_KEYWORD_SET.contains(tokenType)) {
            return DTL_KEYWORD_SET_KEYS;
        }
        if (DtlTokenSet.DTL_NUMBER_SET.contains(tokenType)) {
            return DTL_NUMBER_SET_KEYS;
        }
        return EMPTY_KEYS;
    }
}