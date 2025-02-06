package com.anritsu.intellij.plugin.dtl.taggers;

public class DTLAttribute {
    String name;
    Boolean isFinal;

    public DTLAttribute(String name, Boolean isFinal) {
        this.name = name;
        this.isFinal = isFinal;
    }

    @Override
    public String toString() {
        return (isFinal ? "final " : "") + "var " + name;
    }
}
