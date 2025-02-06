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

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

/**
 * TODO FILL ME!
 *
 * @author Marco Speranza
 * @version $Revision: $
 */
public class VariableAnnotatorTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "test/data";
    }


    public void testAnnotator() {
        myFixture.configureByFiles("VariableAnnotator.dtl");
        myFixture.checkHighlighting(true, true, true, false);
    }


    public void testAnnotatorVariableWithSameNameOnDifferentMethod() {
        myFixture.configureByFiles("VariableOnMultiMethodsAnnotator.dtl");
        myFixture.checkHighlighting(true, true, true, false);
    }

    public void testAnnotatorVariableDeclaredWithImportStatement() {
        myFixture.configureByFiles("VariableOnGlobalImportAnnotator.dtl");
        myFixture.checkHighlighting(true, true, true, false);
    }

    public void testAnnotatorVariableDeclaredintoForeachStatement() {
        myFixture.configureByFiles("VariableWithForEachAnnotator.dtl");
        myFixture.checkHighlighting(true, true, true, false);
    }

    public void testAnnotatorVariableDefinedAfterInvocationAnnotator() {
        myFixture.configureByFiles("VariableDefinedAfterInvocationAnnotator.dtl");
        myFixture.checkHighlighting(true, true, true, false);
    }
}
