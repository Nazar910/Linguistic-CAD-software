package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;
import com.pyvovar.nazar.syntax.SyntaxPrecedenceTableAnalyzer;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

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

        ifPolizies.put("if ( a > b ) { a = 0 ⁋ }", "a b > m0 УПЛ a 0 = m0:");
        ifPolizies.put("if ( a > b ) { a = a + b ⁋ }", "a b > m0 УПЛ a a b + = m0:");
        ifPolizies.put("if ( a > b ) { if ( a < 0 ) { a = 0 ⁋ } }", "a b > m0 УПЛ a 0 < m1 УПЛ a 0 = m1: m0:");
        ifPolizies.put("if ( a == b ) { if ( a < b ) { if ( a == -1 ) { a = 1 ⁋ } } }",
                        "a b == m0 УПЛ a b < m1 УПЛ a -1 == m2 УПЛ a 1 = m2: m1: m0:");

        forPolizies.put("for ( a = 1 ; a < 10 ; a = a * 2 ) cout << a ⁋",
                        "a 1 = r0 1 = m0: a 10 < m1 УПЛ r0 0 == m2 УПЛ a a 2 * = m2: r0 0 = cout a << m0 БП m1:");
        forPolizies.put("for ( a = 1 ; a < b ; a = a * 2 ) for ( b = 0 ; b < 10 ; b = b + 1 ) a = a + b ⁋",
                "a 1 = r0 1 = m0: a b < m1 УПЛ r0 0 == m2 УПЛ a a 2 * = m2: r0 0 = b 0 = r1 1 = m3: b 10 < m4 УПЛ" +
                        " r1 0 == m5 УПЛ b b 1 + = m5: r1 0 = a a b + = m3 БП m4: m0 БП m1:");
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
        HashMap<String, Integer> rTable = new HashMap<>();

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
                                            rTable,
                                            semicolonIndex,
                                            forClosed == 0);
            }

            operatorPolizStack.clear();

            String expected = entry.getValue();
            String actual = String.join(" ", operatorPolizOut);

            assertEquals(expected, actual);

            operatorPolizOut.clear();

            labelTable.clear();
            rTable.clear();

        }
    }

    @Test
    public void whenGetIfPolizShouldCalculateItWright() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        HashMap<String, Pair<String, String>> idns = new HashMap<>();
        idns.put("a", new Pair<>("int", "2"));
        idns.put("b", new Pair<>("int", "0"));

        String str = "a b > m0 УПЛ a 0 = m0:";
        LinkedList<String> poliz = new LinkedList<>(Arrays.asList(str.split(" ")));

        analyzer.calculatePoliz(poliz, idns, System.out);

        assertEquals("0", idns.get("a").getValue());
        assertEquals("0", idns.get("b").getValue());
    }

    @Test
    public void whenGetWrappedIfPolizShouldCalculateItWright() {
        Pair<ArrayList<LexRecord>, ArrayList<String>> pair = wrightLexSequences.get(0);
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer(pair.getKey(), pair.getValue());

        HashMap<String, Pair<String, String>> idns = new HashMap<>();
        idns.put("a", new Pair<>("int", "0"));
        idns.put("b", new Pair<>("int", "0"));

        String str = "a b == m0 УПЛ a 1 < m1 УПЛ a -1 > m2 УПЛ a 1 = m2: m1: m0:";
        LinkedList<String> poliz = new LinkedList<>(Arrays.asList(str.split(" ")));

        analyzer.calculatePoliz(poliz, idns, System.out);

        assertEquals("1", idns.get("a").getValue());
        assertEquals("0", idns.get("b").getValue());
    }

}
