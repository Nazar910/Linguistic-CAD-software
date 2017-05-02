package com.pyvovar.nazar;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by nazar on 5/2/17.
 */
public class SyntaxAnalyzerTest {

    private ArrayList<Pair<ArrayList<LexRecord>, ArrayList<IdRecord>>> wrightLexSequences = new ArrayList<>();
    private HashMap<Pair<ArrayList<LexRecord>, ArrayList<IdRecord>>, String> wrongLexSequences = new HashMap<>();

    @Before
    public void before() {
        wrightLexSequences.add(new Pair<>(
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
                        )), new ArrayList<IdRecord>(Arrays.asList(
                new IdRecord("Program", "prog", ""),
                new IdRecord("i", "int", "")
                )))
        );

        wrongLexSequences.put(new Pair<>(
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
                        )), new ArrayList<IdRecord>(Arrays.asList(
                new IdRecord("Program", "prog", ""),
                new IdRecord("i", "int", "")
                ))), "Немає відкриваючої фігурнох дужки : Line = 3\n"
        );
    }

    @Test
    public void whenWrightLexSequencesReturnSuccess() {

        try {
            for (Pair<ArrayList<LexRecord>, ArrayList<IdRecord>> pair : wrightLexSequences) {

                SyntaxAnalyzer analyzer = new SyntaxAnalyzer(pair.getKey(), pair.getValue());
                analyzer.start();

            }
        } catch (SyntaxError syntaxError) {
            fail("Thrown an exception: " + syntaxError.getMessage());
        }
    }

    @Test
    public void whenWrongLexSequencesThrowException() {

        for (Pair<ArrayList<LexRecord>, ArrayList<IdRecord>> pair: wrongLexSequences.keySet()) {

            try {
                SyntaxAnalyzer analyzer = new SyntaxAnalyzer(pair.getKey(), pair.getValue());
                analyzer.start();
                fail("Expect an exception to be thrown before this message...");
            } catch (SyntaxError syntaxError) {
                assertEquals(wrongLexSequences.get(pair), syntaxError.getMessage());
            }

        }

    }

}
