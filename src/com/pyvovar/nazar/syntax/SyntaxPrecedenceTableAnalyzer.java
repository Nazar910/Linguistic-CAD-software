package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.helpers.Precedence;
import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class SyntaxPrecedenceTableAnalyzer {
    private List<LexRecord> lexList;
    private Map<String, String> grammar;
    private String[][] matr;
    private List<String> tableColumns;
    private List<String> lexDB;
    private LinkedList<String> stack = new LinkedList<>();
    private LinkedList<String> expression = new LinkedList<>();

    private HashMap<String, String> idns = new HashMap<>();

    private Set<String> arithmeticOperations = new HashSet<>(
            Arrays.asList("*", "/", "+", "-"));

    public SyntaxPrecedenceTableAnalyzer(List<LexRecord> lexList, List<String> lexDB) {
        this.lexList = lexList;

        lexList.stream()
                .filter(elem -> elem.getKod() == 28)
                .forEach(elem -> idns.put(elem.getLex().trim(), ""));

        System.out.println(idns);
        this.lexDB = lexDB;
        this.lexDB.set(27, "IDN");
        this.lexDB.set(28, "CON");

        Precedence precedence = new Precedence(true);
        this.grammar = precedence.getGrammar();
        this.matr = precedence.calculate();
        this.tableColumns = precedence.getTableColumns();
    }

    public void start() throws SyntaxError {

        stack.add("#");
        int polizIndex = -1;
        for (int i = 0; i < lexList.size(); i++) {
            String left = stack.peekLast();
            String right = this.tableColumns.get(getTableColumnIndex(getLexDBbyIndex(lexList.get(i).getKod() - 1)));

            boolean forLoop = false;
            if (right.equals(";")) {
                forLoop = true;
            }

            if ((right.equals("⁋") || forLoop || right.equals(")")) && expression.size() != 0) {
                LinkedList<String> buff = new LinkedList<>();
                expression.forEach(buff::addFirst);
                System.out.println(buff);
                buff = buff.stream()
                        .map(String::trim)
                        .collect(Collectors.toCollection(LinkedList::new));

                String idn = buff.getFirst();
                int index = buff.indexOf("=");
                if (index >= 0) {
                    LinkedList<String> toPoliz = new LinkedList<>(buff.subList(index + 1, buff.size()));
                    LinkedList<String> poliz = convertToPoliz(toPoliz);
                    double result = calculatePoliz(poliz);
                    this.idns.put(idn, result + "");
                    System.out.println("Result = " + result);
                }
                expression = new LinkedList<>();
            }

            if ((right.equals("IDN") || right.equals("CON")
                    || this.arithmeticOperations.contains(right) || right.equals("=")) && polizIndex != i) {
                expression.push(lexList.get(i).getLex());
                polizIndex = i;
            }

            if (i == lexList.size() - 1 && right.equals("⁋")) {
                right = "#";
            }
            boolean lt = getSign(left, right).equals("<");
            boolean equals = getSign(left, right).equals("=");
            boolean gt = getSign(left, right).equals(">");
            if (lt || equals) {
                stack.add(right);
                stack.forEach(el -> System.out.print(el + " "));
                System.out.println();
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
                    stack.forEach(el -> System.out.print(el + " "));
                    System.out.println();
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

    public LinkedList<String> convertToPoliz(LinkedList<String> expression) {
        LinkedList<String> poliz = new LinkedList<>();
        LinkedList<String> operators = new LinkedList<>();

        LinkedList<String> plusMinus = new LinkedList<>(Arrays.asList("+", "-"));
        LinkedList<String> mulDiv = new LinkedList<>(Arrays.asList("*", "/"));

        for (String e : expression) {

            if (this.arithmeticOperations.contains(e) || e.equals("(")) {
                //if e is +, -, / or *

                if (operators.peekLast() == null) {
                    //if it is first operator in stack
                    operators.addLast(e);
                    continue;
                }

                if (mulDiv.contains(e) && mulDiv.contains(operators.peekLast())) {
                    //if it is * or / we already have * or / in stack
                    poliz.addLast(operators.pollLast());
                }

                if (plusMinus.contains(e) && plusMinus.contains(operators.peekLast())) {
                    //if it + or - and we already have + or - in stack
                    poliz.addLast(operators.pollLast());
                }

                operators.addLast(e);
                continue;
            }

            if (e.equals(")")) {
                //if it is ")" move all operators to poliz except "("
                while (!operators.peekLast().equals("(")) {
                    poliz.addLast(operators.pollLast());
                }
                operators.pollLast();
                continue;
            }

            poliz.addLast(e);
        }

        while (operators.size() != 0) {
            poliz.addLast(operators.pollLast());
        }

        return poliz;
    }

    public double calculatePoliz(LinkedList<String> poliz) {
        LinkedList<Double> stack = new LinkedList<>();

        for (String p : poliz) {

            if (!this.arithmeticOperations.contains(p)) {

                double item;

                String value = this.idns.get(p);
                if (value != null) {
                    item = Double.parseDouble(value);
                } else {
                    item = Double.parseDouble(p);
                }

                stack.push(item);
                continue;
            }

            double op2 = stack.pop();
            double op1 = stack.pop();
            switch (p) {
                case "+":
                    stack.push(op1 + op2);
                    break;
                case "-":
                    stack.push(op1 - op2);
                    break;
                case "*":
                    stack.push(op1 * op2);
                    break;
                case "/":
                    stack.push(op1 / op2);
                    break;
            }
        }

        return stack.getLast();
    }

}
