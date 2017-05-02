package com.pyvovar.nazar;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by nazar on 5/2/17.
 */
public class LexicalAnalyzerTest {

    private String sample1 = "prog Program\nvar int i\n{i=2\n}";

    private LexicalAnalyzer lexical;

    @Before
    public void before() {
        FileManager mockFileManager = mock(FileManager.class);
        lexical = new LexicalAnalyzer();
    }

    @Test
    public void whenSample1ReturnSuccess() {
        try {
            lexical.start();
        } catch (LexicalError lexicalError) {
            fail("Thrown an exception with sample1: " + lexicalError.getMessage());
        }
    }

    @Test
    public void checkLex() {
        int idn = lexical.checkLex(" idn");
        assertEquals(28, idn);
    }

}
