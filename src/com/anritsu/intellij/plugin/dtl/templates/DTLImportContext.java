package com.anritsu.intellij.plugin.dtl.templates;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import org.jetbrains.annotations.NotNull;

public class DTLImportContext extends TemplateContextType {

    protected DTLImportContext() {
        super( "Import");
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getName().endsWith(".dtl");
    }



}
