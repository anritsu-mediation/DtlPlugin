package com.anritsu.intellij.plugin.dtl.taggers;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportmethoddirective;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportvar;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlUsermethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DTLMethod {
    public String name;
    public List<DTLAttribute> attributes;
    public Boolean isFinal;
    public String briefDescription;

    public DTLMethod(String name, List<DTLAttribute> attributes, Boolean isFinal, String briefDescription) {
        this.name = name;
        this.attributes = attributes;
        this.isFinal = isFinal;
        this.briefDescription = briefDescription;
    }

    public static DTLMethod fromImportMethodDirective(DtlImportmethoddirective element) {
        String methodName = element.getNativeMethod();
        boolean methodIsFinal = element.getVariableIdentifier().getVarclass().getText().contains("final");
        List<DTLAttribute> methodAttributes = getMethodAttributesFromImportDirective(element);
        List<DTLMethod> matchingNativeMethods = getMatchingNativeMethods(methodName, methodIsFinal, methodAttributes);
        if (matchingNativeMethods.isEmpty()) {
            return new DTLMethod(methodName, methodAttributes, methodIsFinal, "");
        }
        String methodBriefDescription = matchingNativeMethods.get(0).briefDescription;
        return new DTLMethod(methodName, methodAttributes, methodIsFinal, methodBriefDescription);
    }

    public static DTLMethod fromUserMethod(DtlUsermethod element) {
        String methodName = element.getUserDefinedMethod();
        boolean methodIsFinal = element.getVariableIdentifier().getVarclass().getText().contains("final");
        List<DTLAttribute> methodAttributes = getMethodAttributesFromUserMethod(element);
        return new DTLMethod(methodName, methodAttributes, methodIsFinal, "");
    }

    private static List<DTLMethod> getMatchingNativeMethods(String methodName, boolean methodIsFinal, List<DTLAttribute> methodAttributes) {
        return nativeMethods.stream()
                .filter(dtlMethod -> dtlMethod.name.equals(methodName) && dtlMethod.attributes.size() == methodAttributes.size() && dtlMethod.isFinal == methodIsFinal)
                .collect(Collectors.toList());
    }

    private static List<DTLAttribute> getMethodAttributesFromImportDirective(DtlImportmethoddirective element) {
        List<DTLAttribute> methodAttributes = new ArrayList<>();
        if (element.getImportvars() != null) {
            for (DtlImportvar importVar : element.getImportvars().getImportvarList()) {
                String methodAttributeName = importVar.getIdentifierName();
                boolean methodAttributeIsFinal = importVar.getVariableIdentifier().getVarclass().getText().contains("final");
                methodAttributes.add(new DTLAttribute(methodAttributeName, methodAttributeIsFinal));
            }
        }
        return methodAttributes;
    }

    private static List<DTLAttribute> getMethodAttributesFromUserMethod(DtlUsermethod element) {
        List<DTLAttribute> methodAttributes = new ArrayList<>();
        if (element.getImportvars() != null) {
            for (DtlImportvar importVar : element.getImportvars().getImportvarList()) {
                String methodAttributeName = importVar.getIdentifierName();
                boolean methodAttributeIsFinal = importVar.getVariableIdentifier().getVarclass().getText().contains("final");
                methodAttributes.add(new DTLAttribute(methodAttributeName, methodAttributeIsFinal));
            }
        }
        return methodAttributes;
    }

    public boolean isANativeMethod() {
        List<DTLMethod> matchingNativeMethods = getMatchingNativeMethods(name, isFinal, attributes);
        return !matchingNativeMethods.isEmpty() && !briefDescription.isEmpty();
    }

    private String getPresentableTextForCompletion(boolean inImport) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("(");
        for (int i = 0; i < attributes.size(); ++i) {
            DTLAttribute attribute = attributes.get(i);
            if(inImport)
                stringBuilder.append(attribute.isFinal ? "final " : "").append("var ");
            stringBuilder.append(attribute.name);
            if (i < attributes.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");
        if (inImport)
            stringBuilder.append(";");
        return stringBuilder.toString();
    }

    public String getPresentableTextForCompletionInImport() {
        return getPresentableTextForCompletion(true);
    }

    public String getPresentableTextForCompletionInMethodInvocation(boolean insideMethodInvocation) {
        return getPresentableTextForCompletion(false) + (insideMethodInvocation ? "" : ";");
    }

    @Override
    public String toString() {
        return (isFinal ? "final " : "") + "var " + getPresentableTextForCompletionInImport();
    }

    public static List<DTLMethod> nativeMethods = Arrays.asList(
            /* FINAL */
            new DTLMethod("outputGetFieldFinal", List.of(new DTLAttribute("outFieldRef", true)), true, "Gets output DR field value"),
            new DTLMethod("outputGetFieldFinal", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("drIdx", true)), true, "Gets output DR field value"),
            new DTLMethod("lookupMclsDim", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true)), true, "Looks up a value on a MCLS dimension table"),
            new DTLMethod("lookupMclsInterval", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("value", true)), true, "Looks up a value by range check on a MCLS interval table: will return the record whose range fits provided value."),

            /* NOT FINAL */
            new DTLMethod("createTree", List.of(), false, "Create a tree for external format"),
            new DTLMethod("duplicateTree", List.of(new DTLAttribute("idSourceTree", true)), false, "Duplicate a tree for external format use this function only in dtl process"),
            new DTLMethod("createNode", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("label", true)), false, "Create a node for external format"),
            new DTLMethod("createNode", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("label", true), new DTLAttribute("multipleValues", true)), false, "Create a node for external format"),
            new DTLMethod("createNode", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("label", true), new DTLAttribute("value", true), new DTLAttribute("attributes", true)), false, "Create a node for external format"),
            new DTLMethod("setNodeValue", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("idNode", true), new DTLAttribute("value", true)), false, "Add a relationship between parent and children"),
            new DTLMethod("removeNode", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("idNode", true)), false, "Remove a node from tree"),
            new DTLMethod("addRelationship", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("idParentNode", true), new DTLAttribute("idChildNode", true)), false, "Add a relationship between parent and children"),
            new DTLMethod("addRelationship", Arrays.asList(new DTLAttribute("idTree", true), new DTLAttribute("idParentNode", true), new DTLAttribute("idChildren", true)), false, "Add a relationship between parent and children"),
            new DTLMethod("verifyTree", List.of(new DTLAttribute("idTree", true)), false, "Dump the tree in xml, only for test"),
            new DTLMethod("inputGetScenarioName", List.of(), false, "Provides name of scenario being run"),
            new DTLMethod("getVlanMode", List.of(), false, "Provides VLAN mode use"),
            new DTLMethod("inputLookupConstituent", List.of(new DTLAttribute("constname", true)), false, "finds constituent index by constituent name, return index of constituent inside scenario or null if not found"),
            new DTLMethod("inputGetConstituentName", List.of(new DTLAttribute("constIndex", true)), false, "retrieves the constituent name by constituent index, return name of constituent inside scenario or null if not found"),
            new DTLMethod("inputLookupField", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("name", true)), false, "Finds CSDR field by name, put in context and return index to it or null if not found"),
            new DTLMethod("inputLookupField", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("name", true), new DTLAttribute("mandatory", true)), false, "Finds CSDR field by name, put in context and return index to it or null if not found"),
            new DTLMethod("inputLookupField", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("name", true), new DTLAttribute("mandatory", true), new DTLAttribute("encoding", true)), false, "Finds CSDR field by name, put in context and return index to it or null if not found"),
            new DTLMethod("inputGetField", List.of(new DTLAttribute("fieldRef", true)), false, "Reads field value from trail and returns it"),
            new DTLMethod("inputGetField", Arrays.asList(new DTLAttribute("fieldRef", true), new DTLAttribute("dlgIndex", true)), false, "Reads field value from specified dialogue of trail and returns it"),
            new DTLMethod("inputGetField", Arrays.asList(new DTLAttribute("fieldRef", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Reads field value from specified CSDR of specified dialogue of trail, and returns it"),
            new DTLMethod("inputGetFieldAndRefer", Arrays.asList(new DTLAttribute("fieldRef", true), new DTLAttribute("dlgIndex", false), new DTLAttribute("csdrIndex", false)), false, "Reads field from trail, returns it and writes to provided arguments"),
            new DTLMethod("inputGetFieldAndReferByDlg", Arrays.asList(new DTLAttribute("fieldRef", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", false)), false, "Reads field from trail, returns it and writes to provided arguments"),
            new DTLMethod("inputGetFieldName", List.of(new DTLAttribute("fieldIndex", true)), false, "Retrieves field name by field index and returns it"),
            new DTLMethod("inputGetCsdrCount", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Provides number of CSDRs on specified constituent/dialogue"),
            new DTLMethod("inputGetDialogueCount", List.of(new DTLAttribute("constIndex", true)), false, "Provides number of dialogues on specified constituent"),
            new DTLMethod("inputIsCsdrForward", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("csdrIndex", true)), false, "Returns true if the CSDR is forward"),
            new DTLMethod("inputIsCsdrBackward", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("csdrIndex", true)), false, "Returns true if the CSDR is backward"),
            new DTLMethod("inputGetStartTime", List.of(), false, "Return trail start time"),
            new DTLMethod("inputGetStartTime", List.of(new DTLAttribute("constIndex", true)), false, "Return start time of constituent"),
            new DTLMethod("inputGetStartTime", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Return start time of constituent/dialogue"),
            new DTLMethod("inputGetStartTime", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Return start time of constituent/dialogue/CSDR"),
            new DTLMethod("inputGetEndTime", List.of(), false, "Return end time of trail"),
            new DTLMethod("inputGetEndTime", List.of(new DTLAttribute("constIndex", true)), false, "Return end time of constituent"),
            new DTLMethod("inputGetEndTime", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Return end time of constituent/dialogue"),
            new DTLMethod("inputGetEndTime", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Return end time of constituent/dialogue/CSDR"),
            new DTLMethod("getLinksetId", List.of(new DTLAttribute("constIndex", true)), false, "Returns topology LinkSet ID for provided constituent"),
            new DTLMethod("getLinksetId", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Returns topology LinkSet ID for provided constituent/dialogue"),
            new DTLMethod("getLinksetId", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Returns topology LinkSet ID for provided constituent/dialogue/CSDR"),
            new DTLMethod("getLinksetName", List.of(new DTLAttribute("constIndex", true)), false, "Returns topology LinkSet name for provided constituent"),
            new DTLMethod("getLinksetName", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Returns topology LinkSet name for provided constituent/dialogue"),
            new DTLMethod("getLinksetName", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Returns topology LinkSet name for provided constituent/dialogue/CSDR"),
            new DTLMethod("getDirectionOnLink", List.of(new DTLAttribute("constIndex", true)), false, "Provides direction on link for constituent, got from the first CSDR of the first dialogue of the constituent index"),
            new DTLMethod("getDirectionOnLink", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Provides direction on link for constituent/dialogue"),
            new DTLMethod("getDirectionOnLink", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Provides direction on link for constituent/dialogue/CSDR"),
            new DTLMethod("getCallTraceJump", List.of(), false, "Provides a string containing CallTrace Jump field for trail"),
            new DTLMethod("getCallTraceJump", List.of(new DTLAttribute("constIndex", true)), false, "Provides a string containing CallTrace Jump field for constituent"),
            new DTLMethod("getCallTraceJump", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Provides a string containing CallTrace Jump field for constituent/dialogue"),
            new DTLMethod("getCallTraceJump", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Provides a string containing CallTrace Jump field for constituent/dialogue/CSDR"),
            new DTLMethod("getCallTraceJumpRaw", List.of(), false, "Provides a string containing CallTrace Jump Binary field for trail"),
            new DTLMethod("getCallTraceJumpRaw", List.of(new DTLAttribute("constIndex", true)), false, "Provides a string containing CallTrace Jump Binary field for constituent"),
            new DTLMethod("getCallTraceJumpRaw", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true)), false, "Provides a string containing CallTrace Jump Binary field for constituent/dialogue"),
            new DTLMethod("getCallTraceJumpRaw", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dlgIndex", true), new DTLAttribute("csdrIndex", true)), false, "Provides a string containing CallTrace Jump Binary field for constituent/dialogue/CSDR"),
            new DTLMethod("getSwitchNameByIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("addr", true)), false, "Looks up for a switch in topology according to provided IP address and constituent"),
            new DTLMethod("getPointAndSwitchNameByIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("addr", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false)), false, "Looks up for a point and related switch in topology according to provided IP address and constituent"),
            new DTLMethod("getSwitchAddressDetailsByAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("addr", true), new DTLAttribute("name", false), new DTLAttribute("type", false), new DTLAttribute("usage", false)), false, "Looks up for a switch and its address details in topology according to address(E164,E212,X121) and constituent"),
            new DTLMethod("getNetworkInfoByIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("addr", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided IP address and constituent"),
            new DTLMethod("getNetworkInfoByIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("addr", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided IP address and constituent"),
            new DTLMethod("getNetworkInfoByVLanIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("vlan", true), new DTLAttribute("addr", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided VLan id and IP address and constituent"),
            new DTLMethod("getNetworkInfoByVLanIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("vlan", true), new DTLAttribute("addr", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided VLan id and IP address and constituent"),
            new DTLMethod("getAutomaticNetworkInfoByIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("vlan", true), new DTLAttribute("addr", true), new DTLAttribute("mode", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided IP address and constituent"),
            new DTLMethod("getAutomaticNetworkInfoByIpAddress", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("vlan", true), new DTLAttribute("addr", true), new DTLAttribute("mode", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided IP address and constituent"),
            new DTLMethod("getNetworkInfoByPointCode", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("pCode", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided PointCode address and constituent"),
            new DTLMethod("getNetworkInfoByPointCode", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("pCode", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided PointCode address and constituent"),
            new DTLMethod("getNetworkInfoByPointCode", Arrays.asList(new DTLAttribute("constindex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("csdrIndex", true), new DTLAttribute("pCode", true), new DTLAttribute("pointName", false), new DTLAttribute("switchName", false), new DTLAttribute("switchRoles", false)), false, "Looks up for a point and related switch and its roles in topology according to provided PointCode address and constituent"),
            new DTLMethod("getFullyQualifiedPointcode", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("pcValue", true)), false, "Returns fully qualified point code string upon lookup based on provided constituent and PC value"),
            new DTLMethod("getFullyQualifiedPointcode", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("pcValue", true)), false, "Returns fully qualified point code string upon lookup based on provided constituent and PC value"),
            new DTLMethod("getFullyQualifiedPointcode", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("dialogueIndex", true), new DTLAttribute("csdrIndex", true), new DTLAttribute("pcValue", true)), false, "Returns fully qualified point code string upon lookup based on provided constituent and PC value"),
            new DTLMethod("createDirectDataBinding", List.of(), false, "Creates an empty direct data binding handle and returns it"),
            new DTLMethod("installDirectDataBinding", Arrays.asList(new DTLAttribute("bindingRef", true), new DTLAttribute("constIndex", true), new DTLAttribute("inputName", true), new DTLAttribute("outputName", true)), false, "Installs in bindingRef bindings the provided bindings between data input and data output. This is extremely useful when there is no need to perform data manipulation"),
            new DTLMethod("installDirectDataBinding", Arrays.asList(new DTLAttribute("bindingRef", true), new DTLAttribute("constIndex", true), new DTLAttribute("inputName", true), new DTLAttribute("outputName", true), new DTLAttribute("isMandatory", true)), false, "Installs in bindingRef bindings the provided bindings between data input and data output. This is extremely useful when there is no need to perform data manipulation"),
            new DTLMethod("installDirectDataBinding", Arrays.asList(new DTLAttribute("bindingRef", true), new DTLAttribute("constIndex", true), new DTLAttribute("inOutFieldName", true)), false, "Installs in bindingRef bindings the provided bindings between data input and data output couple. This is extremely useful when there is no need to perform data manipulation"),
            new DTLMethod("processDirectDataBindings", Arrays.asList(new DTLAttribute("bindingRef", true), new DTLAttribute("outIdx", true)), false, "Processes previously installed direct data bindings"),
            new DTLMethod("processDirectDataBindings", Arrays.asList(new DTLAttribute("bindingRef", true), new DTLAttribute("outIdx", true), new DTLAttribute("dlgIndex", true)), false, "Processes previously installed direct data bindings"),
            new DTLMethod("outputSetCount", List.of(new DTLAttribute("drCount", true)), false, "Sets number of output DataRecord. Must be called before setting output fields on output. Usually it is just 1 DR for each trail"),
            new DTLMethod("Sleep", List.of(new DTLAttribute("milliseconds", true)), false, "only for debug, sleep milliseconds"),
            new DTLMethod("DebugLogMessage", Arrays.asList(new DTLAttribute("id", true), new DTLAttribute("msg", true)), false, "DebugLogMessage - Log a message in qxdrs.log if is in DEBUG mode"),
            new DTLMethod("outputGetCount", List.of(), false, "Gets current number of output DataRecords"),
            new DTLMethod("outputSetField", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("value", false)), false, "Writes output DR field value (for DR index 0)"),
            new DTLMethod("outputSetField", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("value", false), new DTLAttribute("drIdx", true)), false, "Writes output DR field value, for any DR index"),
            new DTLMethod("outputGetField", List.of(new DTLAttribute("outFieldRef", true)), false, "Gets output DR field value"),
            new DTLMethod("outputGetField", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("drIdx", true)), false, "Gets output DR field value"),
            new DTLMethod("outputLookupField", List.of(new DTLAttribute("name", true)), false, "Looks up for output field in model and provides reference"),
            new DTLMethod("outputLookupField", Arrays.asList(new DTLAttribute("name", true), new DTLAttribute("mandatory", true)), false, "Looks up for output field in model and provides reference"),
            new DTLMethod("outputSetFieldFromInputField", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("inFieldRef", true)), false, "Writes output DR field value directly from an input field (only for DR index 0)"),
            new DTLMethod("lookupMsisdnByImsi", List.of(new DTLAttribute("imsi", true)), false, "Looks up MSISDN by IMSI by means of MICS"),
            new DTLMethod("lookupImsiByMsisdn", List.of(new DTLAttribute("msisdn", true)), false, "Looks up IMSI by MSISDN by means of MICS"),
            new DTLMethod("prefetchMsisdnByImsi", List.of(new DTLAttribute("key", true)), false, "Performs a prefetch for Msisdn by Imsi"),
            new DTLMethod("prefetchImsiByMsisdn", List.of(new DTLAttribute("key", true)), false, "Performs a prefetch for Imsi by Msisdn"),
            new DTLMethod("prefetchImsiMsisdnPair", Arrays.asList(new DTLAttribute("imsi", true), new DTLAttribute("msisdn", true)), false, "Performs a prefetch for Imsi and Msisdn"),
            new DTLMethod("isPrefetchActiveImsiMsisdnPair", List.of(), false, "Checks whether prefetch is active or not."),
            new DTLMethod("isPrefetchActiveImsiByMsisdn", List.of(), false, "Checks whether prefetch is active or not."),
            new DTLMethod("isPrefetchActiveMsisdnByImsi", List.of(), false, "Checks whether prefetch is active or not."),
            new DTLMethod("lookupOperatorInfoByE212", Arrays.asList(new DTLAttribute("number", true), new DTLAttribute("prefix", false), new DTLAttribute("name", false), new DTLAttribute("country", false)), false, "Looks up Operator informations by E212 and writes to provided arguments"),
            new DTLMethod("lookupOperatorInfoByE164", Arrays.asList(new DTLAttribute("number", true), new DTLAttribute("prefix", false), new DTLAttribute("name", false), new DTLAttribute("country", false)), false, "Looks up Operator informations by E164 and writes to provided arguments"),
            new DTLMethod("lookupOperatorInfoByE212", Arrays.asList(new DTLAttribute("number", true), new DTLAttribute("prefix", false), new DTLAttribute("name", false), new DTLAttribute("country", false), new DTLAttribute("corporatePartner", false), new DTLAttribute("interConnectPartner", false), new DTLAttribute("roamingPartner", false)), false, "Looks up Operator informations by E212 and writes to provided arguments"),
            new DTLMethod("lookupOperatorInfoByE164", Arrays.asList(new DTLAttribute("number", true), new DTLAttribute("prefix", false), new DTLAttribute("name", false), new DTLAttribute("country", false), new DTLAttribute("corporatePartner", false), new DTLAttribute("interConnectPartner", false), new DTLAttribute("roamingPartner", false)), false, "Looks up Operator informations by E164 and writes to provided arguments"),
            new DTLMethod("lookupOperatorInfoByE164", Arrays.asList(new DTLAttribute("number", true), new DTLAttribute("prefix", false), new DTLAttribute("name", false), new DTLAttribute("country", false), new DTLAttribute("corporatePartner", false), new DTLAttribute("interConnectPartner", false), new DTLAttribute("roamingPartner", false), new DTLAttribute("home", false)), false, "Looks up Operator informations by E164 and writes to provided arguments"),
            new DTLMethod("lookupOperatorInfoByE212", Arrays.asList(new DTLAttribute("number", true), new DTLAttribute("prefix", false), new DTLAttribute("name", false), new DTLAttribute("country", false), new DTLAttribute("corporatePartner", false), new DTLAttribute("interConnectPartner", false), new DTLAttribute("roamingPartner", false), new DTLAttribute("home", false)), false, "Looks up Operator informations by E212 and writes to provided arguments"),
            new DTLMethod("mclsGetTable", List.of(new DTLAttribute("name", true)), false, "Returns handle to a MCLS (Lookup Service) table"),
            new DTLMethod("mclsQuery", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true), new DTLAttribute("eam", true), new DTLAttribute("wantTime", true)), false, "Performs a query on MCLS table"),
            new DTLMethod("mclsQuery", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true), new DTLAttribute("colIndexes", true), new DTLAttribute("eam", true), new DTLAttribute("wantTime", true)), false, "Performs a query on MCLS table"),
            new DTLMethod("mclsStore", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true), new DTLAttribute("fields", true), new DTLAttribute("time", true), new DTLAttribute("ow", true)), false, "Stores a record on a MCLS table"),
            new DTLMethod("mclsStore", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true), new DTLAttribute("fields", true), new DTLAttribute("columnIndexes", true), new DTLAttribute("time", true), new DTLAttribute("ow", true)), false, "Stores a record (or some fields of it) on a MCLS table"),
            new DTLMethod("mclsList", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true), new DTLAttribute("maxRec", true), new DTLAttribute("fwd", true), new DTLAttribute("wantTime", true)), false, "Lists content of MCLS table"),
            new DTLMethod("mclsDimTable", Arrays.asList(new DTLAttribute("name", true), new DTLAttribute("refreshTime", true), new DTLAttribute("wantTime", true)), false, "Creates a MCLS dimension table, that can be used later for quick lookup. Table must be little, no more than some thousand records"),
            new DTLMethod("mclsIntervalTable", Arrays.asList(new DTLAttribute("name", true), new DTLAttribute("refreshTime", true), new DTLAttribute("wantTime", true), new DTLAttribute("minRangeColIdx", true), new DTLAttribute("maxRangeColIdx", true)), false, "Creates a MCLS Interval dimension table, that can be used later for quick lookup of intervals. Will return null or record that has (column[minRangeColIdx]<= value < column[maxRangeColIdx])."),
            new DTLMethod("micsFeederStore", Arrays.asList(new DTLAttribute("imsi", true), new DTLAttribute("msisdn", true), new DTLAttribute("time", true)), false, "Store MSISDN/IMSI/Time in MICS using feeder."),
            new DTLMethod("getMclsLookupTable", Arrays.asList(new DTLAttribute("name", true), new DTLAttribute("columnList", true), new DTLAttribute("type", true)), false, "Creates a MCLS lookup table, that can be used later for quick lookup."),
            new DTLMethod("queryMclsLookupTable", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true)), false, "Looks up a value on a MCLS lookup table"),
            new DTLMethod("isMclsLookupTablePrefetchActive", List.of(new DTLAttribute("tableHandle", true)), false, "Checks whether prefetch is active or not."),
            new DTLMethod("prefetchMclsLookupTable", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("key", true)), false, "Performs a prefetch for given table"),
            new DTLMethod("formatIpAddress", Arrays.asList(new DTLAttribute("ipValue", true), new DTLAttribute("ipv4FormWhenMapped", true), new DTLAttribute("shortFormIpv6", true)), false, "Formats an IP address to human readable format"),
            new DTLMethod("getXdrConfigProperty", List.of(new DTLAttribute("name", true)), false, "Reads a configuration property and returns it as string, from application nin file"),
            new DTLMethod("getRandomInteger", Arrays.asList(new DTLAttribute("startOffset", true), new DTLAttribute("size", true)), false, "Generates a random integer"),
            new DTLMethod("getCurrentTime", List.of(), false, "Obtains the current time in millis"),
            new DTLMethod("formatTime", Arrays.asList(new DTLAttribute("timeInMillis", true), new DTLAttribute("strFTimeFormat", true), new DTLAttribute("utcTime", true)), false, "Formats input time (in milliseconds) using the given Unix-strftime string format, either in UTC time or local time (boolean)"),
            new DTLMethod("nativeFormatEoIpUp7_init", List.of(), false, "init() of native format eoIP-TDR-7.0 UP"),
            new DTLMethod("nativeFormatEoIpUp7_process", List.of(), false, "process() of native format eoIP-TDR-7.0 UP"),
            new DTLMethod("msrnMsisdnFeederStore", Arrays.asList(new DTLAttribute("msrn", true), new DTLAttribute("msisdn", true), new DTLAttribute("noa", true), new DTLAttribute("time", true)), false, "Store MSRN/MSISDN/MsNoA Time in MSRN using feeder."),
            new DTLMethod("getLinksetNameByLinksetId", Arrays.asList(new DTLAttribute("constIndex", true), new DTLAttribute("linksetID", true)), false, "Returns topology LinkSet name for provided constituent and ID"),
            new DTLMethod("checkRoamingException", Arrays.asList(new DTLAttribute("tableHandle", true), new DTLAttribute("PLMN", true), new DTLAttribute("IMSI", true), new DTLAttribute("APN", true), new DTLAttribute("GTPC", true), new DTLAttribute("GTPCV2", true)), false, "Check roaming exception on MCLS exception formula table"),
            new DTLMethod("prefetchTrunkGroupTable", Arrays.asList(new DTLAttribute("max_records", false), new DTLAttribute("useNetworkCodes", false), new DTLAttribute("path_separator", false)), false, "Perform a prefetch of TrunkGroup table from mcls"),
            new DTLMethod("lookupTrunkGroupTable", Arrays.asList(new DTLAttribute("opc", false), new DTLAttribute("dpc", false), new DTLAttribute("cic", false)), false, "Look up TrunkGroup table"),
            new DTLMethod("lookupTrunkGroupTableWithNetworkCodes", Arrays.asList(new DTLAttribute("opc", false), new DTLAttribute("dpc", false), new DTLAttribute("cic", false), new DTLAttribute("opcNetworkCode", false), new DTLAttribute("dpcNetworkCode", false)), false, "Look up TrunkGroup table and return"),
            new DTLMethod("operatorHome", Arrays.asList(new DTLAttribute("MNC", false), new DTLAttribute("MCC", false), new DTLAttribute("Name", false)), false, "Get MNC/MCC and Operator Name of first operator with isHome true if present, or any other operator isHome is false. If the CDB operator are not loaded from CDB return null, ture if isHome, false otherwise"),

            /* Cypher */
            new DTLMethod("cipherInit", Arrays.asList(new DTLAttribute("factoryName", true), new DTLAttribute("factoryInitString", true), new DTLAttribute("libraryInitString", true)), false, "Load cipher library using the factory name args, must be called in the Dtl init phase"),
            new DTLMethod("encrypt", Arrays.asList(new DTLAttribute("policy", true), new DTLAttribute("fieldId", true), new DTLAttribute("text", false)), false, "Encrypt the field using the library defined in the CipherInit and the provided policy and fieldId"),
            new DTLMethod("decrypt", Arrays.asList(new DTLAttribute("policy", true), new DTLAttribute("fieldId", true), new DTLAttribute("text", false)), false, "Decrypt the field using the library defined in the CipherInit and the provided policy and fieldId"),

            /* THIRD PARTY METHODS */

            new DTLMethod("getCsdr", List.of(), false, "Called to get each csdr from stream. The fields of CSDR is accessible by means of input accessor methods, and the output DR can be set with related methods."),
            new DTLMethod("StrfTime", Arrays.asList(new DTLAttribute("inputTime", true), new DTLAttribute("formatTime", true), new DTLAttribute("isUtcTime", true)), false, "Transform the date in number of milliseconds from 1970"),
            new DTLMethod("outputLookupField", Arrays.asList(new DTLAttribute("name", true), new DTLAttribute("type", true)), false, "Looks up for output field in model and provides reference"),
            new DTLMethod("outputSetField", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("value", true)), false, "Writes output field value"),
            new DTLMethod("outputSetFieldFromString", Arrays.asList(new DTLAttribute("outFieldRef", true), new DTLAttribute("value", true)), false, "Writes output field value"),
            new DTLMethod("csvPrepareParseStream", Arrays.asList(new DTLAttribute("lineSeparator", true), new DTLAttribute("fieldSeparator", true), new DTLAttribute("nullFieldValue", true)), false, "Configures input for CSV read"),
            new DTLMethod("csvPrepareParseStream", Arrays.asList(new DTLAttribute("comment", true), new DTLAttribute("header", true), new DTLAttribute("lineSeparator", true), new DTLAttribute("fieldSeparator", true), new DTLAttribute("nullFieldValue", true)), false, "Configures input for CSV read"),
            new DTLMethod("csvGetLine", List.of(), false, "Get single line from CSV stream"),
            new DTLMethod("csvGetField", List.of(new DTLAttribute("index", true)), false, "Get CSV value for provided index"),
            new DTLMethod("xmlParseStream", List.of(), false, "Perform parse of XML stream (as a whole)"),
            new DTLMethod("xmlCountNodes", List.of(new DTLAttribute("node", true)), false, "Counts nodes of a specific path"),
            new DTLMethod("xmlCountNodes", Arrays.asList(new DTLAttribute("currNode", true), new DTLAttribute("node", true)), false, "Counts nodes of a specific path"),
            new DTLMethod("xmlGetNode", Arrays.asList(new DTLAttribute("currNode", true), new DTLAttribute("pathNode", true), new DTLAttribute("indexNode", true)), false, "Returns a node by its path"),
            new DTLMethod("xmlGetNode", Arrays.asList(new DTLAttribute("pathNode", true), new DTLAttribute("indexNode", true)), false, "Returns a node by its path"),
            new DTLMethod("xmlSetEoStream", List.of(), false, "Set the end of CSDR stream analisys"),
            new DTLMethod("xmlGetAttribute", Arrays.asList(new DTLAttribute("indexNode", true), new DTLAttribute("nameAttribute", true)), false, "Get attribute value of a specific node"),
            new DTLMethod("xmlGetChildText", Arrays.asList(new DTLAttribute("indexNode", true), new DTLAttribute("tagPathName", true)), false, "Get text value of specific child node"),
            new DTLMethod("xmlGetRepeatedChildText", Arrays.asList(new DTLAttribute("indexNode", true), new DTLAttribute("tagPathName", true), new DTLAttribute("indexChild", true)), false, "Get text value of specific child node"),
            new DTLMethod("xmlGetText", List.of(new DTLAttribute("indexNode", true)), false, "Get text of a node"),
            new DTLMethod("xmlReleaseNode", List.of(new DTLAttribute("indexNode", true)), false, "Releases provided node"),
            new DTLMethod("protoPrepareParseStream", Arrays.asList(new DTLAttribute("nameFileProto", true), new DTLAttribute("rootMessage", true), new DTLAttribute("typeLenFormat", true)), false, "Configures input for protocol-buffer read"),
            new DTLMethod("protoParseNextMessage", List.of(), false, "Parses next protobuf message in the stream"),
            new DTLMethod("protoLookupDescriptor", List.of(new DTLAttribute("relativePathField", true)), false, "Looks up for protobuf field and provides reference"),
            new DTLMethod("protoGetField", Arrays.asList(new DTLAttribute("msgRef", true), new DTLAttribute("indexDesc", true)), false, "Get value of field"),
            new DTLMethod("protoGetRepeatedField", Arrays.asList(new DTLAttribute("msgRef", true), new DTLAttribute("indexDesc", true), new DTLAttribute("indexField", true)), false, "Gets value of field"),
            new DTLMethod("protoCountRepeatedElements", Arrays.asList(new DTLAttribute("msgRef", true), new DTLAttribute("indexDesc", true)), false, "Counts number of field with a specific path"),
            new DTLMethod("protoGetSubMessage", Arrays.asList(new DTLAttribute("msgRef", true), new DTLAttribute("indexDesc", true)), false, "Get reference index of a sub message"),
            new DTLMethod("protoGetSubRepeatedMessage", Arrays.asList(new DTLAttribute("msgRef", true), new DTLAttribute("indexDesc", true), new DTLAttribute("indexRepeatedMsg", true)), false, "Get reference index of a sub repeated message"),
            new DTLMethod("protoReleaseMessage", List.of(new DTLAttribute("msgRef", true)), false, "Releases a protobuf message"),
            new DTLMethod("avroPrepareParseSchema", List.of(new DTLAttribute("nameFileSchema", true)), false, "Configures input for avro-buffer read"),
            new DTLMethod("avroPrepareParseSchema", Arrays.asList(new DTLAttribute("nameFileSchema", true), new DTLAttribute("nameFileSubSchema", true)), false, "Configures input for avro-buffer read"),
            new DTLMethod("avroLookupField", List.of(new DTLAttribute("fullnamePathField", true)), false, "Looks up for avro field and provides reference"),
            new DTLMethod("avroParseNextMessage", List.of(), false, "Parses next avro message in the stream"),
            new DTLMethod("avroGetField", List.of(new DTLAttribute("indexField", true)), false, "Get value of field"),
            new DTLMethod("jsonParseNextMessage", List.of(), false, "Parses next json message in the stream"),
            new DTLMethod("jsonGetField", List.of(new DTLAttribute("key", true)), false, "Get value of field"),
            new DTLMethod("binaryParseNextMessage", List.of(new DTLAttribute("platform", true)), false, "Parses next binary message in the stream"),
            new DTLMethod("binaryParsePartialMessage", Arrays.asList(new DTLAttribute("platform", true), new DTLAttribute("len", true)), false, "Parses next binary message in the stream"),
            new DTLMethod("binaryGetInt8", List.of(), false, "Get signed char value of field"),
            new DTLMethod("binaryGetUInt8", List.of(), false, "Get unsigned char value of field"),
            new DTLMethod("binaryGetInt16", List.of(), false, "Get short int16 value of field"),
            new DTLMethod("binaryGetUInt16", List.of(), false, "Get unsigned short16 value of field"),
            new DTLMethod("binaryGetInt32", List.of(), false, "Get signed integer32 value of field"),
            new DTLMethod("binaryGetUInt32", List.of(), false, "Get unsigned interger32 value of field"),
            new DTLMethod("binaryGetInt64", List.of(), false, "Get signed long64 value of field"),
            new DTLMethod("binaryGetDouble", List.of(), false, "Get double value of field"),
            new DTLMethod("binaryGetString", List.of(new DTLAttribute("len", true)), false, "Get string value of field"),
            new DTLMethod("binaryResetOffset", List.of(), false, "Reset pointer at beginning"),
            new DTLMethod("binaryShiftBytesDx", List.of(new DTLAttribute("numOfBytes", true)), false, "Shift pointer dx"),
            new DTLMethod("binaryShiftBytesSx", List.of(new DTLAttribute("numOfBytes", true)), false, "Shift pointer sx"),

            /* ENRICHMENT METHODS */
            new DTLMethod("mclsTableExists", List.of(new DTLAttribute("name", true)), false, "Checks if a table exists in MC LookupService"),
            new DTLMethod("mclsSelectDistinct", Arrays.asList(new DTLAttribute("tableName", true), new DTLAttribute("columnIndexes", true), new DTLAttribute("maxDistinct", true), new DTLAttribute("maxRecords", true)), false, "Performs a (multiple) select distinct query to MCLS, helpful to get enumeration join values from real data"),
            new DTLMethod("getFormatsByName", List.of(new DTLAttribute("formatName", true)), false, "Provides list of format identifiers, null if not found"),
            new DTLMethod("getFormatById", Arrays.asList(new DTLAttribute("formatName", true), new DTLAttribute("formatId", true)), false, "Returns formatId value if found, null otherwise"),
            new DTLMethod("getListField", List.of(new DTLAttribute("formatId", true)), false, "Provides fields of an eoDR format"),
            new DTLMethod("getFieldName", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("fieldName", true)), false, "Provides field name for a eoDR format if field exists, null otherwise"),
            new DTLMethod("getFieldId", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("fieldId", true)), false, "Returns eoDR format field by its id if found, null otherwise"),
            new DTLMethod("installSimpleEnrichedField", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("name", true), new DTLAttribute("descr", true), new DTLAttribute("df", true), new DTLAttribute("ejv", true), new DTLAttribute("aggRole", true), new DTLAttribute("tableName", true), new DTLAttribute("colIndex", true), new DTLAttribute("origFieldName", true), new DTLAttribute("lookupType", true)), false, "Installs a simple enriched field, mapping a MC LookupService field to a single eoDR format field"),
            new DTLMethod("installSimpleEnrichedField", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("name", true), new DTLAttribute("descr", true), new DTLAttribute("df", true), new DTLAttribute("ejv", true), new DTLAttribute("aggRole", true), new DTLAttribute("tableName", true), new DTLAttribute("colIndex", true), new DTLAttribute("origFieldName", true), new DTLAttribute("lookupType", true),new DTLAttribute("uniqueTableId", true)), false, "Installs a simple enriched field, mapping a MC LookupService field to a single eoDR format field"),
            new DTLMethod("installCompositeEnrichedField", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("name", true), new DTLAttribute("descr", true), new DTLAttribute("df", true), new DTLAttribute("ejv", true), new DTLAttribute("aggRole", true), new DTLAttribute("tableName", true), new DTLAttribute("colIndex", true), new DTLAttribute("origFieldNames", true), new DTLAttribute("separator", true)), false, "Installs a composite enriched field, mapping a MC LookupService field to a concatenation of eoDR format fields"),
            new DTLMethod("installCompositeEnrichedField", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("name", true), new DTLAttribute("descr", true), new DTLAttribute("df", true), new DTLAttribute("ejv", true), new DTLAttribute("aggRole", true), new DTLAttribute("tableName", true), new DTLAttribute("colIndex", true), new DTLAttribute("origFieldNames", true), new DTLAttribute("separator", true), new DTLAttribute("lookupType", true)), false, "Installs a composite enriched field, mapping a MC LookupService field to a concatenation of eoDR format fields"),
            new DTLMethod("installCompositeEnrichedField", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("name", true), new DTLAttribute("descr", true), new DTLAttribute("df", true), new DTLAttribute("ejv", true), new DTLAttribute("aggRole", true), new DTLAttribute("tableName", true), new DTLAttribute("colIndex", true), new DTLAttribute("origFieldNames", true), new DTLAttribute("separator", true), new DTLAttribute("prefix", true), new DTLAttribute("suffix", true)), false, "Installs a composite enriched field, mapping a MC LookupService field to a concatenation of eoDR format fields"),
            new DTLMethod("installCompositeEnrichedField", Arrays.asList(new DTLAttribute("formatId", true), new DTLAttribute("name", true), new DTLAttribute("descr", true), new DTLAttribute("df", true), new DTLAttribute("ejv", true), new DTLAttribute("aggRole", true), new DTLAttribute("tableName", true), new DTLAttribute("colIndex", true), new DTLAttribute("origFieldNames", true), new DTLAttribute("separator", true), new DTLAttribute("prefix", true), new DTLAttribute("suffix", true), new DTLAttribute("uniqueTableId", true)), false, "Installs a composite enriched field, mapping a MC LookupService field to a concatenation of eoDR format fields")
    );

}