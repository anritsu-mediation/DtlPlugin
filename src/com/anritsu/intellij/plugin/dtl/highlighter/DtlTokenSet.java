/*
 * $Id: DtlTokenSet.java 29021 2013-04-10 15:47:16Z masp $
 *
 * Copyright 1998-2006 by Anritsu A/S,
 * Kirkebjerg Alle 86, DK-2605 Broendby, Denmark.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Anritsu A/S.
 */
package com.anritsu.intellij.plugin.dtl.highlighter;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;
import com.intellij.psi.tree.TokenSet;

/**
 * Contains the group of tokens to configure th highlight
 *
 * @author Marco Speranza
 * @version $Revision: $
 */
public interface DtlTokenSet extends DtlTypes {

    TokenSet DTL_WS_SET = TokenSet.create(WS);

    TokenSet DTL_COMMENT_SET = TokenSet.create(C_STYLE_COMMENT, END_OF_LINE_COMMENT);

    TokenSet DTL_STRING_SET = TokenSet.create(STRING, SINGLE_QUOTED_STRING);

    TokenSet DTL_NUMBER_SET = TokenSet.create(
            INTEGERNUMBER,
            DOUBLENUMBER
            );

    TokenSet DTL_KEYWORD_SET = TokenSet.create(
            DTL_TOKEN_IMPORT,
            DTL_TOKEN_VAR,
            DTL_TOKEN_IF,
            DTL_TOKEN_NULLVALUE,
            DTL_TOKEN_FINAL,
            DTL_TOKEN_NULLVALUE,
            DTL_TOKEN_TRUE,
            DTL_TOKEN_FALSE,
            DTL_TOKEN_VAR,
            DTL_TOKEN_FINAL,
            DTL_TOKEN_SET,
            DTL_TOKEN_LIST,
            DTL_TOKEN_BYTES,
            DTL_TOKEN_MAP,
            DTL_TOKEN_CLONE,
            DTL_TOKEN_IF,
            DTL_TOKEN_ELSE,
            DTL_TOKEN_SWITCH,
            DTL_TOKEN_CASE,
            DTL_TOKEN_DEFAULT,
            DTL_TOKEN_WHILE,
            DTL_TOKEN_FOR,
            DTL_TOKEN_BREAK,
            DTL_TOKEN_CONTINUE,
            DTL_TOKEN_RETURN,
            DTL_TOKEN_ASSERT,
            DTL_TOKEN_TRACE
    );

    TokenSet DTL_NATIVE_METHOD_SET = TokenSet.create(
            DTL_TOKEN_TOINTEGER,
            DTL_TOKEN_TOSTRING,
            DTL_TOKEN_TODOUBLE,
            DTL_TOKEN_TOBYTES,
            DTL_TOKEN_TOLIST,
            DTL_TOKEN_TOSET,
            DTL_TOKEN_TOMAP,
            DTL_TOKEN_CONTAINS,
            DTL_TOKEN_FIND,
            DTL_TOKEN_PUT,
            DTL_TOKEN_SIZE,
            DTL_TOKEN_ERASE,
            DTL_TOKEN_REMOVE,
            DTL_TOKEN_KEYS,
            DTL_TOKEN_VALUES,
            DTL_TOKEN_ENTRIES,
            DTL_TOKEN_TOLOWER,
            DTL_TOKEN_TOUPPER,
            DTL_TOKEN_SORT,
            DTL_TOKEN_BEGINSWITH,
            DTL_TOKEN_ENDSWITH,
            DTL_TOKEN_RFIND,
            DTL_TOKEN_SUBSTRING,
            DTL_TOKEN_MATCH,
            DTL_TOKEN_SPLIT,
            DTL_TOKEN_ISNULL
    );

}
