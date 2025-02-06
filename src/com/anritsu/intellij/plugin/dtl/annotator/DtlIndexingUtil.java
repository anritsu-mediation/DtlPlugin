package com.anritsu.intellij.plugin.dtl.annotator;

import com.anritsu.intellij.plugin.dtl.DtlFileType;
import com.anritsu.intellij.plugin.dtl.parser.DtlFile;
import com.anritsu.intellij.plugin.dtl.parser.psi.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DtlIndexingUtil {

    /**
     * Find  all native methods
     *
     * @return The list of all native DTL methods
     */
    public static List<DtlImportmethoddirective> findNativeMethods(Project project, String... files) {
        List<DtlImportmethoddirective> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(DtlFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            if (!containsFileName(files, virtualFile.getName())) continue;

            DtlFile simpleFile = (DtlFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                Collection<DtlImportmethoddirective> importNativeDirectives = PsiTreeUtil.findChildrenOfAnyType(simpleFile, DtlImportmethoddirective.class);
                result.addAll(importNativeDirectives);
            }
        }
        return result;
    }



    public static List<DtlUsermethod> findUserDefinedMethods(Project project, String... files) {
        List<DtlUsermethod> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(DtlFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            if (!containsFileName(files, virtualFile.getName())) continue;

            DtlFile simpleFile = (DtlFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                DtlProgramcompound[] program = PsiTreeUtil.getChildrenOfType(simpleFile, DtlProgramcompound.class);
                if (program == null) continue;
                for (DtlProgramcompound dtlProgramcompound : program) {

                    DtlUsermethod[] userMethods = PsiTreeUtil.getChildrenOfType(dtlProgramcompound, DtlUsermethod.class);
                    if (userMethods != null) {
                        Collections.addAll(result, userMethods);
                    }
                }
            }
        }
        return result;
    }

    public static List<PsiElement> findUserDefinedVariables(Project project, String... files) {
        List<PsiElement> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(DtlFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            if (!containsFileName(files, virtualFile.getName())) continue;

            DtlFile simpleFile = (DtlFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {

                Collection<PsiElement> variabledecls = PsiTreeUtil.findChildrenOfAnyType(simpleFile,
                        DtlVariabledeclStatement.class,
                        DtlImportvar.class,
                        DtlImportvardirective.class,
                        DtlIterationStatement.class);

                for (PsiElement element : variabledecls) {
                    if (element instanceof DtlVariabledeclStatement) {
                        result.add(element);
                    } else {
                        PsiElement firstParent = PsiTreeUtil.findFirstParent(element, psiElement -> psiElement instanceof DtlImportmethoddirective);
                        // skip variable parameters declared in native methods
                        if (firstParent != null) continue;

                        List<DtlVariableIdentifier> childrenOfType = PsiTreeUtil.getChildrenOfTypeAsList(element, DtlVariableIdentifier.class);
                        result.addAll(childrenOfType);
                    }
                }
            }
        }
        return result;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean containsFileName(String[] files, String name) {
        for (String s : files) {
            if (name.equals(s)) return true;
        }
        return false;
    }
}