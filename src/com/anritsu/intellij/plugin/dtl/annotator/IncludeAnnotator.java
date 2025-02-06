package com.anritsu.intellij.plugin.dtl.annotator;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlIncludedirective;
import com.anritsu.intellij.plugin.dtl.taggers.Tagger;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Annotates the include directives
 */
public class IncludeAnnotator implements Annotator {
    public IncludeAnnotator() {
    }

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
            if (psiElement instanceof DtlIncludedirective) {
                annotateIncludedDirective((DtlIncludedirective) psiElement, annotationHolder);
            }
    }

    private void annotateIncludedDirective(DtlIncludedirective psiElement, AnnotationHolder annotationHolder) {
        String filename = psiElement.getIncludedFilename();
        VirtualFile virtualFile = Tagger.searchIncludedFile(psiElement.getContainingFile(), filename);
        if (virtualFile == null)
            annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Couldn't find " + filename).create();
    }

}
