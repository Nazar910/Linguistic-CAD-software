package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;
import com.pyvovar.nazar.syntax.SyntaxPrecedenceTableAnalyzer;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by nazar on 5/2/17.
 */
public class SyntaxPrecedenceTableAnalyzerTest {

    private ArrayList<Pair<ArrayList<LexRecord>, ArrayList<String>>> wrightLexSequences = new ArrayList<>();
    private HashMap<Pair<ArrayList<LexRecord>, ArrayList<String>>, String> wrongLexSequences = new HashMap<>();

    private HashMap<LinkedList<String>, LinkedList<String>> expressions = new HashMap<>();
    private HashMap<LinkedList<String>, Double> polizes = new HashMap<>();

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
                                new LexRecord(3, "+", 13, 0),
                                new LexRecord(3, "1", 29, 2),
                                new LexRecord(4, "⁋", 11, 0),
                                new LexRecord(4, "}", 10, 0),
                                new LexRecord(5, "⁋", 11, 0)
                        )), new ArrayList<>(lexDB))
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
                        new ArrayList<>(lexDB)),
                "Error in 3"
        );

        expressions.put(new LinkedList<>(Arrays.asList("i", "+", "k")), new LinkedList<>(Arrays.asList("i", "k", "+")));
        expressions.put(new LinkedList<>(Arrays.asList("3", "+", "4", "*", "2")),
                        new LinkedList<>(Arrays.asList("3", "4", "2", "*", "+")));
        expressions.put(new LinkedList<>(Arrays.asList("3", "+", "4", "*", "2", "/", "(", "1", "-", "5", ")")),
                        new LinkedList<>(Arrays.asList("3", "4", "2", "*", "1", "5", "-", "/", "+")));

        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "+")), 7.0);
        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "2", "*", "+")), 11.0);
        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "2", "*", "1", "5", "-", "/", "+")), 1.0);
    }

    @Test
    public void whenWrightLexSequencesReturnSuccess() {
        System.out.println("=========Wright Lexes Test=========");

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
        System.out.println("=========Wrong Lexes Test=========");

        for (Map.Entry<Pair<ArrayList<LexRecord>, ArrayList<String>>, String> entry: wrongLexSequences.entrySet()) {

            try {
                Pair<ArrayList<LexRecord>, ArrayList<String>> pair = entry.getKey();
                SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());
                analyzer.start();
                fail("Expect an exception to be thrown before this message...");
            } catch (SyntaxError syntaxError) {
                assertEquals(entry.getValue(), syntaxError.getMessage());
            }

        }

    }

    @Test
    public void whenGetExpressionConvertToWrightPoliz() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        for (Map.Entry<LinkedList<String>, LinkedList<String>> entry: expressions.entrySet()) {

            LinkedList<String> actual = analyzer.convertToPoliz(entry.getKey());
            LinkedList<String> expected = entry.getValue();

            assertArrayEquals(expected.toArray(), actual.toArray());

        }
    }

    @Test
    public void whenGetPolizShouldCalculateItWright() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        for (Map.Entry<LinkedList<String>, Double> entry: polizes.entrySet()) {

            double actual = analyzer.calculatePoliz(entry.getKey());
            double expected = entry.getValue();

            assertEquals(expected, actual, 0.001);

        }
    }

}
