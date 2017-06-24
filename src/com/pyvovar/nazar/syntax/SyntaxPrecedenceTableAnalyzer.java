package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.helpers.Precedence;
import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;
import javafx.util.Pair;

import java.io.PrintStream;
import java.util.*;

public class SyntaxPrecedenceTableAnalyzer {
    private List<LexRecord> lexList;
    private Map<String, String> grammar;
    private String[][] matr;
    private List<String> tableColumns;
    private List<String> lexDB;
    private LinkedList<String> stack = new LinkedList<>();

    private HashMap<String, String> idns = new HashMap<>();

    private Set<String> arithmeticOperations = new HashSet<>(
            Arrays.asList("*", "/", "+", "-", "="));
    private LinkedList<String> expressionSigns = new LinkedList<>(Arrays.asList(">", "<", "==", "<=", ">="));

    private HashMap<LinkedList<String>, String> globalPolizies = new HashMap<>();

    private LinkedList<String> poliz = new LinkedList<>();

    private LinkedList<String> operatorPolizStack = new LinkedList<>();
    private LinkedList<String> operatorPolizOut = new LinkedList<>();

    private HashMap<String, Integer> labelTable = new HashMap<>();
    private HashMap<String, Integer> rTable = new HashMap<>();

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

        boolean ifFlag = false;
        boolean forFlag = false;

        int semicolonIndex = -1;
        int forClosed = 0;

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

                /*if (right.equals("⁋") || right.equals(";")) {

                    String calculated = this.calculatePoliz(poliz);

                    if (calculated.indexOf('=') != -1) {
                        String[] operands = calculated.split("=");
                        this.idns.put(operands[0], operands[1]);
                    }

                    this.globalPolizies.put(poliz, calculated);

                    this.poliz = new LinkedList<>();
                }*/

                if (ifFlag) {

                    if (obtainIfOperator(this.lexList.get(i).getLex().trim(), this.operatorPolizStack, this.operatorPolizOut, this.labelTable)) {
                        this.operatorPolizStack.clear();

                        System.out.println();
                        this.operatorPolizOut.forEach(elem -> System.out.print(elem + " "));
                        System.out.println();

                        this.operatorPolizOut.clear();
                        this.labelTable.clear();
                        ifFlag = false;
                    }

                }
                
                if (right.equals("for")) {
                    forFlag = true;
                }

                if (forFlag) {

                    String lex = this.lexList.get(i).getLex().trim();

                    if (lex.equals(";")) {
                        semicolonIndex++;
                    }

                    if (lex.equals("(")) {
                        forClosed++;
                    }

                    if (lex.equals(")")) {
                        forClosed--;
                    }

                    if (obtainForOperator(lex,
                            this.operatorPolizStack,
                            this.operatorPolizOut,
                            this.labelTable,
                            this.rTable,
                            semicolonIndex,
                            forClosed == 0)) {
                        operatorPolizStack.clear();

                        System.out.println();
                        this.operatorPolizOut.forEach(elem -> System.out.print(elem + " "));
                        System.out.println();

                        operatorPolizOut.clear();

                        labelTable.clear();
                        rTable.clear();

                        forFlag = false;
                    }

                }


                if (right.equals("if")) {
                    this.operatorPolizStack.addLast("if");
                    ifFlag = true;
                }


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
                String toReduce = stringBuilder.toString();
                if (left.equals("#") && toReduce.equals("<програма>") && nextElem.equals("#")) break;

                String reduced = reduce(toReduce, left, nextElem);
                if (!reduced.equals("404")) {
                    /*String arithmeticSign = "";

                    for (String sign : arithmeticOperations) {
                        if (toReduce.contains(sign)) {
                            arithmeticSign = sign;
                            break;
                        }
                    }

                    if (!arithmeticSign.equals("")) {
                        this.poliz.addLast(arithmeticSign);
                    }

                    if (toReduce.equals("IDN") || toReduce.equals("CON")) {
                        this.poliz.addLast(this.lexList.get(i - 1).getLex());
                    }*/

//                    System.out.println(this.poliz);

                    stack.add(reduced);
                    stack.forEach(el -> System.out.print(el + " "));
                    System.out.println();
                } else {
                    throw new SyntaxError("Error in " + lexList.get(i).getLine());
                }
                i--;
            }
        }


//        for (Map.Entry<LinkedList<String>, String> poliz : this.globalPolizies.entrySet()) {
//            System.out.println(poliz.getKey());
//            System.out.println(poliz.getValue());
//            System.out.println("============");
//        }
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

    public void calculatePoliz(LinkedList<String> poliz,
                                 HashMap<String, Pair<String, String>> idns,
                                 PrintStream out) {
        LinkedList<String> stack = new LinkedList<>();

        String idnName = "";
        for (int i = 0; i < poliz.size(); i++) {
            String p = poliz.get(i);

            if (!this.arithmeticOperations.contains(p) && !this.expressionSigns.contains(p)) {

                if (p.contains(":")) {
                    i++;
                    continue;
                }

                if (poliz.contains(p + ":")) {
                    int index = poliz.indexOf(p + ":");

                    if (poliz.get(i + 1).equals("БП")) {
                        i = index;
                        continue;
                    }

                    //УПЛ
                    Boolean cond = new Boolean(stack.pop());

                    //if condition is false
                    if (!cond) {
                        i = index;
                        continue;
                    }

                    i++;
                    continue;
                }

                String item;

                Pair<String, String> value = idns.get(p);
                if (value != null && !value.getValue().equals("")) {
                    item = value.getValue();
                    idnName = p;
                } else {
                    item = p;
                }

                stack.push(item);
                continue;
            }
            if (this.arithmeticOperations.contains(p) || this.expressionSigns.contains(p)) {
                String strOp2 = stack.pop();
                String strOp1 = stack.pop();

                if (p.equals("=")) {
                    Pair<String, String> idn = idns.get(idnName);
                    idns.put(idnName, new Pair<>(idn.getKey(), strOp2));
                }

                double op2 = Double.parseDouble(strOp2);
                double op1 = Double.parseDouble(strOp1);

                double result = 0;
                boolean wasArithmetic = true;
                switch (p) {
                    case "+":
                        result = op1 + op2;
                        break;
                    case "-":
                        result = op1 - op2;
                        break;
                    case "*":
                        result = op1 * op2;
                        break;
                    case "/":
                        result = op1 / op2;
                        break;
                    default:
                        wasArithmetic = false;
                }

                if (wasArithmetic) {
                    stack.push(result + "");
                    continue;
                }

                boolean boolResult = false;
                switch (p) {
                    case "<":
                        boolResult = op1 < op2;
                        break;
                    case ">":
                        boolResult = op1 > op2;
                        break;
                    case "<=":
                        boolResult = op1 <= op2;
                        break;
                    case ">=":
                        boolResult = op1 >= op2;
                        break;
                    case "==":
                        boolResult = op1 == op2;
                        break;
                }

                stack.push(boolResult + "");
                continue;
            }

            boolean boolOp2 = new Boolean(stack.pop());
            boolean boolOp1 = new Boolean(stack.pop());

            boolean boolResult = false;
            switch (p) {
                case "and":
                    boolResult = boolOp1 && boolOp2;
                    break;
                case "or":
                    boolResult = boolOp1 || boolOp2;
                    break;
            }

            stack.push(boolResult + "");
        }
    }

    public boolean obtainIfOperator(String right,
                                    LinkedList<String> operatorPolizStack,
                                    LinkedList<String> operatorPolizOut,
                                    HashMap<String, Integer> labelTable) {
        String elem;
        switch(right) {

            case "{":
                elem = operatorPolizStack.pollLast();

                while (elem != null) {

                    operatorPolizOut.addLast(elem);

                    String peekLast = operatorPolizStack.peekLast();
                    elem = peekLast.equals("if") || peekLast.startsWith("m")
                            ? null
                            : operatorPolizStack.pollLast();

                }

                int labelIndex = 0;

                if (labelTable.size() > 0) {
                    labelIndex = labelTable.size();
                }

                operatorPolizOut.addLast("m" + labelIndex);
                operatorPolizStack.addLast("m" + labelIndex);
                operatorPolizOut.addLast("УПЛ");

                //put zero as second value for now
                labelTable.put("m" + labelIndex, 0);
                break;
            case "}":
                elem = operatorPolizStack.pollLast();

                while (elem != null && !elem.equals("if")) {

                    if (elem.startsWith("m")) {
                        operatorPolizOut.addLast(elem + ":");
                    } else {
                        operatorPolizOut.addLast(elem);
                    }

                    elem = operatorPolizStack.peekLast().equals("if")
                            ? null
                            : operatorPolizStack.pollLast();

                }
                return true;
            case ">":
            case "<":
            case "==":
            case "!=":
            case ">=":
            case "=<":
            case "=":
            case "+":
            case "-":
            case "*":
            case "/":
                operatorPolizStack.addLast(right);
                break;
            case "if":

                if (operatorPolizStack.contains("if")) break;

                operatorPolizStack.addLast(right);
                break;
            case "⁋":
            case "(":
            case ")":
                break;
            default:
                operatorPolizOut.addLast(right);
        }

        return false;
    }

    public boolean obtainForOperator(String right,
                                    LinkedList<String> operatorPolizStack,
                                    LinkedList<String> operatorPolizOut,
                                    HashMap<String, Integer> labelTable,
                                     HashMap<String, Integer> rTable,
                                     int semicolonIndex,
                                     boolean closedFor) {
        String elem;
        switch(right) {

            case ";":

                elem = operatorPolizStack.pollLast();

                while (elem != null) {

                    operatorPolizOut.addLast(elem);

                    String peekLast = operatorPolizStack.peekLast();
                    elem = peekLast.equals("for") || peekLast.startsWith("m")
                            ? null
                            : operatorPolizStack.pollLast();

                }


                int labelIndex = 0;

                if (labelTable.size() > 0) {
                    labelIndex = labelTable.size();
                }

                if (semicolonIndex % 2 == 0) {
                    operatorPolizOut.addLast("r" + (rTable.size() - 1));
                    operatorPolizOut.addLast("1");
                    operatorPolizOut.addLast("=");

                    operatorPolizOut.addLast("m" + labelIndex + ":");
                    operatorPolizStack.addLast("m" + labelIndex);

                    labelTable.put("m" + labelIndex, 0);
                    return false;
                }

                operatorPolizOut.addLast("m" + labelIndex);
                operatorPolizStack.addLast("m" + labelIndex);
                operatorPolizOut.addLast("УПЛ");

                //put zero as second value for now
                labelTable.put("m" + labelIndex, 0);

                operatorPolizOut.addLast("r" + (rTable.size() - 1));
                operatorPolizOut.addLast("0");
                operatorPolizOut.addLast("==");

                labelIndex++;

                operatorPolizOut.addLast("m" + labelIndex);
                operatorPolizStack.addLast("m" + labelIndex);
                operatorPolizOut.addLast("УПЛ");

                //put zero as second value for now
                labelTable.put("m" + labelIndex, 0);
                break;
            case ")":
                if (!closedFor) {
                    return false;
                }

                elem = operatorPolizStack.pollLast();

                while (elem != null && !elem.startsWith("m")) {

                    operatorPolizOut.addLast(elem);

                    String peekLast = operatorPolizStack.peekLast();
                    elem = peekLast == null || peekLast.startsWith("m")
                            ? null
                            : operatorPolizStack.pollLast();

                }

                //if elem not null -> then we have label in stack
                if (operatorPolizStack.peekLast() != null) {
                    elem = operatorPolizStack.pollLast();

                    operatorPolizOut.addLast(elem + ":");

                    operatorPolizOut.addLast("r" + (rTable.size() - 1));
                    operatorPolizOut.addLast("0");
                    operatorPolizOut.addLast("=");
                }

                break;
            case "⁋":
                elem = operatorPolizStack.pollLast();

                LinkedList<String> labels = new LinkedList<>();
                while (elem != null && !elem.equals("for")) {

                    if (elem.startsWith("m")) {
                        labels.addLast(elem);
                    } else {
                        operatorPolizOut.addLast(elem);
                    }

                    String peekLast = operatorPolizStack.peekLast();
                    elem = peekLast == null || elem.equals("for")
                            ? null
                            : operatorPolizStack.pollLast();

                }

                String second = labels.pollFirst();
                String first = labels.pollFirst();

                while (second != null && first != null) {

                    operatorPolizOut.addLast(first);
                    operatorPolizOut.addLast("БП");
                    operatorPolizOut.addLast(second + ":");

                    second = labels.pollFirst();
                    first = labels.pollFirst();
                }
                return true;
            case ">":
            case "<":
            case "==":
            case "!=":
            case ">=":
            case "=<":
            case "=":
            case "+":
            case "-":
            case "/":
            case "*":
            case "<<":
            case ">>":
                operatorPolizStack.addLast(right);
                break;
            case "for":
                rTable.put("r" + rTable.size(), 0);

                if (operatorPolizStack.contains("for")) break;

                operatorPolizStack.addLast(right);
                break;
            case "(":
                break;
            default:
                operatorPolizOut.addLast(right);
        }

        return false;
    }
}
