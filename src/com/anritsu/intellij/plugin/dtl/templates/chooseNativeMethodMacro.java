package com.anritsu.intellij.plugin.dtl.templates;

import com.anritsu.intellij.plugin.dtl.taggers.DTLMethod;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.*;
import com.intellij.codeInsight.template.macro.EnumMacro;
import com.intellij.codeInsight.template.macro.MacroBase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class chooseNativeMethodMacro extends EnumMacro {
    List<String> results = DTLMethod.nativeMethods.stream().map(DTLMethod::toString).collect(Collectors.toList());

    @Override
    public String getName() {
        return "chooseNativeMethod";
    }

    @Override
    public String getPresentableName() {
        return "chooseNativeMethod()";
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
