import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        avtomatList.add(new Record(new int[]{1}, new int[]{2}, new String[]{""}, "", "error: Program have to start with prog"));//1 prog
        avtomatList.add(new Record(new int[]{28}, new int[]{3}, new String[]{""}, "", "error: After \"prog\" must be idn"));//2 idn
        avtomatList.add(new Record(new int[]{11}, new int[]{4}, new String[]{""}, "", "error: \"⁋\" expected"));//3 ⁋
        avtomatList.add(new Record(new int[]{2}, new int[]{5}, new String[]{""}, "", "error: Expected var"));//4 var
        avtomatList.add(new Record(new int[]{3, 4}, new int[]{6, 6}, new String[]{"", ""}, "", "error: Type have to be int or real"));//5 int real
        avtomatList.add(new Record(new int[]{28}, new int[]{7}, new String[]{""}, "", "error: Expected idn"));//6 idn
        avtomatList.add(new Record(new int[]{12, 11}, new int[]{6, 8}, new String[]{"", ""}, "", "error: \"⁋\" expected"));//7 , ⁋
        avtomatList.add(new Record(new int[]{3, 4, 9}, new int[]{6, 6, 11}, new String[]{"", "", "push9"}, "", ""));//8 int real <op>
        avtomatList.add(new Record(new int[]{11}, new int[]{10}, new String[]{""}, "", "error: Operators have to be divided by \"⁋\""));//9 ⁋
        avtomatList.add(new Record(new int[]{10, 0}, new int[]{0, 11}, new String[]{"", "push9"}, "exit", ""));//10 }
        avtomatList.add(new Record(new int[]{5, 6, 28, 8, 7, 17}, new int[]{12, 15, 45, 18, 31, 49}, new String[]{"", "", "push51", "", "", ""}, "", "error: Operator have to be cout,cin,for,if,assignment or ternary operator"));//11 cout cin idn for if (
        avtomatList.add(new Record(new int[]{20}, new int[]{13}, new String[]{""}, "", "error: Operands of cout have to be divided by \"<<\""));//12 <<
        avtomatList.add(new Record(new int[]{28, 29}, new int[]{14, 14}, new String[]{"", ""}, "", "error: Operands of cout must be idn or con"));//13 idn con
        avtomatList.add(new Record(new int[]{20}, new int[]{13}, new String[]{""}, "", "exit"));//14 <<
        avtomatList.add(new Record(new int[]{21}, new int[]{16}, new String[]{""}, "", "error: Operators of cin have to be divided by \">>\""));//15 >>
        avtomatList.add(new Record(new int[]{28}, new int[]{17}, new String[]{""}, "", "error: Expecting idn in cin"));//16 idn
        avtomatList.add(new Record(new int[]{21}, new int[]{16}, new String[]{""}, "", "exit"));//17 >>
        avtomatList.add(new Record(new int[]{17}, new int[]{19}, new String[]{""}, "", "error: Expecting \"(\" after \"for\""));//18 (
        avtomatList.add(new Record(new int[]{28}, new int[]{20}, new String[]{""}, "", "error: Expecting idn"));//19 idn
        avtomatList.add(new Record(new int[]{19}, new int[]{21}, new String[]{""}, "", "error: Expecting \"=\""));//20 =
        avtomatList.add(new Record(new int[]{0}, new int[]{39}, new String[]{"push22"}, "", ""));//21 <E> push22
        avtomatList.add(new Record(new int[]{35}, new int[]{23}, new String[]{""}, "", "error: Expecting \";\""));//22 ;
        avtomatList.add(new Record(new int[]{0}, new int[]{42}, new String[]{"push24"}, "", ""));//23 <LE> push24
        avtomatList.add(new Record(new int[]{35}, new int[]{25}, new String[]{""}, "", "error: Expecting \";\""));//24 ;
        avtomatList.add(new Record(new int[]{28}, new int[]{26}, new String[]{""}, "", "error: Expecting idn"));//25 idn
        avtomatList.add(new Record(new int[]{19}, new int[]{27}, new String[]{""}, "", "error: Expecting \"=\""));//26 =
        avtomatList.add(new Record(new int[]{0}, new int[]{39}, new String[]{"push28"}, "", ""));//27 <E> push28
        avtomatList.add(new Record(new int[]{18}, new int[]{29}, new String[]{""}, "", "error: Expecting \")\""));//28 )
        avtomatList.add(new Record(new int[]{0}, new int[]{11}, new String[]{"push30"}, "", ""));//29 <op> push30
        avtomatList.add(new Record(new int[]{0}, new int[]{0}, new String[]{""}, "", "exit"));//30
        avtomatList.add(new Record(new int[]{17}, new int[]{32}, new String[]{""}, "", "error: Expecting \"(\" after \"if\""));//31 (
        avtomatList.add(new Record(new int[]{0}, new int[]{39}, new String[]{"push33"}, "", ""));//32 <E> push33
        avtomatList.add(new Record(new int[]{23, 25, 22, 24, 26, 27}, new int[]{34, 34, 34, 34, 34, 34}, new String[]{"", "", "", "", "", ""}, "", "error: Expecting reference sign"));//33 > >= < <= == !=
        avtomatList.add(new Record(new int[]{0}, new int[]{39}, new String[]{"push35"}, "", ""));//34 <E> push35
        avtomatList.add(new Record(new int[]{18}, new int[]{36}, new String[]{""}, "", "error: Expecting \")\""));//35 )
        avtomatList.add(new Record(new int[]{9}, new int[]{11}, new String[]{"push37"}, "", ""));//36 { <op> push37
        avtomatList.add(new Record(new int[]{11}, new int[]{38}, new String[]{""}, "", "error: Operators have to be divided by \"⁋\""));//37 ⁋
        avtomatList.add(new Record(new int[]{10, 0}, new int[]{0, 11}, new String[]{"", "push37"}, "exit", ""));//38 }
        //**<E>****//
        avtomatList.add(new Record(new int[]{28, 29, 17}, new int[]{40, 40, 39}, new String[]{"", "", "push41"}, "", "error: Expecting idn con or \"(\""));//39 idn con (
        avtomatList.add(new Record(new int[]{13, 14, 15, 16}, new int[]{39, 39, 39, 39}, new String[]{"", "", "", ""}, "", "exit"));//40 +-*/ exit
        avtomatList.add(new Record(new int[]{18}, new int[]{40}, new String[]{""}, "", "error: Missing closing bracket in expression"));//41 )
        //**<LE>***//
        avtomatList.add(new Record(new int[]{32, 0}, new int[]{42, 39}, new String[]{"", "push43"}, "", ""));//42 not
        avtomatList.add(new Record(new int[]{23, 25, 22, 24, 26, 27}, new int[]{39, 39, 39, 39, 39, 39}, new String[]{"push44", "push44", "push44", "push44", "push44", "push44"}, "", "error: Missing reference sign"));//43 > >= < <= == !=
        avtomatList.add(new Record(new int[]{30, 31}, new int[]{42, 42}, new String[]{"", ""}, "", "exit"));//44 and or
        //**idn = **//
        avtomatList.add(new Record(new int[]{19}, new int[]{46}, new String[]{""}, "", "error: Expecting \"=\""));//45 =
        avtomatList.add(new Record(new int[]{0}, new int[]{39}, new String[]{"push47"}, "", ""));//46 <E>
        avtomatList.add(new Record(new int[]{23, 25, 22, 24, 26, 27}, new int[]{39, 39, 39, 39, 39, 39}, new String[]{"push48", "push48", "push48", "push48", "push48", "push48"}, "", "exit"));//47 exit < <= > >= == !=
        avtomatList.add(new Record(new int[]{30, 31, 0}, new int[]{46, 46, 49}, new String[]{"", "", "pop51"}, "", "exit"));//48 and or
        avtomatList.add(new Record(new int[]{33}, new int[]{39}, new String[]{"push50"}, "", "error: Expecting \"?\""));//49 ?
        avtomatList.add(new Record(new int[]{34}, new int[]{39}, new String[]{"push51"}, "", "error: Expecting \":\""));//50 :
        avtomatList.add(new Record(new int[]{0}, new int[]{0}, new String[]{""}, "", "exit"));//51 exit
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
                buf = avtomatList.get(getAlpha()).getLabel();
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
                    if (avtomatList.get(getAlpha()) != null && avtomatList.get(getAlpha()).getCompareFailure().startsWith("error")) {
                        throw new SyntaxError("Line "+lex.getLine()+" "+avtomatList.get(getAlpha()).getCompareFailure()+"\nError in state " + (getAlpha()));
                    } else if (getAlpha() == 0 || avtomatList.get(getAlpha()).getCompareFailure().equals("exit")) {
                        if (stack.isEmpty()) {
                            throw new SyntaxError("Stack is empty but he shouldn't be");
                        } else {
                            setAlpha(popStack());
                            i--;
                        }
                    }
                } else {
                    checkStack(avtomatList.get(getAlpha()).getStack()[index]);
                    setAlpha(avtomatList.get(getAlpha()).getBeta()[index]);
                    i--;
                }
            } else {
                if (avtomatList.get(getAlpha()).getCompareSuccess().equals("exit") && stack.isEmpty()) {
                    System.out.println("Successfully");
                    break;
                }
                checkStack(avtomatList.get(getAlpha()).getStack()[index]);
                setAlpha(avtomatList.get(getAlpha()).getBeta()[index]);
            }
            i++;
        }
    }
}

class Record {
    private int[] label;
    private int[] beta;
    private String[] stack;
    private String compareSuccess;
    private String compareFailure;

    public Record(int[] label, int[] beta, String[] stack, String compareSuccess, String compareFailure) {
        this.label = label;
        this.beta = beta;
        this.stack = stack;
        this.compareSuccess = compareSuccess;
        this.compareFailure = compareFailure;
    }

    public int[] getLabel() {
        return label;
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
