package com.anritsu.intellij.plugin.dtl.annotator;

import com.anritsu.intellij.plugin.dtl.parser.psi.*;
import com.anritsu.intellij.plugin.dtl.taggers.DTLVariable;
import com.anritsu.intellij.plugin.dtl.taggers.Tagger;
import com.anritsu.intellij.plugin.dtl.taggers.VariablesTagger;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;


import static com.intellij.ide.highlighter.JavaHighlightingColors.STATIC_FIELD_ATTRIBUTES;

/**
 * Annotates the methods resolving the name in the global space (common.dtl) and local file
 */
public class VariableAnnotator implements Annotator {
    public VariableAnnotator() {
    }

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {

            VariablesTagger variablesTagger = Tagger.getInstance().getVariablesTagger(psiElement.getContainingFile());
            if (psiElement instanceof DtlExpressionTerminal) {
                Tagger.getInstance().refreshFile(psiElement.getContainingFile());
                annotateTerminalVariable(psiElement, annotationHolder, variablesTagger);
            } else if (psiElement instanceof DtlImportvardirective) {
                Tagger.getInstance().refreshFile(psiElement.getContainingFile());
                annotateImportedVariable((DtlImportvardirective) psiElement, annotationHolder);
            }
    }

    private void annotateImportedVariable(DtlImportvardirective psiElement, @NotNull AnnotationHolder annotationHolder) {
        DTLVariable variable = DTLVariable.fromImportVariableDirective(psiElement);
        if (!variable.isANativeVariable()) {
            annotationHolder.newAnnotation(HighlightSeverity.ERROR, variable.getPresentableTextForCompletionInMethodInvocation() + " is not a valid native variable").create();
        }
    }

    private void annotateTerminalVariable(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder, VariablesTagger variablesTagger) {


        DtlExpressionTerminal expressionTerminal = (DtlExpressionTerminal) psiElement;
        String variableName = expressionTerminal.getIdentifierName();
        if (variableName != null) {
            if (variablesTagger.getImportedVariables().stream().anyMatch(dtlVariable -> dtlVariable.name.equals(variableName)) || DTLVariable.nativeVariables.stream().anyMatch(dtlVariable -> dtlVariable.name.equals(variableName))) {
                @NotNull AnnotationBuilder annotationBuilder = annotationHolder.newSilentAnnotation(HighlightSeverity.INFORMATION);
                annotationBuilder.textAttributes(STATIC_FIELD_ATTRIBUTES).create();
                return;
            }

            if (variablesTagger.getGlobalVariables().stream().anyMatch(dtlVariable -> dtlVariable.name.equals(variableName))) return;

            if (variablesTagger.getIncludedVariables().stream().anyMatch(dtlVariable -> dtlVariable.name.equals(variableName))) return;

            DtlUsermethod usermethod = PsiTreeUtil.getParentOfType(psiElement, DtlUsermethod.class);
            if (usermethod != null && variablesTagger.getLocalVariables().stream().anyMatch(dtlVariableStringSimpleEntry -> dtlVariableStringSimpleEntry.getKey().name.equals(variableName) && dtlVariableStringSimpleEntry.getValue().equals(usermethod.getUserDefinedMethod()))) {
                    return;
                }

            DtlIterationStatement iterationStatement = PsiTreeUtil.getParentOfType(psiElement, DtlIterationStatement.class);
            if (iterationStatement != null && variablesTagger.getLocalVariables().stream().anyMatch(dtlVariableStringSimpleEntry -> dtlVariableStringSimpleEntry.getKey().name.equals(variableName))) {
                    return;
                }

            annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Cannot resolve symbol: " + variableName).create();
        }
    }

}
