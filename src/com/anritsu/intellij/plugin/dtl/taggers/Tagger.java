package com.anritsu.intellij.plugin.dtl.taggers;

import com.anritsu.intellij.plugin.dtl.parser.psi.*;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Tagger {
    private static volatile Tagger instance;
    private static final HashMap<String, VirtualFile> virtualFiles = new HashMap<>();
    private final HashMap<String, VariablesTagger> variablesTaggerForFile = new HashMap<>();
    private final HashMap<String, MethodsTagger> methodsTaggerForFile = new HashMap<>();
    private final HashMap<String, Long> modificationStamps = new HashMap<>();
    private String rootText;

    public long getModificationStamp(PsiFile psiRoot) {
        if (psiRoot != null && instance.modificationStamps.containsKey(psiRoot.getName())) {
            return instance.modificationStamps.get(psiRoot.getName());
        }
        else
            return -1;
    }

    private Tagger() {
    }

    public static Tagger getInstance() {
        if (instance == null) {
            synchronized (Tagger.class) {
                if (instance == null) {
                    instance = new Tagger();
                }
            }
        }
        return instance;
    }

    public  synchronized void refreshFile(PsiFile psiRoot) {
        if (psiRoot.getModificationStamp() == instance.getModificationStamp(psiRoot) && instance.rootText.equals(psiRoot.getText()))
            return;

        VariablesTagger variablesTagger = instance.getVariablesTagger(psiRoot);
        variablesTagger.reset();
        MethodsTagger methodsTagger = instance.getMethodsTagger(psiRoot);
        methodsTagger.reset();
        instance.modificationStamps.put(psiRoot.getName(), psiRoot.getModificationStamp());
        rootText = psiRoot.getText();

        psiRoot.accept(TreeVisitor(psiRoot, variablesTagger, methodsTagger));
    }

    @NotNull
    private JavaRecursiveElementVisitor TreeVisitor(PsiFile psiRoot, VariablesTagger variablesTagger, MethodsTagger methodsTagger) {
        return new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                super.visitElement(element);

                if (element instanceof DtlImportvardirective) { // Native variables
                    variablesTagger.getImportedVariables().add(DTLVariable.fromImportVariableDirective((DtlImportvardirective) element));
                } else if (element instanceof DtlImportmethoddirective) { // Native methods
                    methodsTagger.getImportedMethods().add(DTLMethod.fromImportMethodDirective((DtlImportmethoddirective) element));
                } else if (element instanceof DtlIncludedirective) { // Included files
                    String filename = ((DtlIncludedirective) element).getIncludedFilename();
                    addExtractedVariablesAndMethodsFrom(filename);
                } else if (element instanceof DtlVariabledeclStatement) { // Declared variables
                    DtlUsermethod usermethod = PsiTreeUtil.getParentOfType(element, DtlUsermethod.class);
                    if (usermethod == null) { // Global declared variables
                        variablesTagger.getGlobalVariables().addAll(DTLVariable.fromVariableDeclaration((DtlVariabledeclStatement) element));
                    } else // Declarations inside methods
                        variablesTagger.getLocalVariables().addAll(
                                DTLVariable.fromVariableDeclaration((DtlVariabledeclStatement) element).stream().map(
                                        dtlVariable -> new AbstractMap.SimpleEntry<>(dtlVariable, usermethod.getUserDefinedMethod())
                                ).collect(Collectors.toSet())
                        );
                } else if (element instanceof DtlUsermethod) { // Declaration inside signatures
                    DtlUsermethod usermethod = (DtlUsermethod) element;
                    DtlImportvars importvars = usermethod.getImportvars();
                    methodsTagger.getGlobalMethods().add(DTLMethod.fromUserMethod((DtlUsermethod) element)); // Global methods
                    if (importvars != null)
                        variablesTagger.getLocalVariables().addAll(
                                DTLVariable.fromSignature(importvars.getImportvarList()).stream().map(
                                        dtlVariable -> new AbstractMap.SimpleEntry<>(dtlVariable, usermethod.getUserDefinedMethod())
                                ).collect(Collectors.toSet())
                        );
                } else if (element instanceof DtlIterationStatement) { // Declaration inside for/while/etc.
                    DtlIterationStatement iterationStatement = (DtlIterationStatement) element;
                    DtlVariableIdentifier variableIdentifier = iterationStatement.getVariableIdentifier();
                    if (variableIdentifier != null) {
                        variablesTagger.getLocalVariables().add(
                                new AbstractMap.SimpleEntry<>(
                                        new DTLVariable(variableIdentifier.getIdentifierName(), false, ""),
                                        element.getText()
                                )
                        );
                    } else {
                        List<DtlStartingstatement> startingstatementList = iterationStatement.getStartingstatementList();
                        if (!startingstatementList.isEmpty()) {
                            DtlStartingstatement startingstatement = startingstatementList.get(0);
                            if (startingstatement != null) {
                                DtlVariabledeclStatement variabledeclStatement = startingstatement.getVariabledeclStatement();
                                if (variabledeclStatement != null) {
                                    variablesTagger.getLocalVariables().addAll(
                                            DTLVariable.fromVariableDeclaration(variabledeclStatement).stream().map(
                                                    dtlVariable -> new AbstractMap.SimpleEntry<>(dtlVariable, element.getText())
                                            ).collect(Collectors.toSet())
                                    );
                                }
                            }
                        }
                    }
                }
            }

            private void addExtractedVariablesAndMethodsFrom(String filename) {
                VirtualFile virtualFile = searchIncludedFile(psiRoot, filename);

                if (virtualFile != null) {
                    PsiFile file = PsiManager.getInstance(psiRoot.getProject()).findFile(virtualFile);
                    if (file != null) {
                        file.accept(fileVisitor(variablesTagger, methodsTagger));
                    }
                }
            }
        };
    }


    @NotNull
    private JavaRecursiveElementVisitor fileVisitor(VariablesTagger variablesTagger, MethodsTagger methodsTagger) {
        return new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                super.visitElement(element);
                if (element instanceof DtlVariabledeclStatement) {
                    variablesTagger.getIncludedVariables().addAll(DTLVariable.fromVariableDeclaration((DtlVariabledeclStatement) element));
                }
                if (element instanceof DtlUsermethod) {
                    methodsTagger.getIncludedMethods().add(DTLMethod.fromUserMethod((DtlUsermethod) element));
                }
            }
        };
    }

    public static VirtualFile searchIncludedFile(PsiFile psiRoot, String filename) {

        if(virtualFiles.containsKey(filename))
            return virtualFiles.get(filename);

        String path = psiRoot.getVirtualFile().getParent().getPath() + "/" + filename;
        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByNioPath(Path.of(path));
        if (virtualFile == null) { // Try to find the included dtl on common
            path = psiRoot.getVirtualFile().getParent().getParent().getPath() + "/" + "common" + "/" + filename;
            virtualFile = VirtualFileManager.getInstance().findFileByNioPath(Path.of(path));
        }
        if (virtualFile == null) { // Try to find the included dtl on all eoxdr-config git project (if we're in it)
            if(psiRoot.getVirtualFile() == null || psiRoot.getVirtualFile().getParent() == null)
                return null;
            VirtualFile parent = psiRoot.getVirtualFile().getParent();
            if(parent != null && parent.exists()) {
                while (!parent.getName().contains("eoxdr-config")) {
                    parent = parent.getParent();
                    if(parent == null || !parent.exists())
                        return null;
                }

                virtualFile = recursiveFind(parent, filename);
                if(virtualFile == null)
                    return null;
            }
            else
                return null;
        }
        
        virtualFiles.put(filename, virtualFile);
        
        return virtualFile;
    }

    public static VirtualFile recursiveFind(VirtualFile root, String filename) {
        final VirtualFile[] child = {null};
        VfsUtilCore.visitChildrenRecursively(root, new VirtualFileVisitor<VirtualFile>() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                if(file.getName().equals(filename)){
                    child[0] = file;
                }
                return true;
            }
        });

        return child[0];
    }

    public VariablesTagger getVariablesTagger(PsiFile psiRoot) {
        if (!variablesTaggerForFile.containsKey(psiRoot.getName()))
            variablesTaggerForFile.put(psiRoot.getName(), new VariablesTagger());
        return variablesTaggerForFile.get(psiRoot.getName());
    }

    public MethodsTagger getMethodsTagger(PsiFile psiRoot) {
        if (!methodsTaggerForFile.containsKey(psiRoot.getName()))
            methodsTaggerForFile.put(psiRoot.getName(), new MethodsTagger());
        return methodsTaggerForFile.get(psiRoot.getName());
    }
}
