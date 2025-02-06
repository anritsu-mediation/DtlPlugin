package com.anritsu.intellij.plugin.dtl.reference;

import com.anritsu.intellij.plugin.dtl.DtlFileType;
import com.anritsu.intellij.plugin.dtl.parser.DtlFile;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class DtlElementFactory {
  public static PsiElement createVariableIdentifier(Project project, String name) {
    final DtlFile file = createFile(project, name);
    return file.getFirstChild();
  }

  public static DtlFile createFile(Project project, String text) {
    String name = "dummy.dtl";
    return (DtlFile) PsiFileFactory.getInstance(project).
        createFileFromText(name, DtlFileType.INSTANCE, text);
  }
}