package com.pyvovar.nazar;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by pyvov on 18.11.2016.
 */
public class SyntaxAvtomatAnalyzer {
    private static List<Record> avtomatList = new ArrayList<>();
    private static List<LexRecord> lexList;
    private static Stack stack;
    private static int alpha;

    private static int getAlpha() {
        return alpha;
    }

    private static void setAlpha(int a) {
        alpha = a;
    }

    private static int popStack() {
        if (!stack.isEmpty()) {
            return Integer.parseInt(stack.pop().toString());
        }
        return 0;
    }

    private static void resetStatic() {
        alpha = 1;
        stack.clear();
    }
    private static Record get(int alpha){
        Record res = null;
        for(Record avt:avtomatList){
            if(avt!=null && alpha == avt.getAlpha())
                return res = avt;
        }
        return res;
    }
    private static void checkStack(String str) {
        if (str.equals("")) return;
        if (str.contains("push")) {
            int push1 = str.indexOf("push"), push2 = push1 + 4;
            if (str.contains("pop")) {
                if (!stack.isEmpty()) {
                    int pop = str.indexOf("pop") + 3;
                    int popValue = (int) stack.pop();
                    if (popValue != Integer.parseInt(str.substring(pop, push1))) {
                        System.out.println("Error");
                    }
                } else {
                    System.out.println("Empty stack!!");
                }
            }
            stack.push(str.substring(push2, str.length()));
        } else if (str.contains("pop")) {
            int pop1 = str.indexOf("pop"), pop2 = pop1 + 3;
            int p = Integer.parseInt(stack.pop().toString());
            if (p != Integer.parseInt(str.substring(pop2, str.length()))) {
                System.out.println("Error");
            }
        }
    }

    static {
        stack = new Stack();
        avtomatList.add(null);
        avtomatList.add(new Record(1,new int[]{1}, new int[]{2}, new String[]{""}, "", "error: Program have to start with prog"));//1 prog
        avtomatList.add(new Record(2,new int[]{28}, new int[]{3}, new String[]{""}, "", "error: After \"prog\" must be idn"));//2 idn
        avtomatList.add(new Record(3,new int[]{11}, new int[]{4}, new String[]{""}, "", "error: \"⁋\" expected"));//3 ⁋
        avtomatList.add(new Record(4,new int[]{2}, new int[]{5}, new String[]{""}, "", "error: Expected var"));//4 var
        avtomatList.add(new Record(5,new int[]{3, 4}, new int[]{6, 6}, new String[]{"", ""}, "", "error: Type have to be int or real"));//5 int real
        avtomatList.add(new Record(6,new int[]{28}, new int[]{7}, new String[]{""}, "", "error: Expected idn"));//6 idn
        avtomatList.add(new Record(7,new int[]{12, 11}, new int[]{6, 8}, new String[]{"", ""}, "", "error: \"⁋\" expected"));//7 , ⁋
        avtomatList.add(new Record(8,new int[]{3, 4, 9}, new int[]{6, 6, 11}, new String[]{"", "", "push9"}, "", ""));//8 int real <op>
        avtomatList.add(new Record(9,new int[]{11}, new int[]{10}, new String[]{""}, "", "error: Operators have to be divided by \"⁋\""));//9 ⁋
        avtomatList.add(new Record(10,new int[]{10, 0}, new int[]{0, 11}, new String[]{"", "push9"}, "exit", ""));//10 }
        avtomatList.add(new Record(11,new int[]{5, 6, 28, 8, 7, 17}, new int[]{12, 15, 47, 18, 31, 49}, new String[]{"", "", "", "", "", ""}, "", "error: Operator have to be cout,cin,for,if,assignment or ternary operator"));//11 cout cin idn for if (
        avtomatList.add(new Record(12,new int[]{20}, new int[]{13}, new String[]{""}, "", "error: Operands of cout have to be divided by \"<<\""));//12 <<
        avtomatList.add(new Record(13,new int[]{28, 29}, new int[]{14, 14}, new String[]{"", ""}, "", "error: Operands of cout must be idn or con"));//13 idn con
        avtomatList.add(new Record(14,new int[]{20}, new int[]{13}, new String[]{""}, "", "exit"));//14 <<
        avtomatList.add(new Record(15,new int[]{21}, new int[]{16}, new String[]{""}, "", "error: Operators of cin have to be divided by \">>\""));//15 >>
        avtomatList.add(new Record(16,new int[]{28}, new int[]{17}, new String[]{""}, "", "error: Expecting idn in cin"));//16 idn
        avtomatList.add(new Record(17,new int[]{21}, new int[]{16}, new String[]{""}, "", "exit"));//17 >>
        avtomatList.add(new Record(18,new int[]{17}, new int[]{19}, new String[]{""}, "", "error: Expecting \"(\" after \"for\""));//18 (
        avtomatList.add(new Record(19,new int[]{28}, new int[]{20}, new String[]{""}, "", "error: Expecting idn"));//19 idn
        avtomatList.add(new Record(20,new int[]{19}, new int[]{21}, new String[]{""}, "", "error: Expecting \"=\""));//20 =
        avtomatList.add(new Record(21,new int[]{0}, new int[]{39}, new String[]{"push22"}, "", ""));//21 <E> push22
        avtomatList.add(new Record(22,new int[]{35}, new int[]{23}, new String[]{""}, "", "error: Expecting \";\""));//22 ;
        avtomatList.add(new Record(23,new int[]{0}, new int[]{42}, new String[]{"push24"}, "", ""));//23 <LE> push24
        avtomatList.add(new Record(24,new int[]{35}, new int[]{25}, new String[]{""}, "", "error: Expecting \";\""));//24 ;
        avtomatList.add(new Record(25,new int[]{28}, new int[]{26}, new String[]{""}, "", "error: Expecting idn"));//25 idn
        avtomatList.add(new Record(26,new int[]{19}, new int[]{27}, new String[]{""}, "", "error: Expecting \"=\""));//26 =
        avtomatList.add(new Record(27,new int[]{0}, new int[]{39}, new String[]{"push28"}, "", ""));//27 <E> push28
        avtomatList.add(new Record(28,new int[]{18}, new int[]{29}, new String[]{""}, "", "error: Expecting \")\""));//28 )
        avtomatList.add(new Record(29,new int[]{0}, new int[]{11}, new String[]{"push30"}, "", ""));//29 <op> push30
        avtomatList.add(new Record(30,new int[]{0}, new int[]{0}, new String[]{""}, "", "exit"));//30
        avtomatList.add(new Record(31,new int[]{17}, new int[]{32}, new String[]{""}, "", "error: Expecting \"(\" after \"if\""));//31 (
        avtomatList.add(new Record(32,new int[]{0}, new int[]{39}, new String[]{"push33"}, "", ""));//32 <E> push33
        avtomatList.add(new Record(33,new int[]{23, 25, 22, 24, 26, 27}, new int[]{34, 34, 34, 34, 34, 34}, new String[]{"", "", "", "", "", ""}, "", "error: Expecting reference sign"));//33 > >= < <= == !=
        avtomatList.add(new Record(34,new int[]{0}, new int[]{39}, new String[]{"push35"}, "", ""));//34 <E> push35
        avtomatList.add(new Record(35,new int[]{18}, new int[]{36}, new String[]{""}, "", "error: Expecting \")\""));//35 )
        avtomatList.add(new Record(36,new int[]{9}, new int[]{11}, new String[]{"push37"}, "", ""));//36 { <op> push37
        avtomatList.add(new Record(37,new int[]{11}, new int[]{38}, new String[]{""}, "", "error: Operators have to be divided by \"⁋\""));//37 ⁋
        avtomatList.add(new Record(38,new int[]{10, 0}, new int[]{0, 11}, new String[]{"", "push37"}, "exit", ""));//38 }
        //**<E>****//
        avtomatList.add(new Record(39,new int[]{28, 29, 17}, new int[]{40, 40, 39}, new String[]{"", "", "push41"}, "", "error: Expecting idn con or \"(\""));//39 idn con (
        avtomatList.add(new Record(40,new int[]{13, 14, 15, 16}, new int[]{39, 39, 39, 39}, new String[]{"", "", "", ""}, "", "exit"));//40 +-*/ exit
        avtomatList.add(new Record(41,new int[]{18}, new int[]{40}, new String[]{""}, "", "error: Missing closing bracket in expression"));//41 )
        //**<LE>***//
        avtomatList.add(new Record(42,new int[]{32, 0, 36}, new int[]{42, 39, 45}, new String[]{"", "push43",""}, "", ""));//42 not [
        avtomatList.add(new Record(43,new int[]{23, 25, 22, 24, 26, 27}, new int[]{39, 39, 39, 39, 39, 39}, new String[]{"push44", "push44", "push44", "push44", "push44", "push44"}, "", "error: Missing reference sign"));//43 > >= < <= == !=
        avtomatList.add(new Record(44,new int[]{30, 31}, new int[]{42, 42}, new String[]{"", ""}, "", "exit"));//44 and or
        avtomatList.add(new Record(45,new int[]{0}, new int[]{42}, new String[]{"push46"}, "", ""));//45 <LE>
        avtomatList.add(new Record(46,new int[]{37}, new int[]{44}, new String[]{""}, "", "error: Expecting \"]\""));//46 ]
        //**idn = **//
        avtomatList.add(new Record(47,new int[]{19}, new int[]{48}, new String[]{"push53"}, "", "error: Expecting \"=\""));//47 =
        avtomatList.add(new Record(48,new int[]{0,36}, new int[]{39,481}, new String[]{"push49",""}, "", ""));//48 <E> [
        avtomatList.add(new Record(481,new int[]{0}, new int[]{42}, new String[]{"push482"}, "", ""));//481 <LE>
        avtomatList.add(new Record(482,new int[]{37}, new int[]{50}, new String[]{""}, "", ""));//482 ]
        avtomatList.add(new Record(49,new int[]{23, 25, 22, 24, 26, 27}, new int[]{39, 39, 39, 39, 39, 39}, new String[]{"push50", "push50", "push50", "push50", "push50", "push50"}, "", "exit"));//49 exit < <= > >= == !=
        avtomatList.add(new Record(50,new int[]{30, 31, 0}, new int[]{48, 48, 51}, new String[]{"", "", "pop53"}, "", ""));//50 and or
        avtomatList.add(new Record(51,new int[]{33}, new int[]{39}, new String[]{"push52"}, "", "error: Expecting \"?\""));//51 ?
        avtomatList.add(new Record(52,new int[]{34}, new int[]{39}, new String[]{"push53"}, "", "error: Expecting \":\""));//52 :
        avtomatList.add(new Record(53,new int[]{0}, new int[]{0}, new String[]{""}, "", "exit"));//53 exit
    }

    public static void start() throws SyntaxError {
        lexList = LexicalAnalyzer.getTableManager().getLexRecords();
        resetStatic();
        int i = 0;
        LexRecord lex;
        while (i < lexList.size()) {
            lex = lexList.get(i);
            int index = -1;
            int[] buf = null;
            if (getAlpha() != 0) {
                buf = get(getAlpha()).getLabel();
                for (int j = 0; j < buf.length; j++) {
                    if (buf[j] == lex.getKod()) {
                        index = j;
                        break;
                    }
                }
            }
            if (index == -1) {
                if (getAlpha() != 0)
                    for (int j1 = 0; j1 < buf.length; j1++) {
                        if (buf[j1] == 0) {
                            index = j1;
                            break;
                        }
                    }
                if (index == -1) {
                    if (get(getAlpha()) != null && get(getAlpha()).getCompareFailure().startsWith("error")) {
                        throw new SyntaxError("Line "+lex.getLine()+" "+get(getAlpha()).getCompareFailure()+"\nError in state " + (getAlpha()));
                    } else if (getAlpha() == 0 || get(getAlpha()).getCompareFailure().equals("exit")) {
                        if (stack.isEmpty()) {
                            throw new SyntaxError("Stack is empty but he shouldn't be");
                        } else {
                            setAlpha(popStack());
                            i--;
                        }
                    }
                } else {
                    checkStack(get(getAlpha()).getStack()[index]);
                    setAlpha(get(getAlpha()).getBeta()[index]);
                    i--;
                }
            } else {
                if (get(getAlpha()).getCompareSuccess().equals("exit") && stack.isEmpty()) {
                    break;
                }
                checkStack(get(getAlpha()).getStack()[index]);
                setAlpha(get(getAlpha()).getBeta()[index]);
            }
            i++;
        }
    }
}

class Record {
    private int alpha;
    private int[] label;
    private int[] beta;
    private String[] stack;
    private String compareSuccess;
    private String compareFailure;

    public Record(int alpha,int[] label, int[] beta, String[] stack, String compareSuccess, String compareFailure) {
        this.alpha = alpha;
        this.label = label;
        this.beta = beta;
        this.stack = stack;
        this.compareSuccess = compareSuccess;
        this.compareFailure = compareFailure;
    }

    public int[] getLabel() {
        return label;
    }

    public int getAlpha() {
        return alpha;
    }

    public int[] getBeta() {
        return beta;
    }

    public String[] getStack() {
        return stack;
    }

    public String getCompareSuccess() {
        return compareSuccess;
    }

    public String getCompareFailure() {
        return compareFailure;
    }
}
