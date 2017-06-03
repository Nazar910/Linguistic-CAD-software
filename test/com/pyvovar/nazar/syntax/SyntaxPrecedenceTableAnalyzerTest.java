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
    private HashMap<LinkedList<String>, String> polizes = new HashMap<>();

    private HashMap<String, String> ifPolizies = new HashMap<>();
    private HashMap<String, String> forPolizies = new HashMap<>();

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
                                new LexRecord(3, "0", 29, 1),
                                new LexRecord(3, "⁋", 11, 0),
                                new LexRecord(3, "i", 28, 2),
                                new LexRecord(3, "=", 19, 0),
                                new LexRecord(3, "i", 29, 2),
                                new LexRecord(3, "+", 13, 0),
                                new LexRecord(3, "1", 29, 3),
                                new LexRecord(4, "⁋", 11, 0),
                                new LexRecord(4, "}", 10, 0),
                                new LexRecord(5, "⁋", 11, 0)
                        )), new ArrayList<>(lexDB))
        );

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
                        new LexRecord(3, "0", 29, 1),
                        new LexRecord(3, "⁋", 11, 0),
                        new LexRecord(3, "i", 28, 2),
                        new LexRecord(3, "=", 19, 0),
                        new LexRecord(3, "i", 29, 2),
                        new LexRecord(3, "+", 13, 0),
                        new LexRecord(3, "2", 29, 3),
                        new LexRecord(4, "⁋", 11, 0),
                        new LexRecord(4, "i", 28, 2),
                        new LexRecord(4, "=", 19, 0),
                        new LexRecord(4, "i", 29, 2),
                        new LexRecord(4, "<", 22, 0),
                        new LexRecord(4, "1", 29, 3),
                        new LexRecord(4, "?", 33, 0),
                        new LexRecord(4, "1", 29, 3),
                        new LexRecord(4, ":", 34, 0),
                        new LexRecord(4, "i", 29, 2),
                        new LexRecord(5, "⁋", 11, 0),
                        new LexRecord(5, "}", 10, 0),
                        new LexRecord(6, "⁋", 11, 0)
                )), new ArrayList<>(lexDB))
        );

        wrongLexSequences.put(
                new Pair<>(
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
        expressions.put(new LinkedList<>(Arrays.asList("i", ">", "k")), new LinkedList<>(Arrays.asList("i", "k", ">")));
        expressions.put(new LinkedList<>(Arrays.asList("1", "*", "2", "+", "3")),
                new LinkedList<>(Arrays.asList("1", "2", "*", "3", "+")));
        expressions.put(new LinkedList<>(Arrays.asList("1", "+", "2", "*", "(", "3", "+", "4", ")", "*", "(", "5", "+", "6", ")")),
                new LinkedList<>(Arrays.asList("1", "2", "3", "4", "+", "*", "5", "6", "+", "*", "+")));

        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "+")), "7.0");
        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "2", "*", "+")), "11.0");
        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "2", "*", "1", "5", "-", "/", "+")), "1.0");
        polizes.put(new LinkedList<>(Arrays.asList("3", "4", "<")), "true");

        ifPolizies.put("if ( a > b ) { a = 0 ⁋ }", "a b > m1 УПЛ a 0 = m1 :");
        ifPolizies.put("if ( a > b ) { a = a + b ⁋ }", "a b > m1 УПЛ a a b + = m1 :");
        ifPolizies.put("if ( a > b ) { if ( a < 0 ) { a = 0 ⁋ } }", "a b > m1 УПЛ a 0 < m2 УПЛ a 0 = m2 : m1 :");
        ifPolizies.put("if ( a == b ) { if ( a < b ) { if ( a == -1 ) { a = 1 ⁋ } } }",
                        "a b == m1 УПЛ a b < m2 УПЛ a -1 == m3 УПЛ a 1 = m3 : m2 : m1 :");

        forPolizies.put("for ( i = 0 ; i < 10 ; i = i + 1 ) a = a + i ⁋",
                        "i 0 = m1 : i 10 < m2 УПЛ i i 1 + = a a i + = m1 БП m2 :");
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
    public void whenGetPolizShouldCalculateItWright() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        for (Map.Entry<LinkedList<String>, String> entry: polizes.entrySet()) {

            String actual = analyzer.calculatePoliz(entry.getKey());
            String expected = entry.getValue();

            assertEquals(expected, actual);

        }
    }

    @Test
    public void whenGetIfOperatorShouldCreatePoliz() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        LinkedList<String> operatorPolizStack = new LinkedList<String>();
        LinkedList<String> operatorPolizOut = new LinkedList<String>();
        HashMap<String, Integer> labelTable = new HashMap<>();
        for (Map.Entry<String, String> entry: ifPolizies.entrySet()) {

            for (String right : entry.getKey().split(" ")) {

                analyzer.obtainIfOperator(right, operatorPolizStack, operatorPolizOut, labelTable);
            }

            operatorPolizStack.clear();

            String expected = entry.getValue();
            String actual = String.join(" ", operatorPolizOut);

            assertEquals(expected, actual);

            operatorPolizOut.clear();

            labelTable.clear();

        }
    }

    @Test
    public void whenGetForOperatorShouldCreatePoliz() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        LinkedList<String> operatorPolizStack = new LinkedList<String>();
        LinkedList<String> operatorPolizOut = new LinkedList<String>();
        HashMap<String, Integer> labelTable = new HashMap<>();

        for (Map.Entry<String, String> entry: forPolizies.entrySet()) {

            int semicolonIndex = -1;
            int forClosed = 0;
            for (String right : entry.getKey().split(" ")) {

                if (right.equals(";")) {
                    semicolonIndex++;
                }

                if (right.equals("(")) {
                    forClosed++;
                }

                if (right.equals(")")) {
                    forClosed--;
                }

                analyzer.obtainForOperator(right,
                                            operatorPolizStack,
                                            operatorPolizOut,
                                            labelTable,
                                            semicolonIndex,
                                            forClosed == 0);
            }

            operatorPolizStack.clear();

            String expected = entry.getValue();
            String actual = String.join(" ", operatorPolizOut);

            assertEquals(expected, actual);

            operatorPolizOut.clear();

            labelTable.clear();

        }
    }

}
