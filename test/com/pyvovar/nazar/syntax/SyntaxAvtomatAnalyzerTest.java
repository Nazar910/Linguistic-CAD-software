package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.syntax.SyntaxAvtomatAnalyzer;
import com.pyvovar.nazar.errors.SyntaxError;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by nazar on 5/2/17.
 */
public class SyntaxAvtomatAnalyzerTest {

    private ArrayList<ArrayList<LexRecord>> wrightLexSequences = new ArrayList<>();
    private HashMap<ArrayList<LexRecord>, String> wrongLexSequences = new HashMap<>();

    @Before
    public void before() {
        wrightLexSequences.add(
                        new ArrayList<LexRecord>(Arrays.asList(
                                new LexRecord(1, "prog", 1, 0),
                                new LexRecord(1, "Program", 28, 1),
                                new LexRecord(2, "⁋", 11, 0),
                                new LexRecord(2, "var", 2, 0),
                                new LexRecord(2, "int", 3, 0),
                                new LexRecord(2, "i", 28, 2),
                                new LexRecord(3, "⁋", 11, 0),
                                new LexRecord(3, "{", 9, 0),
                                new LexRecord(3, "i", 28, 2),
                                new LexRecord(3, "=", 19, 0),
                                new LexRecord(3, "1", 29, 1),
                                new LexRecord(4, "⁋", 11, 0),
                                new LexRecord(4, "}", 10, 0),
                                new LexRecord(5, "⁋", 11, 0)
                        ))
        );

        wrongLexSequences.put(
                        new ArrayList<LexRecord>(Arrays.asList(
                                new LexRecord(1, "prog", 1, 0),
                                new LexRecord(1, "Program", 28, 1),
                                new LexRecord(2, "⁋", 11, 0),
                                new LexRecord(2, "var", 2, 0),
                                new LexRecord(2, "int", 3, 0),
                                new LexRecord(2, "i", 28, 2),
//                                new LexRecord(3, "⁋", 11, 0),
                                new LexRecord(3, "{", 9, 0),
                                new LexRecord(3, "i", 28, 2),
                                new LexRecord(3, "=", 19, 0),
                                new LexRecord(3, "1", 29, 1),
                                new LexRecord(4, "⁋", 11, 0),
                                new LexRecord(4, "}", 10, 0),
                                new LexRecord(5, "⁋", 11, 0)
                        )), "Line 3 error: \"⁋\" expected\n" +
                            "Error in state 7"
        );
    }

    @Test
    public void whenWrightLexSequencesReturnSuccess() {

        try {
            for (ArrayList<LexRecord> lexList : wrightLexSequences) {

                SyntaxAvtomatAnalyzer analyzer = new SyntaxAvtomatAnalyzer(lexList);
                analyzer.start();

            }
        } catch (SyntaxError syntaxError) {
            fail("Thrown an exception: " + syntaxError.getMessage());
        }
    }

    @Test
    public void whenWrongLexSequencesThrowException() {

        for (ArrayList<LexRecord> lexList: wrongLexSequences.keySet()) {

            try {
                SyntaxAvtomatAnalyzer analyzer = new SyntaxAvtomatAnalyzer(lexList);
                analyzer.start();
                fail("Expect an exception to be thrown before this message...");
            } catch (SyntaxError syntaxError) {
                //TODO somehow getting with existing key returns null
                assertEquals(wrongLexSequences.get(lexList), syntaxError.getMessage());
            }

        }

    }

}
