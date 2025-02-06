/*
 * $Id: AnnotatorTest.java 29021 2013-04-10 15:47:16Z masp $
 *
 * Copyright 1998-2006 by Anritsu A/S,
 * Kirkebjerg Alle 86, DK-2605 Broendby, Denmark.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Anritsu A/S.
 */

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlId;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportmethoddirective;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportvardirective;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlVariableIdentifier;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * TODO FILL ME!
 *
 * @author Marco Speranza
 * @version $Revision: $
 */
public class ReferencerTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "test/data/reference";
    }


    public void testGlobalVariableReference() {
        myFixture.configureByFiles("Reference.dtl");
        PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset()).getParent();
        PsiReference[] references = element.getReferences();
        assertEquals("inputGetField", ((DtlId) references[0].resolve()).getNameIdentifier().getText());
    }
}
