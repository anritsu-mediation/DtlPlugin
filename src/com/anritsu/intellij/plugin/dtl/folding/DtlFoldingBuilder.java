package com.anritsu.intellij.plugin.dtl.folding;

import com.anritsu.intellij.plugin.dtl.parser.DtlFile;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlCodeblockorsingle;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlUsermethod;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DtlFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> foldingDescriptors = new ArrayList<>();
        if (root instanceof DtlFile) {
            Collection<PsiElement> codeBlocks = PsiTreeUtil.findChildrenOfType(root, DtlCodeblockorsingle.class);
            Collection<PsiElement> userMethods = PsiTreeUtil.findChildrenOfType(root, DtlUsermethod.class);

            List<PsiElement> all = new ArrayList<>();
            all.addAll(codeBlocks);
            all.addAll(userMethods);
            for (PsiElement element : all) {
                FoldingGroup group = FoldingGroup.newGroup("dtl-" + element.hashCode());

                TextRange range = getRange(element);
                if(range.isEmpty())
                    continue;

                foldingDescriptors.add(new FoldingDescriptor(element.getNode(),
                        range,
                        group));

            }

        }

        return foldingDescriptors.toArray(new FoldingDescriptor[0]);
    }

    @NotNull
    private TextRange getRange(PsiElement element) {
        int startOffset = element.getTextRange().getStartOffset() + 1 ;
        int endOffset = element.getTextRange().getEndOffset() - 1;
        if (element instanceof DtlUsermethod) {
            startOffset += element.getText().indexOf("{");
        }

        return new TextRange(startOffset,
                endOffset);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
