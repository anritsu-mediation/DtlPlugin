package com.anritsu.intellij.plugin.dtl.annotator;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportmethoddirective;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlMethodinvocation;
import com.anritsu.intellij.plugin.dtl.taggers.DTLMethod;
import com.anritsu.intellij.plugin.dtl.taggers.MethodsTagger;
import com.anritsu.intellij.plugin.dtl.taggers.Tagger;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Annotates the methods resolving the name in the global space (common.dtl) and local file
 */
public class MethodAnnotator implements Annotator {
    MethodAnnotator() {
    }

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        if (psiElement instanceof DtlMethodinvocation) {
            Tagger.getInstance().refreshFile(psiElement.getContainingFile());
            MethodsTagger methodsTagger = Tagger.getInstance().getMethodsTagger(psiElement.getContainingFile());
            annotateDtlMethodInvocation((DtlMethodinvocation) psiElement, annotationHolder, methodsTagger);
        }
        else if (psiElement instanceof DtlImportmethoddirective) {
            Tagger.getInstance().refreshFile(psiElement.getContainingFile());
            annotateDtlImportMethodDirective((DtlImportmethoddirective) psiElement, annotationHolder);
        }
    }

    private void annotateDtlImportMethodDirective(DtlImportmethoddirective psiElement, AnnotationHolder annotationHolder) {
        DTLMethod dtlMethod = DTLMethod.fromImportMethodDirective(psiElement);
        if (!dtlMethod.isANativeMethod()) {
            annotationHolder.newAnnotation(HighlightSeverity.ERROR, dtlMethod.getPresentableTextForCompletionInMethodInvocation(true) + " is not a valid native method").create();
        }
    }

    private void annotateDtlMethodInvocation(@NotNull DtlMethodinvocation psiElement, @NotNull AnnotationHolder annotationHolder, MethodsTagger methodsTagger) {
        String method = psiElement.getUserMethodInvocation();
        int numberOfArguments;

        if (psiElement.getExpressionlist() == null)
            numberOfArguments = 0;
        else {
            numberOfArguments = psiElement.getExpressionlist().getExpressionList().size();
        }

        if (method != null) {
            if (
                    methodsTagger.getImportedMethods().stream().anyMatch(dtlMethod -> dtlMethod.name.equals(method) && dtlMethod.attributes.size() == numberOfArguments)
                    ||
                    methodsTagger.getIncludedMethods().stream().anyMatch(dtlMethod -> dtlMethod.name.equals(method) && dtlMethod.attributes.size() == numberOfArguments)
                    ||
                    methodsTagger.getGlobalMethods().stream().anyMatch(dtlMethod -> dtlMethod.name.equals(method) && dtlMethod.attributes.size() == numberOfArguments)
            ) {
                return;
        }

        annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Cannot resolve method: " + method + " with " + numberOfArguments + " argument" + (numberOfArguments > 1 ? "s" : "")).create();
        }
    }
}