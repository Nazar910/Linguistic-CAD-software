package lang.cad.determination;

import java.util.*;

/**
 * Created by pyvov on 09.12.2016.
 * Lab 4
 */
public class Precedence {
    private HashMap<String, String> grammar;
    private HashMap<String, String> rightGrammar;
    private String[][] matr = new String[59][59];
    private String[][] rightMatr;
    private ArrayList<String> tableColumns;
    private ArrayList<String> rightTableColumns;
    private boolean isRightGrammar = false;

    public Precedence(boolean isRightGrammar) {
        this.isRightGrammar = isRightGrammar;
        grammar = new HashMap<>();
        grammar.put("<програма>", "prog IDN ⁋ var <сп.ог.> { <сп.оп.> }");
        grammar.put("<сп.ог.>", "<тип> <сп.ід.> ⁋|<сп.ог.> <тип> <сп.ід.> ⁋");
        grammar.put("<тип>", "int|real");
        grammar.put("<сп.ід.>", "IDN|<сп.ід.> , IDN");
        grammar.put("<сп.оп.>", "<оп.> ⁋|<сп.оп.> <оп.> ⁋");
        grammar.put("<оп.>", "<вив.>|<введ.>|<присв.>|<цикл>|<ум.пер.>");
        grammar.put("<вив.>", "cout <оп.вив.>|<вив.> <оп.вив.>");
        grammar.put("<оп.вив.>", "<< IDN|<< CON");
        grammar.put("<введ.>", "cin <оп.введ.>|<введ.> <оп.введ.>");
        grammar.put("<оп.введ.>", ">> IDN");
        grammar.put("<присв.>", "IDN = <ЛВ> ? <E> : <E>|IDN = <E>");
        grammar.put("<цикл>", "for ( IDN = <E> ; <ЛВ> ; IDN = <E> ) <оп.>");
        grammar.put("<ум.пер.>", "if ( <відношення> ) { <сп.оп.> }");
        grammar.put("<ЛВ>", "<ЛТ>|<ЛВ> or <ЛТ>");
        grammar.put("<ЛТ>", "<ЛМ>|<ЛТ> and <ЛМ>");
        grammar.put("<ЛМ>", "<відношення>|[ <ЛВ> ]|not <ЛМ>");
        grammar.put("<E>", "<T>|<E> + <T>|<E> - <T>");
        grammar.put("<T>", "<c>|<T> * <c>|<T> / <c>");
        grammar.put("<c>", "IDN|CON|( <E> )");
        grammar.put("<відношення>", "<E> <зн.відн.> <E>");
        grammar.put("<зн.відн.>", ">|>=|<|<=|==|!=");

        rightGrammar = new HashMap<>();
        rightGrammar.put("<програма>", "prog <IDN> ⁋ var <сп.ог1.> { <сп.оп1.> }");
        rightGrammar.put("<IDN>", "IDN");
        rightGrammar.put("<сп.ог.>", "<тип> <сп.ід1.> ⁋|<сп.ог.> <тип> <сп.ід1.> ⁋");
        rightGrammar.put("<сп.ог1.>", "<сп.ог.>");
        rightGrammar.put("<тип>", "int|real");
        rightGrammar.put("<сп.ід.>", "IDN|<сп.ід.> , IDN");
        rightGrammar.put("<сп.ід1.>", "<сп.ід.>");
        rightGrammar.put("<сп.оп.>", "<оп1.> ⁋|<сп.оп.> <оп1.> ⁋");
        rightGrammar.put("<сп.оп1.>", "<сп.оп.>");
        rightGrammar.put("<оп.>", "<вив.>|<введ.>|<присв.>|<цикл>|<ум.пер.>");
        rightGrammar.put("<оп1.>", "<оп.>");
        rightGrammar.put("<вив.>", "cout <оп.вив.>|<вив.> <оп.вив.>");
        rightGrammar.put("<оп.вив.>", "<< IDN|<< CON");
        rightGrammar.put("<введ.>", "cin <оп.введ.>|<введ.> <оп.введ.>");
        rightGrammar.put("<оп.введ.>", ">> IDN");
        rightGrammar.put("<присв.>", "<IDN> = <ЛВ1> ? <E1> : <E1>|<IDN> = <E2>");
        rightGrammar.put("<цикл>", "for ( <IDN> = <E2> ; <ЛВ1> ; <IDN> = <E2> ) <оп.>");
        rightGrammar.put("<ум.пер.>", "if ( <відношення> ) { <сп.оп1.> }");
        rightGrammar.put("<ЛВ>", "<ЛТ1>|<ЛВ> or <ЛТ1>");
        rightGrammar.put("<ЛВ1>", "<ЛВ>");
        rightGrammar.put("<ЛТ>", "<ЛМ>|<ЛТ> and <ЛМ>");
        rightGrammar.put("<ЛТ1>", "<ЛТ>");
        rightGrammar.put("<ЛМ>", "<відношення>|[ <ЛВ1> ]|not <ЛМ>");
        rightGrammar.put("<E1>", "<E>");
        rightGrammar.put("<E2>", "<E1>");
        rightGrammar.put("<E>", "<T1>|<E> + <T1>|<E> - <T1>");
        rightGrammar.put("<T1>", "<T>");
        rightGrammar.put("<T>", "<c>|<T> * <c>|<T> / <c>");
        rightGrammar.put("<c>", "IDN|CON|( <E2> )");
        rightGrammar.put("<відношення>", "<E> <зн.відн.> <E1>");
        rightGrammar.put("<зн.відн.>", ">|>=|<|<=|==|!=");

        tableColumns = new ArrayList<>();
        tableColumns.add("<програма>");
        tableColumns.add("<сп.ог.>");
        tableColumns.add("<тип>");
        tableColumns.add("<сп.ід.>");
        tableColumns.add("<сп.оп.>");
        tableColumns.add("<оп.>");
        tableColumns.add("<вив.>");
        tableColumns.add("<оп.вив.>");
        tableColumns.add("<введ.>");
        tableColumns.add("<оп.введ.>");
        tableColumns.add("<присв.>");
        tableColumns.add("<цикл>");
        tableColumns.add("<ум.пер.>");
        tableColumns.add("<ЛВ>");
        tableColumns.add("<ЛТ>");
        tableColumns.add("<ЛМ>");
        tableColumns.add("<E>");
        tableColumns.add("<T>");
        tableColumns.add("<c>");
        tableColumns.add("<відношення>");
        tableColumns.add("<зн.відн.>");
        tableColumns.add("prog");
        tableColumns.add("var");
        tableColumns.add("int");
        tableColumns.add("real");
        tableColumns.add("cout");
        tableColumns.add("cin");
        tableColumns.add("if");
        tableColumns.add("for");
        tableColumns.add("{");
        tableColumns.add("}");
        tableColumns.add("⁋");
        tableColumns.add(",");
        tableColumns.add("+");
        tableColumns.add("-");
        tableColumns.add("/");
        tableColumns.add("*");
        tableColumns.add("(");
        tableColumns.add(")");
        tableColumns.add("=");
        tableColumns.add("<<");
        tableColumns.add(">>");
        tableColumns.add("<");
        tableColumns.add("<=");
        tableColumns.add(">");
        tableColumns.add(">=");
        tableColumns.add("==");
        tableColumns.add("!=");
        tableColumns.add("IDN");
        tableColumns.add("CON");
        tableColumns.add("and");
        tableColumns.add("or");
        tableColumns.add("not");
        tableColumns.add("?");
        tableColumns.add(":");
        tableColumns.add(";");
        tableColumns.add("[");
        tableColumns.add("]");
        tableColumns.add("#");

        rightTableColumns = new ArrayList<>();
        rightTableColumns.add("<програма>");
        rightTableColumns.add("<сп.ог.>");
        rightTableColumns.add("<сп.ог1.>");
        rightTableColumns.add("<тип>");
        rightTableColumns.add("<сп.ід.>");
        rightTableColumns.add("<сп.ід1.>");
        rightTableColumns.add("<сп.оп.>");
        rightTableColumns.add("<сп.оп1.>");
        rightTableColumns.add("<оп.>");
        rightTableColumns.add("<оп1.>");
        rightTableColumns.add("<вив.>");
        rightTableColumns.add("<оп.вив.>");
        rightTableColumns.add("<введ.>");
        rightTableColumns.add("<оп.введ.>");
        rightTableColumns.add("<присв.>");
        rightTableColumns.add("<цикл>");
        rightTableColumns.add("<ум.пер.>");
        rightTableColumns.add("<ЛВ>");
        rightTableColumns.add("<ЛВ1>");
        rightTableColumns.add("<ЛТ>");
        rightTableColumns.add("<ЛТ1>");
        rightTableColumns.add("<ЛМ>");
        rightTableColumns.add("<E>");
        rightTableColumns.add("<E1>");
        rightTableColumns.add("<E2>");
        rightTableColumns.add("<T>");
        rightTableColumns.add("<T1>");
        rightTableColumns.add("<c>");
        rightTableColumns.add("<відношення>");
        rightTableColumns.add("<зн.відн.>");
        rightTableColumns.add("prog");
        rightTableColumns.add("var");
        rightTableColumns.add("int");
        rightTableColumns.add("real");
        rightTableColumns.add("cout");
        rightTableColumns.add("cin");
        rightTableColumns.add("if");
        rightTableColumns.add("for");
        rightTableColumns.add("{");
        rightTableColumns.add("}");
        rightTableColumns.add("⁋");
        rightTableColumns.add(",");
        rightTableColumns.add("+");
        rightTableColumns.add("-");
        rightTableColumns.add("/");
        rightTableColumns.add("*");
        rightTableColumns.add("(");
        rightTableColumns.add(")");
        rightTableColumns.add("=");
        rightTableColumns.add("<<");
        rightTableColumns.add(">>");
        rightTableColumns.add("<");
        rightTableColumns.add("<=");
        rightTableColumns.add(">");
        rightTableColumns.add(">=");
        rightTableColumns.add("==");
        rightTableColumns.add("!=");
        rightTableColumns.add("IDN");
        rightTableColumns.add("<IDN>");
        rightTableColumns.add("CON");
        rightTableColumns.add("and");
        rightTableColumns.add("or");
        rightTableColumns.add("not");
        rightTableColumns.add("?");
        rightTableColumns.add(":");
        rightTableColumns.add(";");
        rightTableColumns.add("[");
        rightTableColumns.add("]");
        rightTableColumns.add("#");
        rightMatr = new String[rightTableColumns.size()][rightTableColumns.size()];
    }

    public static void main(String[] args) {
        Precedence obj = new Precedence(false);
        System.out.println(obj.FirstPlus("<T>", new HashSet<>()));
        obj.calculate();
    }

    public String[][] calculate() {
        resetMatr();
        setReferences();
        return getMatr();
    }

    private void setEquals() {
        for (Map.Entry<String, String> entry : getGrammar().entrySet()) {
            for (String item : entry.getValue().split("\\|")) {
                String[] array = item.split(" ");
                if (array.length > 1) {
                    for (int i = 1; i < array.length; i++) {
                        getMatr()[getTableColumns().indexOf(array[i - 1])][getTableColumns().indexOf(array[i])] = "=";
                    }
                }
            }
        }
    }

    private void setReferences() {
        setEquals();
        for (int i = 0; i < getMatrLength(); i++) {
            for (int j = 0; j < getMatrLength(); j++) {
                if (getMatr()[i][j].equals("=")) {
                    if (isNotTerminal(getTableColumns().get(j))) {
                        Set<String> res = new HashSet<>();
                        try {
                            res = FirstPlus(getTableColumns().get(j), new HashSet<>());
                        } catch (NullPointerException ex) {
                            System.out.println(getTableColumns().get(i) + "; " + getTableColumns().get(j));
                        }
                        int k = 0;
                        for (String item : res) {
                            k = getTableColumns().indexOf(item);
                            if (!getMatr()[i][k].contains("<")) {
                                getMatr()[i][k] += "<";
                            }
                        }
                    }
                    if (isNotTerminal(getTableColumns().get(i))) {
                        if (isNotTerminal(getTableColumns().get(j))) {
                            Set<String> lastR = new HashSet<>();
                            Set<String> firstV = new HashSet<>();
                            lastR = LastPlus(getTableColumns().get(i), new HashSet<>());
                            firstV = FirstPlus(getTableColumns().get(j), new HashSet<>());
                            int ik = 0, jk = 0;
                            for (String itemR : lastR) {
                                ik = getTableColumns().indexOf(itemR);
                                for (String itemV : firstV) {
                                    jk = getTableColumns().indexOf(itemV);
                                    if (!getMatr()[ik][jk].contains(">")) {
                                        getMatr()[ik][jk] += ">";
                                    }
                                }
                            }
                        } else {
                            Set<String> lastR = LastPlus(getTableColumns().get(i), new HashSet<>());
                            int k = 0;
                            for (String item : lastR) {
                                k = getTableColumns().indexOf(item);
                                if (!getMatr()[k][j].contains(">")) {
                                    getMatr()[k][j] += ">";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<String> Last(String lex) {
        Set<String> last = new HashSet<>();
        if (isNotTerminal(lex)) {
            String[] array = getGrammar().get(lex).split("\\|");
            for (String item : array) {
                last.add(item.split(" ")[item.split(" ").length - 1]);
            }
        }
        return last;
    }

    private Set<String> LastPlus(String lex, Set<String> lastPlusAcc) {
        Set<String> lastPlus = Last(lex);
        if (!lastPlus.isEmpty() && !lastPlusAcc.containsAll(lastPlus)) {
            lastPlusAcc.addAll(lastPlus);
            Set<String> buffer = new HashSet<>();
            for (String item : lastPlus) {
                buffer.addAll(LastPlus(item, lastPlusAcc));
            }
            lastPlus.addAll(buffer);
        }
        return lastPlus;
    }

    private Set<String> First(String lex) {
        Set<String> first = new HashSet<>();
        if (isNotTerminal(lex)) {
            String[] array = getGrammar().get(lex).split("\\|");
            for (String item : array) {
                first.add(item.split(" ")[0]);
            }
        }
        return first;
    }

    private Set<String> FirstPlus(String lex, Set<String> firstPlusAcc) {
        Set<String> firstPlus = First(lex);
        if (!firstPlus.isEmpty() && !firstPlusAcc.containsAll(firstPlus)) {
            firstPlusAcc.addAll(firstPlus);
            Set<String> buffer = new HashSet<>();
            for (String item : firstPlus) {
                buffer.addAll(FirstPlus(item, firstPlusAcc));
            }
            firstPlus.addAll(buffer);
        }
        return firstPlus;
    }

    private boolean isNotTerminal(String lex) {
        return (lex.startsWith("<") && lex.endsWith(">"));
    }

    private HashMap<String, String> getGrammar() {
        return isRightGrammar() ? rightGrammar : grammar;
    }


    public void setGrammar(HashMap<String, String> grammar) {
        this.grammar = grammar;
    }

    public String[][] getMatr() {
        return isRightGrammar() ? rightMatr : matr;
    }

    private void resetMatr() {
        for (int i = 0; i < getMatrLength(); i++) {
            for (int j = 0; j < getMatrLength(); j++) {
                if(i == getMatrLength()-1 && j != getMatrLength()-1){
                    getMatr()[i][j]="<";
                } else if (j==getMatrLength()-1 && i != getMatrLength()-1){
                    getMatr()[i][j]=">";
                } else{
                    getMatr()[i][j] = "";
                }
            }
        }
    }

    public boolean isRightGrammar() {
        return isRightGrammar;
    }

    public ArrayList<String> getTableColumns() {
        return isRightGrammar() ? rightTableColumns : tableColumns;
    }

    public int getMatrLength() {
        return getMatr().length;
    }
}
