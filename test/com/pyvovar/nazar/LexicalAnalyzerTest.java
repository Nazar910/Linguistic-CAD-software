package com.pyvovar.nazar;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by nazar on 5/2/17.
 */
public class LexicalAnalyzerTest {

    private ArrayList<String> rightSamples = new ArrayList<>();

    private HashMap<String, String> wrongSamples = new HashMap<>();

    private LexicalAnalyzer lexical;

    @Before
    public void before() {
        FileManager mockFileManager = mock(FileManager.class);

        rightSamples.add("prog Program\nvar int i\n{i=2\n}");
        rightSamples.add("prog Program\nvar int i\n{cout<<2\n}");

        wrongSamples.put("prog Program\nvar int i\n{i|=2\n}", "Лексична помилка! рядок = 3");
    }

    @Test
    public void whenRightSamplesReturnSuccess() {
        try {
            for(String s: rightSamples) {
                lexical = new LexicalAnalyzer(s);
                lexical.start();
            }
        } catch (LexicalError lexicalError) {
            fail("Thrown an exception: " + lexicalError.getMessage());
        }
    }

    @Test
    public void whenWrongSampleThrowException() {
        for (String code: wrongSamples.keySet()) {
            try {
                lexical = new LexicalAnalyzer(code);
                lexical.start();
                fail("Expect an exception to be thrown before this message...");
            } catch (LexicalError lexicalError) {
                assertEquals(wrongSamples.get(code), lexicalError.getMessage());
            }
        }
    }

    @Test
    public void checkLex() {
        lexical = new LexicalAnalyzer();
        int idn = lexical.checkLex(" idn");
        assertEquals(28, idn);
    }

}
