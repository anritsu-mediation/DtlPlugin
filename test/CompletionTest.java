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

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlImportmethoddirective;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.Arrays;
import java.util.List;

/**
 * TODO FILL ME!
 *
 * @author Marco Speranza
 * @version $Revision: $
 */
public class CompletionTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "test/data/completion";
    }


    public void testGlobalVariableReference() {
        myFixture.configureByFiles("Completion.dtl");
        myFixture.complete(CompletionType.BASIC);
        List<String> strings = myFixture.getLookupElementStrings();
        assertEquals(2, strings.size());
        assertTrue(strings.containsAll(Arrays.asList("inputGetField(var param1)", "inputGetField(var param1, var param2)")));

    }
}
