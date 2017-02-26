package com.pyvovar.nazar;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by pyvov on 26.02.2017.
 */
public class SyntaxPrecedenceTableAnalyzer {
    private List<LexRecord> lexList;
    private Map<String, String> grammar;
    private String[][] matr;
    private List<String> tableColumns;
    private List<String> lexDB = LexicalAnalyzer.getLexDB();
    private LinkedList<String> stack = new LinkedList<>();


    public static void start() throws SyntaxError {
        SyntaxPrecedenceTableAnalyzer analyzer = new SyntaxPrecedenceTableAnalyzer();
        analyzer.startMain();
    }

    private void startMain() throws SyntaxError {
        this.lexList = LexicalAnalyzer.getTableManager().getLexRecords();
        this.lexDB.set(27, "IDN");
        this.lexDB.set(28, "CON");
        Precedence precedence = new Precedence(true);
        this.grammar = precedence.getGrammar();
        this.matr = precedence.calculate();
        this.tableColumns = precedence.getTableColumns();
        System.out.println(reduce("<оп1.> ⁋", "{", "}"));

        stack.add("#");
        for (int i = 0; i < lexList.size(); i++) {
            String left = stack.peekLast();
            String right = this.tableColumns.get(getTableColumnIndex(getLexDBbyIndex(lexList.get(i).getKod() - 1)));
            if (i == lexList.size() - 1 && right.equals("⁋")) {
                right = "#";
            }
            boolean lt = getSign(left, right).equals("<");
            boolean equals = getSign(left, right).equals("=");
            boolean gt = getSign(left, right).equals(">");
            if (lt || equals) {
                stack.add(right);
            } else {
                LinkedList<String> str = new LinkedList<>();
                String backspace = " ";
                while (!getSign(left, right).equals("<")) {
                    str.addFirst(left);
                    str.addFirst(backspace);
                    right = stack.pollLast();
                    left = stack.peekLast();
                }
                str.removeFirst();
                StringBuilder stringBuilder = new StringBuilder();
                for (String s1 : str) {
                    stringBuilder.append(s1);
                }
                String nextElem = this.tableColumns.get(getTableColumnIndex(getLexDBbyIndex(lexList.get(i).getKod() - 1)));
                if (i == lexList.size() - 1 && nextElem.equals("⁋")) {
                    nextElem = "#";
                }
                if (nextElem.equals("}")) {
                    System.out.print("");
                }
                if (left.equals("#") && stringBuilder.toString().equals("<програма>") && nextElem.equals("#")) break;
                String reduced = reduce(stringBuilder.toString(), left, nextElem);
                if (!reduced.equals("404")) {
                    stack.add(reduced);
                } else {
                    throw new SyntaxError("Error in " + lexList.get(i).getLine());
                }
                i--;
            }
        }
    }

    private String reduce(String elem, String previousElem, String nextElem) {
        for (String s : grammar.keySet()) {
            String[] array = grammar.get(s).split("\\|");
            for (String str : array) {
                if (str.equals(elem)
                        && !getSign(s, nextElem).equals("")
                        && !getSign(previousElem, s).equals("")) {
                    return s;
                }
            }
        }
        return "404";
    }

    private String getSign(String left, String right) {
        int leftIndex = getTableColumnIndex(left);
        int rightIndex = getTableColumnIndex(right);
        return
                matr[leftIndex][rightIndex];
    }

    private int getTableColumnIndex(String left) {
        return this.tableColumns.indexOf(left);
    }

    private String getLexDBbyIndex(int index) {
        return this.lexDB.get(index);
    }

}
