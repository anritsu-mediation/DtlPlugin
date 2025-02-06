package com.anritsu.intellij.plugin.dtl.taggers;

import com.anritsu.intellij.plugin.dtl.parser.psi.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DTLVariable {
    public String name;
    public Boolean isFinal;
    public String briefDescription;

    public DTLVariable(String name, Boolean isFinal, String briefDescription) {
        this.name = name;
        this.isFinal = isFinal;
        this.briefDescription = briefDescription;
    }

    public static DTLVariable fromImportVariableDirective(DtlImportvardirective element) {
        String variableName = element.getNativeVariable();
        boolean variableIsFinal = element.getVariableIdentifier().getVarclass().getText().contains("final");
        List<DTLVariable> matchingNativeVariables = getMatchingNativeVariables(variableName, variableIsFinal);
        if (matchingNativeVariables.isEmpty()) {
            return new DTLVariable(variableName, variableIsFinal, "");
        }
        String methodBriefDescription = matchingNativeVariables.get(0).briefDescription;
        return new DTLVariable(variableName, variableIsFinal, methodBriefDescription);
    }

    public static List<DTLVariable> fromVariableDeclaration(DtlVariabledeclStatement element) {
        List<DTLVariable> dtlVariableList = new ArrayList<>();
        List<DtlVariabledecl> variabledeclList = element.getVariabledeclsline().getVariabledeclList();
        boolean variablesAreFinal = element.getVarclass().getText().contains("final");
        for (DtlVariabledecl variableDecl : variabledeclList) {
            String variableName = variableDecl.getVarName();
            dtlVariableList.add(new DTLVariable(variableName, variablesAreFinal, ""));
        }
        return dtlVariableList;
    }

    private static List<DTLVariable> getMatchingNativeVariables(String variableName, boolean variableIsFinal) {
        return nativeVariables.stream()
                .filter(dtlVariable -> dtlVariable.name.equals(variableName) && dtlVariable.isFinal == variableIsFinal)
                .collect(Collectors.toList());
    }

    public static DTLVariable fromTerminalVariable(DtlExpressionTerminal psiElement, boolean isFinal) {
        return new DTLVariable(psiElement.getIdentifierName(), isFinal, "");
    }

    public static List<DTLVariable> fromSignature(List<DtlImportvar> importvarList) {
        List<DTLVariable> dtlVariables = new ArrayList<>();
        for (DtlImportvar importVar : importvarList) {
            dtlVariables.add(new DTLVariable(importVar.getIdentifierName(), importVar.getVariableIdentifier().getVarclass().getText().contains("final"), ""));
        }
        return dtlVariables;
    }


    public boolean isANativeVariable() {
        List<DTLVariable> matchingNativeVariables = getMatchingNativeVariables(name, isFinal);
        return !matchingNativeVariables.isEmpty() && !briefDescription.isEmpty();
    }

    private String getPresentableTextForCompletion(boolean inImport) {
        StringBuilder stringBuilder = new StringBuilder();
        if (inImport)
            stringBuilder.append("final var ");
        stringBuilder.append(name);
        if (inImport)
            stringBuilder.append(";");
        return stringBuilder.toString();
    }

    public String getPresentableTextForCompletionInImport() {
        return getPresentableTextForCompletion(true);
    }

    public String getPresentableTextForCompletionInMethodInvocation() {
        return getPresentableTextForCompletion(false);
    }

    @Override
    public String toString() {
        return (isFinal ? "final " : "") + "var " + getPresentableTextForCompletionInMethodInvocation();
    }

    public static List<DTLVariable> nativeVariables = Arrays.asList(
            /* FINAL */
            new DTLVariable("INT_BIT_0", true, "INT_BIT_0"),
            new DTLVariable("INT_BIT_1", true, "INT_BIT_1"),
            new DTLVariable("INT_BIT_2", true, "INT_BIT_2"),
            new DTLVariable("INT_BIT_3", true, "INT_BIT_3"),
            new DTLVariable("INT_BIT_4", true, "INT_BIT_4"),
            new DTLVariable("INT_BIT_5", true, "INT_BIT_5"),
            new DTLVariable("INT_BIT_6", true, "INT_BIT_6"),
            new DTLVariable("INT_BIT_7", true, "INT_BIT_7"),
            new DTLVariable("INT_BIT_8", true, "INT_BIT_8"),
            new DTLVariable("INT_BIT_9", true, "INT_BIT_9"),
            new DTLVariable("INT_BIT_10", true, "INT_BIT_10"),
            new DTLVariable("INT_BIT_11", true, "INT_BIT_11"),
            new DTLVariable("INT_BIT_12", true, "INT_BIT_12"),
            new DTLVariable("INT_BIT_13", true, "INT_BIT_13"),
            new DTLVariable("INT_BIT_14", true, "INT_BIT_14"),
            new DTLVariable("INT_BIT_15", true, "INT_BIT_15"),
            new DTLVariable("INT_BIT_16", true, "INT_BIT_16"),
            new DTLVariable("INT_BIT_17", true, "INT_BIT_17"),
            new DTLVariable("INT_BIT_18", true, "INT_BIT_18"),
            new DTLVariable("INT_BIT_19", true, "INT_BIT_19"),
            new DTLVariable("INT_BIT_20", true, "INT_BIT_20"),
            new DTLVariable("INT_BIT_21", true, "INT_BIT_21"),
            new DTLVariable("INT_BIT_22", true, "INT_BIT_22"),
            new DTLVariable("INT_BIT_23", true, "INT_BIT_23"),
            new DTLVariable("INT_BIT_24", true, "INT_BIT_24"),
            new DTLVariable("INT_BIT_25", true, "INT_BIT_25"),
            new DTLVariable("INT_BIT_26", true, "INT_BIT_26"),
            new DTLVariable("INT_BIT_27", true, "INT_BIT_27"),
            new DTLVariable("INT_BIT_28", true, "INT_BIT_28"),
            new DTLVariable("INT_BIT_29", true, "INT_BIT_29"),
            new DTLVariable("INT_BIT_30", true, "INT_BIT_30"),
            new DTLVariable("INT_BIT_31", true, "INT_BIT_31"),
            new DTLVariable("INT_BIT_32", true, "INT_BIT_32"),
            new DTLVariable("INT_BIT_33", true, "INT_BIT_33"),
            new DTLVariable("INT_BIT_34", true, "INT_BIT_34"),
            new DTLVariable("INT_BIT_35", true, "INT_BIT_35"),
            new DTLVariable("INT_BIT_36", true, "INT_BIT_36"),
            new DTLVariable("INT_BIT_37", true, "INT_BIT_37"),
            new DTLVariable("INT_BIT_38", true, "INT_BIT_38"),
            new DTLVariable("INT_BIT_39", true, "INT_BIT_39"),
            new DTLVariable("INT_BIT_40", true, "INT_BIT_40"),
            new DTLVariable("INT_BIT_41", true, "INT_BIT_41"),
            new DTLVariable("INT_BIT_42", true, "INT_BIT_42"),
            new DTLVariable("INT_BIT_43", true, "INT_BIT_43"),
            new DTLVariable("INT_BIT_44", true, "INT_BIT_44"),
            new DTLVariable("INT_BIT_45", true, "INT_BIT_45"),
            new DTLVariable("INT_BIT_46", true, "INT_BIT_46"),
            new DTLVariable("INT_BIT_47", true, "INT_BIT_47"),
            new DTLVariable("INT_BIT_48", true, "INT_BIT_48"),
            new DTLVariable("INT_BIT_49", true, "INT_BIT_49"),
            new DTLVariable("INT_BIT_50", true, "INT_BIT_50"),
            new DTLVariable("INT_BIT_51", true, "INT_BIT_51"),
            new DTLVariable("INT_BIT_52", true, "INT_BIT_52"),
            new DTLVariable("INT_BIT_53", true, "INT_BIT_53"),
            new DTLVariable("INT_BIT_54", true, "INT_BIT_54"),
            new DTLVariable("INT_BIT_55", true, "INT_BIT_55"),
            new DTLVariable("INT_BIT_56", true, "INT_BIT_56"),
            new DTLVariable("INT_BIT_57", true, "INT_BIT_57"),
            new DTLVariable("INT_BIT_58", true, "INT_BIT_58"),
            new DTLVariable("INT_BIT_59", true, "INT_BIT_59"),
            new DTLVariable("INT_BIT_60", true, "INT_BIT_60"),
            new DTLVariable("INT_BIT_61", true, "INT_BIT_61"),
            new DTLVariable("INT_BIT_62", true, "INT_BIT_62"),
            new DTLVariable("INT_BIT_63", true, "INT_BIT_63"),
            new DTLVariable("BYTE_MSB_0", true, "BYTE_MSB_0"),
            new DTLVariable("BYTE_MSB_1", true, "BYTE_MSB_1"),
            new DTLVariable("BYTE_MSB_2", true, "BYTE_MSB_2"),
            new DTLVariable("BYTE_MSB_3", true, "BYTE_MSB_3"),
            new DTLVariable("BYTE_MSB_4", true, "BYTE_MSB_4"),
            new DTLVariable("BYTE_MSB_5", true, "BYTE_MSB_5"),
            new DTLVariable("BYTE_MSB_6", true, "BYTE_MSB_6"),
            new DTLVariable("BYTE_MSB_7", true, "BYTE_MSB_7"),
            new DTLVariable("WORD_MSB_0", true, "WORD_MSB_0"),
            new DTLVariable("WORD_MSB_1", true, "WORD_MSB_1"),
            new DTLVariable("WORD_MSB_2", true, "WORD_MSB_2"),
            new DTLVariable("WORD_MSB_3", true, "WORD_MSB_3"),
            new DTLVariable("WORD_MSB_4", true, "WORD_MSB_4"),
            new DTLVariable("WORD_MSB_5", true, "WORD_MSB_5"),
            new DTLVariable("WORD_MSB_6", true, "WORD_MSB_6"),
            new DTLVariable("WORD_MSB_7", true, "WORD_MSB_7"),
            new DTLVariable("WORD_MSB_8", true, "WORD_MSB_8"),
            new DTLVariable("WORD_MSB_9", true, "WORD_MSB_9"),
            new DTLVariable("WORD_MSB_10", true, "WORD_MSB_10"),
            new DTLVariable("WORD_MSB_11", true, "WORD_MSB_11"),
            new DTLVariable("WORD_MSB_12", true, "WORD_MSB_12"),
            new DTLVariable("WORD_MSB_13", true, "WORD_MSB_13"),
            new DTLVariable("WORD_MSB_14", true, "WORD_MSB_14"),
            new DTLVariable("WORD_MSB_15", true, "WORD_MSB_15"),
            new DTLVariable("DWORD_MSB_0", true, "DWORD_MSB_0"),
            new DTLVariable("DWORD_MSB_1", true, "DWORD_MSB_1"),
            new DTLVariable("DWORD_MSB_2", true, "DWORD_MSB_2"),
            new DTLVariable("DWORD_MSB_3", true, "DWORD_MSB_3"),
            new DTLVariable("DWORD_MSB_4", true, "DWORD_MSB_4"),
            new DTLVariable("DWORD_MSB_5", true, "DWORD_MSB_5"),
            new DTLVariable("DWORD_MSB_6", true, "DWORD_MSB_6"),
            new DTLVariable("DWORD_MSB_7", true, "DWORD_MSB_7"),
            new DTLVariable("DWORD_MSB_8", true, "DWORD_MSB_8"),
            new DTLVariable("DWORD_MSB_9", true, "DWORD_MSB_9"),
            new DTLVariable("DWORD_MSB_10", true, "DWORD_MSB_10"),
            new DTLVariable("DWORD_MSB_11", true, "DWORD_MSB_11"),
            new DTLVariable("DWORD_MSB_12", true, "DWORD_MSB_12"),
            new DTLVariable("DWORD_MSB_13", true, "DWORD_MSB_13"),
            new DTLVariable("DWORD_MSB_14", true, "DWORD_MSB_14"),
            new DTLVariable("DWORD_MSB_15", true, "DWORD_MSB_15"),
            new DTLVariable("DWORD_MSB_16", true, "DWORD_MSB_16"),
            new DTLVariable("DWORD_MSB_17", true, "DWORD_MSB_17"),
            new DTLVariable("DWORD_MSB_18", true, "DWORD_MSB_18"),
            new DTLVariable("DWORD_MSB_19", true, "DWORD_MSB_19"),
            new DTLVariable("DWORD_MSB_20", true, "DWORD_MSB_20"),
            new DTLVariable("DWORD_MSB_21", true, "DWORD_MSB_21"),
            new DTLVariable("DWORD_MSB_22", true, "DWORD_MSB_22"),
            new DTLVariable("DWORD_MSB_23", true, "DWORD_MSB_23"),
            new DTLVariable("DWORD_MSB_24", true, "DWORD_MSB_24"),
            new DTLVariable("DWORD_MSB_25", true, "DWORD_MSB_25"),
            new DTLVariable("DWORD_MSB_26", true, "DWORD_MSB_26"),
            new DTLVariable("DWORD_MSB_27", true, "DWORD_MSB_27"),
            new DTLVariable("DWORD_MSB_28", true, "DWORD_MSB_28"),
            new DTLVariable("DWORD_MSB_29", true, "DWORD_MSB_29"),
            new DTLVariable("DWORD_MSB_30", true, "DWORD_MSB_30"),
            new DTLVariable("DWORD_MSB_31", true, "DWORD_MSB_31"),
            new DTLVariable("QWORD_MSB_0", true, "QWORD_MSB_0"),
            new DTLVariable("QWORD_MSB_1", true, "QWORD_MSB_1"),
            new DTLVariable("QWORD_MSB_2", true, "QWORD_MSB_2"),
            new DTLVariable("QWORD_MSB_3", true, "QWORD_MSB_3"),
            new DTLVariable("QWORD_MSB_4", true, "QWORD_MSB_4"),
            new DTLVariable("QWORD_MSB_5", true, "QWORD_MSB_5"),
            new DTLVariable("QWORD_MSB_6", true, "QWORD_MSB_6"),
            new DTLVariable("QWORD_MSB_7", true, "QWORD_MSB_7"),
            new DTLVariable("QWORD_MSB_8", true, "QWORD_MSB_8"),
            new DTLVariable("QWORD_MSB_9", true, "QWORD_MSB_9"),
            new DTLVariable("QWORD_MSB_10", true, "QWORD_MSB_10"),
            new DTLVariable("QWORD_MSB_11", true, "QWORD_MSB_11"),
            new DTLVariable("QWORD_MSB_12", true, "QWORD_MSB_12"),
            new DTLVariable("QWORD_MSB_13", true, "QWORD_MSB_13"),
            new DTLVariable("QWORD_MSB_14", true, "QWORD_MSB_14"),
            new DTLVariable("QWORD_MSB_15", true, "QWORD_MSB_15"),
            new DTLVariable("QWORD_MSB_16", true, "QWORD_MSB_16"),
            new DTLVariable("QWORD_MSB_17", true, "QWORD_MSB_17"),
            new DTLVariable("QWORD_MSB_18", true, "QWORD_MSB_18"),
            new DTLVariable("QWORD_MSB_19", true, "QWORD_MSB_19"),
            new DTLVariable("QWORD_MSB_20", true, "QWORD_MSB_20"),
            new DTLVariable("QWORD_MSB_21", true, "QWORD_MSB_21"),
            new DTLVariable("QWORD_MSB_22", true, "QWORD_MSB_22"),
            new DTLVariable("QWORD_MSB_23", true, "QWORD_MSB_23"),
            new DTLVariable("QWORD_MSB_24", true, "QWORD_MSB_24"),
            new DTLVariable("QWORD_MSB_25", true, "QWORD_MSB_25"),
            new DTLVariable("QWORD_MSB_26", true, "QWORD_MSB_26"),
            new DTLVariable("QWORD_MSB_27", true, "QWORD_MSB_27"),
            new DTLVariable("QWORD_MSB_28", true, "QWORD_MSB_28"),
            new DTLVariable("QWORD_MSB_29", true, "QWORD_MSB_29"),
            new DTLVariable("QWORD_MSB_30", true, "QWORD_MSB_30"),
            new DTLVariable("QWORD_MSB_31", true, "QWORD_MSB_31"),
            new DTLVariable("QWORD_MSB_32", true, "QWORD_MSB_32"),
            new DTLVariable("QWORD_MSB_33", true, "QWORD_MSB_33"),
            new DTLVariable("QWORD_MSB_34", true, "QWORD_MSB_34"),
            new DTLVariable("QWORD_MSB_35", true, "QWORD_MSB_35"),
            new DTLVariable("QWORD_MSB_36", true, "QWORD_MSB_36"),
            new DTLVariable("QWORD_MSB_37", true, "QWORD_MSB_37"),
            new DTLVariable("QWORD_MSB_38", true, "QWORD_MSB_38"),
            new DTLVariable("QWORD_MSB_39", true, "QWORD_MSB_39"),
            new DTLVariable("QWORD_MSB_40", true, "QWORD_MSB_40"),
            new DTLVariable("QWORD_MSB_41", true, "QWORD_MSB_41"),
            new DTLVariable("QWORD_MSB_42", true, "QWORD_MSB_42"),
            new DTLVariable("QWORD_MSB_43", true, "QWORD_MSB_43"),
            new DTLVariable("QWORD_MSB_44", true, "QWORD_MSB_44"),
            new DTLVariable("QWORD_MSB_45", true, "QWORD_MSB_45"),
            new DTLVariable("QWORD_MSB_46", true, "QWORD_MSB_46"),
            new DTLVariable("QWORD_MSB_47", true, "QWORD_MSB_47"),
            new DTLVariable("QWORD_MSB_48", true, "QWORD_MSB_48"),
            new DTLVariable("QWORD_MSB_49", true, "QWORD_MSB_49"),
            new DTLVariable("QWORD_MSB_50", true, "QWORD_MSB_50"),
            new DTLVariable("QWORD_MSB_51", true, "QWORD_MSB_51"),
            new DTLVariable("QWORD_MSB_52", true, "QWORD_MSB_52"),
            new DTLVariable("QWORD_MSB_53", true, "QWORD_MSB_53"),
            new DTLVariable("QWORD_MSB_54", true, "QWORD_MSB_54"),
            new DTLVariable("QWORD_MSB_55", true, "QWORD_MSB_55"),
            new DTLVariable("QWORD_MSB_56", true, "QWORD_MSB_56"),
            new DTLVariable("QWORD_MSB_57", true, "QWORD_MSB_57"),
            new DTLVariable("QWORD_MSB_58", true, "QWORD_MSB_58"),
            new DTLVariable("QWORD_MSB_59", true, "QWORD_MSB_59"),
            new DTLVariable("QWORD_MSB_60", true, "QWORD_MSB_60"),
            new DTLVariable("QWORD_MSB_61", true, "QWORD_MSB_61"),
            new DTLVariable("QWORD_MSB_62", true, "QWORD_MSB_62"),
            new DTLVariable("QWORD_MSB_63", true, "QWORD_MSB_63"),
            new DTLVariable("FIELD_OPTIONAL", true, "FIELD_OPTIONAL"),
            new DTLVariable("FIELD_MANDATORY", true, "FIELD_MANDATORY"),
            new DTLVariable("LT_MATCH", true, "LT_MATCH"),
            new DTLVariable("LT_PREFIX", true, "LT_PREFIX"),
            new DTLVariable("ENCODING_TEXT", true, "ENCODING_TEXT"),
            new DTLVariable("ENCODING_LATIN1", true, "ENCODING_LATIN1"),

            /* THIRD PARTY VARIABLES */

            new DTLVariable("FORMAT_VARINT32", true, "FORMAT_VARINT32"),
            new DTLVariable("FORMAT_VARINT64", true, "FORMAT_VARINT64"),
            new DTLVariable("FORMAT_LITTLE_ENDIAN32", true, "FORMAT_LITTLE_ENDIAN32"),
            new DTLVariable("FORMAT_LITTLE_ENDIAN64", true, "FORMAT_LITTLE_ENDIAN64"),
            new DTLVariable("FIELD_SIMPLE", true, "FIELD_SIMPLE"),
            new DTLVariable("FIELD_SPECIAL", true, "FIELD_SPECIAL"),

            /* ENRICHMENT VARIABLES */
            new DTLVariable("DF_DIGITS", true, "DF_DIGITS"),
            new DTLVariable("DF_STRING", true, "DF_STRING"),
            new DTLVariable("DF_ENUM", true, "DF_ENUM"),
            new DTLVariable("DF_DEC_ENUM", true, "DF_DEC_ENUM"),
            new DTLVariable("DF_ABSOLUTE_TIME", true, "DF_ABSOLUTE_TIME"),
            new DTLVariable("DF_RELATIVE_TIME", true, "DF_RELATIVE_TIME"),
            new DTLVariable("DF_BINARY_ENUM", true, "DF_BINARY_ENUM"),
            new DTLVariable("DF_POINT_CODE", true, "DF_POINT_CODE"),
            new DTLVariable("DF_CIC", true, "DF_CIC"),
            new DTLVariable("DF_DEC", true, "DF_DEC"),
            new DTLVariable("DF_HEX", true, "DF_HEX"),
            new DTLVariable("DF_IP", true, "DF_IP"),
            new DTLVariable("DF_FIXEDPOINT", true, "DF_FIXEDPOINT"),
            new DTLVariable("DF_BOOLEAN", true, "DF_BOOLEAN"),
            new DTLVariable("DF_FULLYQUALIFIED_POINT_CODE", true, "DF_FULLYQUALIFIED_POINT_CODE"),
            new DTLVariable("DF_UNDEFINED", true, "DF_UNDEFINED"),
            new DTLVariable("SEC_NUMBER", true, "SEC_NUMBER"),
            new DTLVariable("SEC_SMS", true, "SEC_SMS"),
            new DTLVariable("SEC_ENDPOINTDETAILS", true, "SEC_ENDPOINTDETAILS"),
            new DTLVariable("SEC_USSD", true, "SEC_USSD"),
            new DTLVariable("SEC_CREDENTIALS", true, "SEC_CREDENTIALS"),
            new DTLVariable("SEC_UNDEFINED", true, "SEC_UNDEFINED"),
            new DTLVariable("AR_AGGREGABLE_ALWAYS", true, "AR_AGGREGABLE_ALWAYS"),
            new DTLVariable("AR_AGGREGABLE_WITHFILTER", true, "AR_AGGREGABLE_WITHFILTER"),
            new DTLVariable("AR_AGGREGABLE_NEVER", true, "AR_AGGREGABLE_NEVER"),
            new DTLVariable("AR_AGGREGABLE_WITHRANK", true, "AR_AGGREGABLE_WITHRANK"),
            new DTLVariable("AR_UNDEFINED", true, "AR_UNDEFINED"),
            new DTLVariable("LOOKUPTYPE_MATCH", true, "LOOKUPTYPE_MATCH"),
            new DTLVariable("LOOKUPTYPE_PREFIX", true, "LOOKUPTYPE_PREFIX")

    );

}