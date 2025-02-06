package com.anritsu.intellij.plugin.dtl.taggers;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashSet;

public class VariablesTagger {

    private final HashSet<DTLVariable> includedVariables = new HashSet<>();
    private final HashSet<DTLVariable> importedVariables = new HashSet<>();
    private final HashSet<DTLVariable> globalVariables = new HashSet<>();
    private final HashSet<AbstractMap.SimpleEntry<DTLVariable, String>> localVariables = new HashSet<>();

    public VariablesTagger() {}

    public HashSet<DTLVariable> getImportedVariables() {
        return importedVariables;
    }

    public HashSet<DTLVariable> getGlobalVariables() {
        return globalVariables;
    }

    public HashSet<AbstractMap.SimpleEntry<DTLVariable, String>> getLocalVariables() {
        return localVariables;
    }


    public HashSet<DTLVariable> getIncludedVariables() {
        return includedVariables;
    }

    public void reset() {
        importedVariables.clear();
        globalVariables.clear();
        localVariables.clear();
    }
}
