package com.anritsu.intellij.plugin.dtl.structure;

import com.anritsu.intellij.plugin.dtl.parser.DtlFile;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class DtlStructureViewModel extends StructureViewModelBase implements
    StructureViewModel.ElementInfoProvider {
  public DtlStructureViewModel(PsiFile psiFile) {
    super(psiFile, new DtlStructureViewElement(psiFile));
  }

  @NotNull
  public Sorter[] getSorters() {
    return new Sorter[]{Sorter.ALPHA_SORTER};
  }


  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    return element instanceof DtlFile;
  }
}