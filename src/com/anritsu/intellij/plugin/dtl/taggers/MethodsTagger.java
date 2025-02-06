package com.anritsu.intellij.plugin.dtl.taggers;

import java.util.HashSet;

public class MethodsTagger {
    private final HashSet<DTLMethod> importedMethods = new HashSet<>();
    private final HashSet<DTLMethod> includedMethods = new HashSet<>();
    private final HashSet<DTLMethod> globalMethods = new HashSet<>();

    public MethodsTagger() {}

    public HashSet<DTLMethod> getImportedMethods() {
        return importedMethods;
    }

    public HashSet<DTLMethod> getIncludedMethods() {
        return includedMethods;
    }

    public HashSet<DTLMethod> getGlobalMethods() {
        return globalMethods;
    }

    public void reset() {
        importedMethods.clear();
        includedMethods.clear();
        globalMethods.clear();
    }
}
