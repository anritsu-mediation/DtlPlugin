package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.annotator.DtlIndexingUtil;
import com.anritsu.intellij.plugin.dtl.parser.DtlFile;
import com.anritsu.intellij.plugin.dtl.parser.psi.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DtlVariableReference extends DtlReference {
    private DeclarationType declarationType = null;

    public enum DeclarationType {
        LOCAL,
        GLOBAL
    }

    public DtlVariableReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        ArrayList<PsiElementResolveResult> res = getReferences(PsiElementResolveResult::new);
        return res.toArray(new ResolveResult[0]);
    }


    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length >= 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new ArrayList<LookupElement>().toArray();
    }

    public DeclarationType getDeclarationType() {
        return declarationType;
    }

    private boolean isGlobal(PsiElement declaredVar) {
        DtlProgramcompound parentOfType = PsiTreeUtil.getParentOfType(declaredVar, DtlProgramcompound.class, true, DtlUsermethod.class, DtlCodeblockorsingle.class);
        return parentOfType != null;
    }

    private boolean isNative(PsiElement declaredVar) {
        DtlFile parentOfType = PsiTreeUtil.getParentOfType(declaredVar, DtlFile.class, true, DtlUsermethod.class, DtlCodeblockorsingle.class);
        return parentOfType != null;
    }

    private DeclarationType getDeclarationType(PsiElement variabledecl, PsiElement userVariableInvocation) {
        if (isGlobal(variabledecl) || isNative(variabledecl)) {
            declarationType = DeclarationType.GLOBAL;
            return DeclarationType.GLOBAL;
        }

        PsiElement parent = PsiTreeUtil.findFirstParent(variabledecl, psiElement ->
                psiElement instanceof DtlIterationStatement
                        || psiElement instanceof DtlCodeblockorsingle
                        || psiElement instanceof DtlUsermethod
        );

        if (parent != null) {
            if (PsiTreeUtil.isAncestor(parent, userVariableInvocation, true)) {
                boolean b = variabledecl.getNode().getStartOffset() < userVariableInvocation.getNode().getStartOffset();
                if (b) {
                    declarationType = DeclarationType.LOCAL;

                    return DeclarationType.LOCAL;
                }

            }

        }

        return null;
    }


    @NotNull
    private <R> ArrayList<R> getReferences(Function<PsiElement, R> refBuilder) {
        ArrayList<R> res = new ArrayList<>();

        String variable = ((DtlExpressionTerminal) myElement).getIdentifierName();
        if (variable != null) {
            String fileName = myElement.getContainingFile().getName();

            List<PsiElement> variabledecls;
            if (!"common.dtl".equals(fileName) && !"document.dtl".equals(fileName)) {
                variabledecls = DtlIndexingUtil.findUserDefinedVariables(myElement.getProject(), "common.dtl", fileName);

            } else {
                variabledecls = DtlIndexingUtil.findUserDefinedVariables(myElement.getProject(), fileName);

            }

            List<PsiElement> allVariables = new ArrayList<>(variabledecls);

            for (PsiElement var : allVariables) {
                if (var instanceof DtlVariabledeclStatement) {
                    DtlVariabledeclStatement m = (DtlVariabledeclStatement) var;
                    List<DtlVariabledecl> variabledeclList = m.getVariabledeclsline().getVariabledeclList();
                    for (DtlVariabledecl variabledecl : variabledeclList) {
                        if (variabledecl.getVarName().equals(variable)) {
                            if (getDeclarationType(variabledecl, myElement) == null) continue;
                            res.add(refBuilder.apply(variabledecl.getId()));
                        }
                    }
                } else if (var instanceof DtlVariableIdentifier) {
                    DtlVariableIdentifier variabledecl = (DtlVariableIdentifier) var;
                    if (variabledecl.getIdentifierName().equals(variable)) {
                        if (getDeclarationType(variabledecl, myElement) == null) continue;
                        res.add(refBuilder.apply(variabledecl.getId()));
                    }
                }
            }
        }
        return res;
    }


    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        LeafPsiElement nameIdentifier = (LeafPsiElement) myElement.getNode().findChildByType(DtlTypes.IDENTIFIER);
        if (nameIdentifier != null) {
            ASTNode keyNode = nameIdentifier.getNode();

            PsiElement property = DtlElementFactory.createVariableIdentifier(myElement.getProject(), newElementName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            myElement.getNode().replaceChild(keyNode, newKeyNode);
        }
        return myElement;
    }

}