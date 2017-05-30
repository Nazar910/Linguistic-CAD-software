package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.helpers.Precedence;
import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;

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

    private ArrayList<LinkedList<String>> operatorPoliz = new ArrayList<>();

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
        int opIndex = -1;
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

                    if (right.equals("then")) {

                    } else if (right.equals("}")) {
                        ifFlag = false;
                        this.operatorPoliz.get(opIndex).addLast("}");
                        System.out.println(this.operatorPoliz.get(opIndex));
                        this.operatorPoliz.remove(opIndex--);
                    } else {
                        this.operatorPoliz.get(opIndex).addLast(right);
                    }

                }

                if (right.equals("if")) {
                    this.operatorPoliz.add(new LinkedList<>());
                    opIndex++;
                    this.operatorPoliz.get(opIndex).addLast("if");
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

    public String calculatePoliz(LinkedList<String> poliz) {
        LinkedList<String> stack = new LinkedList<>();

        boolean first = true;
        for (String p : poliz) {

            if (!this.arithmeticOperations.contains(p) && !this.expressionSigns.contains(p)) {

                String item;

                String value = this.idns.get(p);
                if (value != null && !value.equals("") && !first) {
                    item = value;
                } else {
                    item = p;
                }

                stack.push(item);
                first = false;
                continue;
            }
            if (this.arithmeticOperations.contains(p) || this.expressionSigns.contains(p)) {
                String strOp2 = stack.pop();
                String strOp1 = stack.pop();

                if (p.equals("=")) {
                    return strOp1 + "=" + strOp2;
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

        return stack.getLast();
    }
}
