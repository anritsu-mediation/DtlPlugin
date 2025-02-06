package com.anritsu.intellij.plugin.dtl.completion;

import com.anritsu.intellij.plugin.dtl.DtlIcons;
import com.anritsu.intellij.plugin.dtl.DtlLanguage;
import com.anritsu.intellij.plugin.dtl.parser.psi.*;
import com.anritsu.intellij.plugin.dtl.taggers.*;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Set;
import java.util.stream.Collectors;

public class DtlMethodCompletionContributor extends CompletionContributor {
    public DtlMethodCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(DtlTypes.IDENTIFIER).withLanguage(DtlLanguage.INSTANCE),
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        PsiElement element = parameters.getOriginalPosition();
                        if (element == null) {
                            return;
                        }

                        PsiFile file = element.getContainingFile();
                        Tagger.getInstance().refreshFile(file);

                        VariablesTagger variablesTagger = Tagger.getInstance().getVariablesTagger(file);
                        MethodsTagger methodsTagger = Tagger.getInstance().getMethodsTagger(file);

                        DtlUsermethod userMethod = PsiTreeUtil.getParentOfType(element, DtlUsermethod.class);

                        if (userMethod != null) {
                            completeInsideUserMethod(resultSet, element, file, variablesTagger, methodsTagger, userMethod);
                        }
                    }
                }
        );
    }

    private void completeInsideUserMethod(@NotNull CompletionResultSet resultSet, PsiElement element, PsiFile file, VariablesTagger variablesTagger, MethodsTagger methodsTagger, DtlUsermethod userMethod) {
        /* Variables */
        for (DTLVariable importedVariable : variablesTagger.getImportedVariables()) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(importedVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withIcon(DtlIcons.NATIVE_VARIABLES)
                    .withPresentableText(importedVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }

        for (DTLVariable includedVariable : variablesTagger.getIncludedVariables()) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(includedVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withIcon(DtlIcons.NATIVE_VARIABLES)
                    .withPresentableText(includedVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }

        for (DTLVariable globalVariable : variablesTagger.getGlobalVariables()) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(globalVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withIcon(DtlIcons.NATIVE_VARIABLES)
                    .withPresentableText(globalVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }

        Set<DTLVariable> localVariables = variablesTagger.getLocalVariables().stream()
                .filter(s -> s.getValue().equals(userMethod.getUserDefinedMethod()))
                .filter(s -> PsiTreeUtil.findChildrenOfType(userMethod, DtlVariabledecl.class).stream()
                        .anyMatch(dtlVariabledecl -> dtlVariabledecl.getVarName().equals(s.getKey().name) && dtlVariabledecl.getTextOffset() < element.getTextOffset()))
                .map(AbstractMap.SimpleEntry::getKey).collect(Collectors.toSet());
        for (DTLVariable localVariable : localVariables) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(localVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withIcon(DtlIcons.NATIVE_VARIABLES)
                    .withPresentableText(localVariable.getPresentableTextForCompletionInMethodInvocation())
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }

        // Signature variables
        DtlImportvars importvars = userMethod.getImportvars();
        if (importvars != null) {
            importvars.getImportvarList().forEach(
                    dtlImportvar -> {
                        LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(dtlImportvar.getIdentifierName())
                                .withIcon(DtlIcons.NATIVE_VARIABLES)
                                .withPresentableText(dtlImportvar.getIdentifierName())
                                .withTypeText(file.getName());
                        resultSet.addElement(lookupElementBuilder);
                    }
            );
        }

        /* Methods */
        boolean insideMethodInvocation = PsiTreeUtil.getParentOfType(element, DtlMethodinvocation.class) != null;
        for (DTLMethod importedMethod : methodsTagger.getImportedMethods()) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(importedMethod.getPresentableTextForCompletionInMethodInvocation(insideMethodInvocation))
                    .withIcon(DtlIcons.NATIVE_METHODS)
                    .withPresentableText(importedMethod.getPresentableTextForCompletionInMethodInvocation(insideMethodInvocation))
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }

        for (DTLMethod includedMethod : methodsTagger.getIncludedMethods()) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(includedMethod.getPresentableTextForCompletionInMethodInvocation(insideMethodInvocation))
                    .withIcon(DtlIcons.NATIVE_VARIABLES)
                    .withPresentableText(includedMethod.getPresentableTextForCompletionInMethodInvocation(insideMethodInvocation))
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }

        for (DTLMethod globalMethod : methodsTagger.getGlobalMethods()) {
            LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(globalMethod.getPresentableTextForCompletionInMethodInvocation(insideMethodInvocation))
                    .withIcon(DtlIcons.NATIVE_VARIABLES)
                    .withPresentableText(globalMethod.getPresentableTextForCompletionInMethodInvocation(insideMethodInvocation))
                    .withTypeText(file.getName());
            resultSet.addElement(lookupElementBuilder);
        }
    }

}