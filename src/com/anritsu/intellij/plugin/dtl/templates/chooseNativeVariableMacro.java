package com.anritsu.intellij.plugin.dtl.templates;

import com.anritsu.intellij.plugin.dtl.taggers.DTLMethod;
import com.anritsu.intellij.plugin.dtl.taggers.DTLVariable;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Result;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.codeInsight.template.macro.EnumMacro;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class chooseNativeVariableMacro extends EnumMacro {
    List<String> results = DTLVariable.nativeVariables.stream().map(DTLVariable::getPresentableTextForCompletionInImport).collect(Collectors.toList());

    @Override
    public String getName() {
        return "chooseNativeVariable";
    }

    @Override
    public String getPresentableName() {
        return "chooseNativeVariable()";
    }

    @Override
    public Result calculateResult(@NotNull Expression[] params, ExpressionContext context) {
        return super.calculateResult(params, context);
    }

    @Override
    public LookupElement[] calculateLookupItems(@NotNull Expression[] params, ExpressionContext context) {
        return results.stream().map(LookupElementBuilder::create).toArray(LookupElement[]::new);
    }

    @Override
    public boolean isAcceptableInContext(TemplateContextType context) {
        return (context instanceof DTLImportContext);
    }

}
