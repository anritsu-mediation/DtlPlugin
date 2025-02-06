package com.anritsu.intellij.plugin.dtl.structure;

import com.anritsu.intellij.plugin.dtl.DtlIcons;
import com.anritsu.intellij.plugin.dtl.parser.DtlFile;
import com.anritsu.intellij.plugin.dtl.parser.psi.*;
import com.intellij.ide.highlighter.JavaHighlightingColors;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

public class DtlStructureViewElement extends PsiTreeElementBase<NavigatablePsiElement> implements ColoredItemPresentation {

    enum ElemType {
        USER_METHOD,
        NATIVE_METHOD,
        NATIVE_VAR,
        FILE
    }

    private ElemType elemType;

    public DtlStructureViewElement(NavigatablePsiElement element) {
        super(element);

        if (getElement() instanceof DtlUsermethod) {
            elemType = ElemType.USER_METHOD;
        } else if (getElement() instanceof DtlImportdirective) {
            if (((DtlImportdirective) getElement()).getImportmethoddirective() != null) {
                elemType = ElemType.NATIVE_METHOD;
            } else {
                elemType = ElemType.NATIVE_VAR;
            }
        } else if (getElement() instanceof DtlFile) {
            elemType = ElemType.FILE;
        }
    }

    @Override
    public NavigatablePsiElement getValue() {
        return getElement();
    }

    @Override
    public void navigate(boolean requestFocus) {
        Objects.requireNonNull(getElement()).navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return Objects.requireNonNull(getElement()).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return Objects.requireNonNull(getElement()).canNavigateToSource();
    }

    @NotNull
    @Override
    public Collection<StructureViewTreeElement> getChildrenBase() {
        if (getElement() instanceof DtlFile) {
            Collection<PsiElement> imports = PsiTreeUtil.findChildrenOfType(getElement(), DtlImportdirective.class);
            Collection<PsiElement> userMethods = PsiTreeUtil.findChildrenOfType(getElement(), DtlUsermethod.class);
            ArrayList<PsiElement> all = new ArrayList<>();
            List<StructureViewTreeElement> treeElements;
            all.addAll(imports);
            all.addAll(userMethods);

            treeElements = new ArrayList<>(all.size());

            for (PsiElement imp : all) {
                treeElements.add(new DtlStructureViewElement((NavigatablePsiElement) imp));
            }
            return treeElements;
        } else {
            return Collections.emptyList();
        }
    }


    @Nullable
    @Override
    public String getPresentableText() {


        switch (elemType) {

            case USER_METHOD: {
                DtlUsermethod element = (DtlUsermethod) getElement();
                DtlVariableIdentifier variableIdentifier = Objects.requireNonNull(element).getVariableIdentifier();

                StringBuilder sb = new StringBuilder();
                String nameIdentifier = variableIdentifier.getId().getName();
                DtlImportvars importvars = element.getImportvars();

                sb.append(nameIdentifier);
                sb.append("(");
                if (importvars != null) {
                    StringBuilder vars = new StringBuilder();
                    for (DtlImportvar importvar : importvars.getImportvarList()) {
                        if (vars.length() > 0) vars.append(", ");
                        vars.append(importvar.getVariableIdentifier().getIdentifierName());
                    }
                    sb.append(vars);
                }

                sb.append(")");
                return sb.toString();
            }
            case NATIVE_METHOD: {
                DtlImportdirective element = (DtlImportdirective) getElement();
                return formatImportMethodDirective(Objects.requireNonNull(Objects.requireNonNull(element).getImportmethoddirective()));
            }
            case NATIVE_VAR: {
                DtlImportdirective element = (DtlImportdirective) getElement();

                return formatImportVarDirective(Objects.requireNonNull(Objects.requireNonNull(element).getImportvardirective()));
            }
            case FILE:
                DtlFile f = (DtlFile) getElement();
                return Objects.requireNonNull(f).getName();
        }

        return null;
    }

    @NotNull
    private String formatImportVarDirective(DtlImportvardirective importvardirective) {
        DtlVariableIdentifier variableIdentifier = importvardirective.getVariableIdentifier();

        StringBuilder sb = new StringBuilder();
        String nameIdentifier = variableIdentifier.getId().getName();
        sb.append(nameIdentifier);
        return sb.toString();
    }

    @NotNull
    private String formatImportMethodDirective(DtlImportmethoddirective importmethoddirective) {
        DtlVariableIdentifier variableIdentifier = importmethoddirective.getVariableIdentifier();

        StringBuilder sb = new StringBuilder();
        String nameIdentifier = variableIdentifier.getId().getName();
        DtlImportvars importvars = importmethoddirective.getImportvars();

        sb.append(nameIdentifier);
        sb.append("(");
        if (importvars != null) {
            StringBuilder vars = new StringBuilder();
            for (DtlImportvar importvar : importvars.getImportvarList()) {
                if (vars.length() > 0) vars.append(", ");
                vars.append(importvar.getVariableIdentifier().getIdentifierName());
            }
            sb.append(vars);
        }

        sb.append(")");

        return sb.toString();
    }

    @Override
    public Icon getIcon(boolean open) {

        switch (elemType) {
            case USER_METHOD:
                return DtlIcons.METHODS;
            case NATIVE_METHOD:
                return DtlIcons.NATIVE_METHODS;
            case NATIVE_VAR:
                return DtlIcons.NATIVE_VARIABLES;
            case FILE:
                return DtlIcons.FILE;
        }
        return null;
    }

    @Nullable
    @Override
    public TextAttributesKey getTextAttributesKey() {

        switch (elemType) {
            case USER_METHOD:
                return JavaHighlightingColors.METHOD_CALL_ATTRIBUTES;
            case NATIVE_METHOD:
                return JavaHighlightingColors.STATIC_METHOD_ATTRIBUTES;
            case NATIVE_VAR:
                return JavaHighlightingColors.STATIC_FIELD_ATTRIBUTES;
            case FILE:
                return JavaHighlightingColors.CLASS_NAME_ATTRIBUTES;
        }


        return null;
    }
}