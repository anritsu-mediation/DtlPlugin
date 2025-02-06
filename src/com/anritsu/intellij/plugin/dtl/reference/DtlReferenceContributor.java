package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlExpressionTerminal;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlMethodinvocation;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class DtlReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(PlatformPatterns.psiElement(DtlMethodinvocation.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                 @NotNull ProcessingContext
                                                                         context) {
                        DtlMethodinvocation methodInvocation = (DtlMethodinvocation) element;
                        String identifierName = methodInvocation.getUserMethodInvocation();
                        if (identifierName != null ) {
                            return new PsiReference[]{
                                    new DtlMethodReference(element, new TextRange(0, identifierName.length()))};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }


                }, PsiReferenceRegistrar.HIGHER_PRIORITY);

        registrar.registerReferenceProvider(PlatformPatterns.psiElement(DtlExpressionTerminal.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                 @NotNull ProcessingContext
                                                                         context) {
                        DtlExpressionTerminal terminal = (DtlExpressionTerminal) element;
                        String identifierName = terminal.getIdentifierName();
                        if (identifierName != null ) {
                            String text = element.getText();
                            int i = text.indexOf(identifierName);
                            DtlVariableReference variableReference = new DtlVariableReference(element, new TextRange(i, i + identifierName.length()));

                            return new PsiReference[]{variableReference};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }


                }, PsiReferenceRegistrar.HIGHER_PRIORITY);
    }
}