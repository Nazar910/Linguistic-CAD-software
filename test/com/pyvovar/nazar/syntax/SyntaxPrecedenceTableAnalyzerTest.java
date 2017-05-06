package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;
import com.pyvovar.nazar.syntax.SyntaxPrecedenceTableAnalyzer;
import javafx.util.Pair;
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
public class SyntaxPrecedenceTableAnalyzerTest {

    private ArrayList<Pair<ArrayList<LexRecord>, ArrayList<String>>> wrightLexSequences = new ArrayList<>();
    private HashMap<Pair<ArrayList<LexRecord>, ArrayList<String>>, String> wrongLexSequences = new HashMap<>();

    private ArrayList<String> lexDB = new ArrayList<>(Arrays.asList(
            "prog", "var", "int", "real", "cout", "cin", "if", "for", "{", "}",
                "⁋", ",", "+", "-", "*", "/", "(", ")", "=", "<<", ">>", "<", ">",
                "<=", ">=", "==", "!=", "idn", "con", "and", "or", "not", "?",
                ":", ";", "[", "]"
    ));

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
                        )), lexDB)
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
                        )),
                        lexDB),
                "Error in 3"
        );
    }

    @Test
    public void whenWrightLexSequencesReturnSuccess() {

        try {
            for (Pair<ArrayList<LexRecord>, ArrayList<String>> pair : wrightLexSequences) {

                SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());
                analyzer.start();

            }
        } catch (SyntaxError syntaxError) {
            fail("Thrown an exception: " + syntaxError.getMessage());
        }
    }

    @Test
    public void whenWrongLexSequencesThrowException() {

        for (Pair<ArrayList<LexRecord>, ArrayList<String>> pair: wrongLexSequences.keySet()) {

            try {
                SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());
                analyzer.start();
                fail("Expect an exception to be thrown before this message...");
            } catch (SyntaxError syntaxError) {
                assertEquals(wrongLexSequences.get(pair), syntaxError.getMessage());
            }

        }

    }

}
