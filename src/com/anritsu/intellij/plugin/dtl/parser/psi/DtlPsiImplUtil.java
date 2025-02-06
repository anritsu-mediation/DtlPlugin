package com.anritsu.intellij.plugin.dtl.parser.psi;

import com.anritsu.intellij.plugin.dtl.annotator.DtlIndexingUtil;
import com.anritsu.intellij.plugin.dtl.reference.DtlElementFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DtlPsiImplUtil {


    public static List<PsiElement> getAllReferences(String fileName, Project project) {

        List<PsiElement> variabledecls = getVariableReferences(fileName, project);
        List<PsiElement> allMethods = getMethodReferences(fileName, project);
        List<PsiElement> allVariables = new ArrayList<>(variabledecls);

        allMethods.addAll(allVariables);
        return allMethods;

    }

    @NotNull
    private static List<PsiElement> getMethodReferences(String fileName, Project project) {
        List<DtlImportmethoddirective> nativeMethods;
        List<DtlUsermethod> userDefinedMethods;
        if (!"common.dtl".equals(fileName) && !"document.dtl".equals(fileName)) {
            nativeMethods = DtlIndexingUtil.findNativeMethods(project, "common.dtl", fileName);
            userDefinedMethods = DtlIndexingUtil.findUserDefinedMethods(project, "common.dtl", fileName);

        } else {
            nativeMethods = DtlIndexingUtil.findNativeMethods(project, fileName);
            userDefinedMethods = DtlIndexingUtil.findUserDefinedMethods(project, fileName);

        }

        List<PsiElement> allMethods = new ArrayList<>();
        allMethods.addAll(nativeMethods);
        allMethods.addAll(userDefinedMethods);
        return allMethods;
    }

    private static List<PsiElement> getVariableReferences(String fileName, Project project) {
        List<PsiElement> variabledecls;
        if (!"common.dtl".equals(fileName) && !"document.dtl".equals(fileName)) {
            variabledecls = DtlIndexingUtil.findUserDefinedVariables(project, "common.dtl", fileName);

        } else {
            variabledecls = DtlIndexingUtil.findUserDefinedVariables(project, fileName);

        }


        return variabledecls;
    }


    public static String getIdentifierName(DtlVariableIdentifier element) {
        DtlId id = element.getId();
        return Objects.requireNonNull(id.getNameIdentifier()).getText();
    }

    public static String getNativeMethod(DtlImportmethoddirective element) {
        return element.getVariableIdentifier().getIdentifierName();
    }

    public static String getNativeVariable(DtlImportvardirective element) {
        return element.getVariableIdentifier().getIdentifierName();

    }

    public static String getUserDefinedMethod(DtlUsermethod element) {
        return element.getVariableIdentifier().getIdentifierName();

    }

    public static String getIncludedFilename(DtlIncludedirective element) {
        String quotedText = element.getFilename().getText();
        return quotedText.replaceAll("\"", "");
    }

    public static String getUserMethodInvocation(DtlMethodinvocation element) {

        LeafPsiElement method = (LeafPsiElement) element.getNode().findChildByType(DtlTypes.IDENTIFIER);
        if (method != null) {
            return method.getText().replaceAll("\\\\ ", " ");
        }
        return null;


    }

    public static String getVarName(DtlVariabledecl element) {

        PsiElement nameIdentifier = element.getId().getNameIdentifier();
        if (nameIdentifier == null) return null;
        return nameIdentifier.getText();
    }


    public static String getIdentifierName(DtlExpressionTerminal element) {

        LeafPsiElement method = (LeafPsiElement) element.getNode().findChildByType(DtlTypes.IDENTIFIER);
        if (method != null) {
            return method.getText().replaceAll("\\\\ ", " ");
        }
        return null;


    }

    public static String getIdentifierName(DtlImportvar element) {

        return element.getVariableIdentifier().getIdentifierName();
    }


    public static String getName(DtlId element) {
        return Objects.requireNonNull(element.getNode().findChildByType(DtlTypes.IDENTIFIER)).getText();
    }

    public static PsiElement setName(DtlId element, String newName) {
        PsiElement nameIdentifier = element.getNameIdentifier();
        if (nameIdentifier != null) {
            ASTNode keyNode = nameIdentifier.getNode();

            PsiElement property = DtlElementFactory.createVariableIdentifier(element.getProject(), newName);
            ASTNode newKeyNode = property.getFirstChild().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }


    public static PsiElement getNameIdentifier(DtlId element) {
        ASTNode keyNode = element.getNode().findChildByType(DtlTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }
}