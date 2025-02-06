/*
 * $Id: DtlCommenter.java 29021 2013-04-10 15:47:16Z masp $
 *
 * Copyright 1998-2006 by Anritsu A/S,
 * Kirkebjerg Alle 86, DK-2605 Broendby, Denmark.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Anritsu A/S.
 */
package com.anritsu.intellij.plugin.dtl.commenter;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

/**
 * TODO FILL ME!
 *
 * @author Marco Speranza
 * @version $Revision: $
 */
public class DtlCommenter implements CodeDocumentationAwareCommenter {
    @Override
    public String getLineCommentPrefix() {
        return "//";
    }

    @Override
    public String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }


    @Nullable
    @Override
    public IElementType getLineCommentTokenType() {
        return DtlTypes.END_OF_LINE_COMMENT;
    }

    @Nullable
    @Override
    public IElementType getBlockCommentTokenType() {
        return DtlTypes.C_STYLE_COMMENT;
    }

    @Nullable
    @Override
    public IElementType getDocumentationCommentTokenType() {
        return null;
    }

    @Nullable
    @Override
    public String getDocumentationCommentPrefix() {
        return "/**";
    }

    @Nullable
    @Override
    public String getDocumentationCommentLinePrefix() {
        return "/**";
    }

    @Nullable
    @Override
    public String getDocumentationCommentSuffix() {
        return "**/";
    }

    @Override
    public boolean isDocumentationComment(PsiComment psiComment) {
        return false;
    }
}
