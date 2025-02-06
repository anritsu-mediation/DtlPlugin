package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.annotator.DtlIndexingUtil;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportmethoddirective;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlMethodinvocation;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlUsermethod;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DtlMethodReference extends DtlReference {

    public DtlMethodReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> res = new ArrayList<>();

        if (myElement instanceof DtlMethodinvocation) {
            DtlMethodinvocation userMetohodInvocation = ((DtlMethodinvocation) myElement);
            String fileName = myElement.getContainingFile().getName();

            List<DtlImportmethoddirective> nativeMethods;
            List<DtlUsermethod> userDefinedMethods;
            if (!"common.dtl".equals(fileName) && !"document.dtl".equals(fileName)) {
                nativeMethods = DtlIndexingUtil.findNativeMethods(myElement.getProject(), "common.dtl", fileName);
                userDefinedMethods = DtlIndexingUtil.findUserDefinedMethods(myElement.getProject(), "common.dtl", fileName);

            } else {
                nativeMethods = DtlIndexingUtil.findNativeMethods(myElement.getProject(), fileName);
                userDefinedMethods = DtlIndexingUtil.findUserDefinedMethods(myElement.getProject(), fileName);

            }

            List<PsiElement> allMethods = new ArrayList<>();
            allMethods.addAll(nativeMethods);
            allMethods.addAll(userDefinedMethods);


            for (PsiElement method : allMethods) {
                if (method instanceof DtlUsermethod) {
                    DtlUsermethod m = (DtlUsermethod) method;
                    if (m.getUserDefinedMethod().equals(userMetohodInvocation.getUserMethodInvocation())) {
                        res.add(new PsiElementResolveResult(m.getVariableIdentifier().getId()));
                    }
                } else if (method instanceof DtlImportmethoddirective) {
                    DtlImportmethoddirective m = (DtlImportmethoddirective) method;
                    if (m.getNativeMethod().equals(userMetohodInvocation.getUserMethodInvocation())) {
                        res.add(new PsiElementResolveResult(m.getVariableIdentifier().getId()));
                    }
                }
            }
        }
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