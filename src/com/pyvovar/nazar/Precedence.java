package com.pyvovar.nazar;

import java.util.*;

/**
 * Created by pyvov on 09.12.2016.
 * Lab 4
 */
public class Precedence {
    private HashMap<String, String> grammar;
    private HashMap<String, String> wrightGrammar;
    private String[][] matr = new String[59][59];
    private String[][] rightMatr;
    private ArrayList<String> tableColumns;
    private ArrayList<String> wrightTableColumns;
    private boolean iswrightGrammar = false;

    public Precedence(boolean iswrightGrammar) {
        this.iswrightGrammar = iswrightGrammar;
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

        wrightGrammar = new HashMap<>();
        wrightGrammar.put("<програма>", "prog <IDN> ⁋ var <сп.ог1.> { <сп.оп1.> }");
        wrightGrammar.put("<IDN>", "IDN");
        wrightGrammar.put("<сп.ог.>", "<тип> <сп.ід1.> ⁋|<сп.ог.> <тип> <сп.ід1.> ⁋");
        wrightGrammar.put("<сп.ог1.>", "<сп.ог.>");
        wrightGrammar.put("<тип>", "int|real");
        wrightGrammar.put("<сп.ід.>", "IDN|<сп.ід.> , IDN");
        wrightGrammar.put("<сп.ід1.>", "<сп.ід.>");
        wrightGrammar.put("<сп.оп.>", "<оп1.> ⁋|<сп.оп.> <оп1.> ⁋");
        wrightGrammar.put("<сп.оп1.>", "<сп.оп.>");
        wrightGrammar.put("<оп.>", "<вив.>|<введ.>|<присв.>|<цикл>|<ум.пер.>");
        wrightGrammar.put("<оп1.>", "<оп.>");
        wrightGrammar.put("<вив.>", "cout <оп.вив.>|<вив.> <оп.вив.>");
        wrightGrammar.put("<оп.вив.>", "<< IDN|<< CON");
        wrightGrammar.put("<введ.>", "cin <оп.введ.>|<введ.> <оп.введ.>");
        wrightGrammar.put("<оп.введ.>", ">> IDN");
        wrightGrammar.put("<присв.>", "<IDN> = <ЛВ1> ? <E1> : <E1>|<IDN> = <E2>");
        wrightGrammar.put("<цикл>", "for ( <IDN> = <E2> ; <ЛВ1> ; <IDN> = <E2> ) <оп.>");
        wrightGrammar.put("<ум.пер.>", "if ( <відношення> ) { <сп.оп1.> }");
        wrightGrammar.put("<ЛВ>", "<ЛТ1>|<ЛВ> or <ЛТ1>");
        wrightGrammar.put("<ЛВ1>", "<ЛВ>");
        wrightGrammar.put("<ЛТ>", "<ЛМ>|<ЛТ> and <ЛМ>");
        wrightGrammar.put("<ЛТ1>", "<ЛТ>");
        wrightGrammar.put("<ЛМ>", "<відношення>|[ <ЛВ1> ]|not <ЛМ>");
        wrightGrammar.put("<E1>", "<E>");
        wrightGrammar.put("<E2>", "<E1>");
        wrightGrammar.put("<E>", "<T1>|<E> + <T1>|<E> - <T1>");
        wrightGrammar.put("<T1>", "<T>");
        wrightGrammar.put("<T>", "<c>|<T> * <c>|<T> / <c>");
        wrightGrammar.put("<c>", "IDN|CON|( <E2> )");
        wrightGrammar.put("<відношення>", "<E> <зн.відн.> <E1>");
        wrightGrammar.put("<зн.відн.>", ">|>=|<|<=|==|!=");

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

        wrightTableColumns = new ArrayList<>();
        wrightTableColumns.add("<програма>");
        wrightTableColumns.add("<сп.ог.>");
        wrightTableColumns.add("<сп.ог1.>");
        wrightTableColumns.add("<тип>");
        wrightTableColumns.add("<сп.ід.>");
        wrightTableColumns.add("<сп.ід1.>");
        wrightTableColumns.add("<сп.оп.>");
        wrightTableColumns.add("<сп.оп1.>");
        wrightTableColumns.add("<оп.>");
        wrightTableColumns.add("<оп1.>");
        wrightTableColumns.add("<вив.>");
        wrightTableColumns.add("<оп.вив.>");
        wrightTableColumns.add("<введ.>");
        wrightTableColumns.add("<оп.введ.>");
        wrightTableColumns.add("<присв.>");
        wrightTableColumns.add("<цикл>");
        wrightTableColumns.add("<ум.пер.>");
        wrightTableColumns.add("<ЛВ>");
        wrightTableColumns.add("<ЛВ1>");
        wrightTableColumns.add("<ЛТ>");
        wrightTableColumns.add("<ЛТ1>");
        wrightTableColumns.add("<ЛМ>");
        wrightTableColumns.add("<E>");
        wrightTableColumns.add("<E1>");
        wrightTableColumns.add("<E2>");
        wrightTableColumns.add("<T>");
        wrightTableColumns.add("<T1>");
        wrightTableColumns.add("<c>");
        wrightTableColumns.add("<відношення>");
        wrightTableColumns.add("<зн.відн.>");
        wrightTableColumns.add("prog");
        wrightTableColumns.add("var");
        wrightTableColumns.add("int");
        wrightTableColumns.add("real");
        wrightTableColumns.add("cout");
        wrightTableColumns.add("cin");
        wrightTableColumns.add("if");
        wrightTableColumns.add("for");
        wrightTableColumns.add("{");
        wrightTableColumns.add("}");
        wrightTableColumns.add("⁋");
        wrightTableColumns.add(",");
        wrightTableColumns.add("+");
        wrightTableColumns.add("-");
        wrightTableColumns.add("/");
        wrightTableColumns.add("*");
        wrightTableColumns.add("(");
        wrightTableColumns.add(")");
        wrightTableColumns.add("=");
        wrightTableColumns.add("<<");
        wrightTableColumns.add(">>");
        wrightTableColumns.add("<");
        wrightTableColumns.add("<=");
        wrightTableColumns.add(">");
        wrightTableColumns.add(">=");
        wrightTableColumns.add("==");
        wrightTableColumns.add("!=");
        wrightTableColumns.add("IDN");
        wrightTableColumns.add("<IDN>");
        wrightTableColumns.add("CON");
        wrightTableColumns.add("and");
        wrightTableColumns.add("or");
        wrightTableColumns.add("not");
        wrightTableColumns.add("?");
        wrightTableColumns.add(":");
        wrightTableColumns.add(";");
        wrightTableColumns.add("[");
        wrightTableColumns.add("]");
        wrightTableColumns.add("#");
        rightMatr = new String[wrightTableColumns.size()][wrightTableColumns.size()];
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
                        //3.1
                        Set<String> lastR = LastPlus(getTableColumns().get(i), new HashSet<>());
                        int k = 0;
                        for (String item : lastR) {
                            k = getTableColumns().indexOf(item);
                            if (!getMatr()[k][j].contains(">")) {
                                getMatr()[k][j] += ">";
                            }
                        }
                        //3.2
                        if (isNotTerminal(getTableColumns().get(j))) {
                            lastR = new HashSet<>();
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

    public HashMap<String, String> getGrammar() {
        return iswrightGrammar() ? wrightGrammar : grammar;
    }


    public void setGrammar(HashMap<String, String> grammar) {
        this.grammar = grammar;
    }

    public String[][] getMatr() {
        return iswrightGrammar() ? rightMatr : matr;
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

    public boolean iswrightGrammar() {
        return iswrightGrammar;
    }

    public ArrayList<String> getTableColumns() {
        return iswrightGrammar() ? wrightTableColumns : tableColumns;
    }

    public int getMatrLength() {
        return getMatr().length;
    }
}
