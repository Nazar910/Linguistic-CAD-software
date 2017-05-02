package com.pyvovar.nazar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyvov on 06.10.2016.
 */
public class LexicalAnalyzer {
    private boolean hasToRead = false;
    private char ch = 0;
    private int i = 0;
    private String str = "";
    private String lex = "";
    private int operands = 0;
    private int brackets = -1;
    private int line = 1;
    private int column = 1;
    private boolean or = false;
    private String type = "";
    private TableManager tableManager;


    private List<String> lexDB;
    private FileManager file = new FileManager("./program.txt");

    public LexicalAnalyzer() {
        lexDB = new ArrayList<>();
        lexDB.add("prog");//1
        lexDB.add("var");//2
        lexDB.add("int");//3
        lexDB.add("real");//4
        lexDB.add("cout");//5
        lexDB.add("cin");//6
        lexDB.add("if");//7
        lexDB.add("for");//8
        lexDB.add("{");//9
        lexDB.add("}");//10
        lexDB.add("⁋");//11
        lexDB.add(",");//12
        lexDB.add("+");//13
        lexDB.add("-");//14
        lexDB.add("*");//15
        lexDB.add("/");//16
        lexDB.add("(");//17
        lexDB.add(")");//18
        lexDB.add("=");//19
        lexDB.add("<<");//20
        lexDB.add(">>");//21
        lexDB.add("<");//22
        lexDB.add(">");//23
        lexDB.add("<=");//24
        lexDB.add(">=");//25
        lexDB.add("==");//26
        lexDB.add("!=");//27
        lexDB.add("idn");//28
        lexDB.add("con");//29
        lexDB.add("and");//30
        lexDB.add("or");//31
        lexDB.add("not");//32
        lexDB.add("?");//33
        lexDB.add(":");//34
        lexDB.add(";");//35
        lexDB.add("[");//36
        lexDB.add("]");//37
    }

    public LexicalAnalyzer(String code) {
        this();
        FileManager file = new FileManager("./program.txt");
        file.write(code);
    }

    public FileManager getFile() {
        return file;
    }

    public List<String> getLexDB() {
        return lexDB;
    }

    public int checkLex(String lex) {
        String s;
        for (int i = 0; i < this.lexDB.size(); i++) {
            s = " " + this.lexDB.get(i);
            if (lex.equals(s))
                return i + 1;
        }
        return 0;
    }

    public TableManager getTableManager() {
        return tableManager;
    }

    public void start() throws LexicalError {
        FileManager fileLex = new FileManager("./tableLex.txt");
        FileManager fileId = new FileManager("./tableId.txt");
        FileManager fileCon = new FileManager("./tableCon.txt");
        str = file.read();
        //System.out.println(str);
        if (str != null) hasToRead = true;
        tableManager = new TableManager();
        fState1();
        StringBuilder sb = new StringBuilder();
        List<LexRecord> list = tableManager.getLexRecords();
        for (LexRecord lr : list) {
            // System.out.println("N = "+lr.getId()+"; Line = "+lr.getLine()+"; Lex = "+lr.getLex()+"; Kod = "+lr.getKod()+"; Kod ID/CON = "+lr.getKodIdCon());
            sb.append("N = " + lr.getId() + "; Line = " + lr.getLine() + "; Lex = " + lr.getLex() + "; Kod = " + lr.getKod() + "; Kod ID/CON = " + lr.getKodIdCon() + '\n');

        }
        fileLex.write(sb.toString());
        sb = new StringBuilder();
        List<IdRecord> listId = tableManager.getIdRecords();
        for (IdRecord ir : listId) {
            sb.append("Kod = " + ir.getKod() + "; Id = " + ir.getId() + "; Type = " + ir.getType() + "; Value = " + ir.getValue() + '\n');

        }
        fileId.write(sb.toString());

        sb = new StringBuilder();
        List<ConRecord> listCon = tableManager.getConRecords();
        for (ConRecord cr : listCon) {
            sb.append("Kod = " + cr.getKod() + "; Lex = " + cr.getLex() + '\n');

        }
        fileCon.write(sb.toString());
    }

    private char getChar() {
        if (i < str.length()) {
            column++;
            return str.charAt(i++);
        } else {
            return 0;
        }
    }

    private void addLex(String lex, String clas) throws LexicalError {
        if (lex.charAt(1) == '\n') {
            lex = " ⁋";
            type = "";
        }
        int kod = 0, kodIdCon = 0;
       /* if(lex.charAt(1) != '\t')
            System.out.println("Line = "+line+"; Column = "+((!or)?column-lex.length():column)+"; Lex = "+lex);*/

        if (lex.charAt(1) != '\t') {
            boolean idn = clas.equals("idn");
            int id = checkLex(lex);
            int tId = tableManager.idOfExistingId(lex);
            int idCount = IdRecord.getCount();
            if (clas.equals("idn") && (checkLex(lex) == 0 /*&& type!=""*/ || (!(tableManager.idOfExistingId(lex) - 1 == IdRecord.getCount())))/*!lex.equals(" prog")
                    && !lex.equals(" if")&& !lex.equals(" for")
                    && !lex.equals(" cin")&& !lex.equals(" cout")
                    && !lex.equals(" var")&& !lex.equals(" int")
                    && !lex.equals(" real")*/) {
                kod = 28;
                if (tableManager.idOfExistingId(lex) - 1 == IdRecord.getCount()) {
                    //if(type!="") {
                    tableManager.addRecord(new IdRecord(lex, type, ""));
                    kodIdCon = IdRecord.getCount();
//                    }
//                    else {
//                        error("0");
//                    }
                } else {
                    // String idType=tableManager.getIdRecords().get(tableManager.idOfExistingCon(lex)).getType();
                    if (type == "")//!idType.equals(type))
                        kodIdCon = tableManager.idOfExistingId(lex);
                    else error("0");
                }
            } else if (clas.equals("con")) {
                kod = 29;
                if (tableManager.idOfExistingCon(lex) - 1 == ConRecord.getCount()) {
                    tableManager.addRecord(new ConRecord(lex));
                    kodIdCon = ConRecord.getCount();
                } else
                    kodIdCon = ConRecord.getCount();

            } else kod = checkLex(lex);
            if (lex.equals(" int") || lex.equals(" real") || lex.equals(" prog"))
                type = lex;
            if (kod == 0) {
                System.out.println(lex);
                error("0");
                return;
            }
            tableManager.addRecord(new LexRecord(line, lex, kod, kodIdCon));

        }
        //hasToRead = false;
        fState1();
    }

    private void fState1() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        } else hasToRead = true;
        or = false;
        if (ch == ' ') {
            fState1();
        } else if (ch >= 'A' && ch <= 'z' && (ch < '[' || ch > '`')) {
            lex = " " + ch;
            fState2();
        } else if (ch >= '1' && ch <= '9') {
            lex = " " + ch;
            fState4();
        } else if (ch == '0') {
            lex = " " + ch;
            fState4_0();
        } else if (ch == '.') {
            lex = " " + ch;
            fState6();
        } else if (ch == '<') {
            lex = " " + ch;
            fState10();
        } else if (ch == '>') {
            lex = " " + ch;
            fState11();
        } else if (ch == '=') {
            lex = " " + ch;
            //hasToRead=false;
            fState12();
        } else if (ch == '!') {
            lex = " " + ch;
            fState13();
        } else if (ch == '\n' || ch == '\t' || ch == ',' || ch == '{' || ch == '}' || ch == '(' ||
                ch == ')' || ch == '*' || ch == '/' || ch == '?' || ch == ':' || ch == ';' ||
                ch == '+' || ch == '-' || ch == '[' || ch == ']') {
            if (ch == '\n') {
                line++;
                column = 1;
            }
            lex = " " + ch;
            hasToRead = true;
            or = true;
            operands = 0;
            if (ch != '\n' || ch != '\t')
                addLex(lex, "" + ch);
            else
                addLex(lex, "⁋");
        } else if (ch == '%') {
            while (getChar() != '\n') ;
            ch = '\n';
            fState1();
        } else if (ch == 0)
            hasToRead = false;
        else {
            error("1");
        }
    }

    private void error(String state) throws LexicalError {
        //System.out.println("Error in stage number "+state);

        LexicalError lexicalError = new LexicalError("Лексична помилка! рядок = " + line/*+"; стовпчик = "+column*/, line, column, Integer.parseInt(state));
        throw lexicalError;

        // System.exit(1);
    }

    private  void fState2() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        }
        if (ch >= 'A' && ch <= 'z'  && (ch < '[' || ch > '`') || ch == '_') {
            lex += ch;
            fState2();
        } else if (ch >= '0' && ch <= '9') {
            lex += ch;
            fState2();
        } else if (ch == 0)
            hasToRead = false;
        else {
            hasToRead = false;
            operands = 1;
            addLex(lex, "idn");   //lexClass = IDN / TRN
        }
    }


    private  void fState3() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        } else hasToRead = true;
        if (ch == '.') {
            lex += ch;
            fState5();
        } else if (ch >= '1' && ch <= '9') {
            lex += ch;
            //hasToRead=false;
            fState4();
        } else if (ch == 0)
            hasToRead = false;
        else {
            if (ch == ')')
                hasToRead = false;
            // operands=1;
            addLex(lex, "" + ch); // LexClass = +/-
        }
    }

    private  void fState4() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        } else hasToRead = true;
        if (ch == '.') {
            lex += ch;
            fState5();
        } else if (ch >= '0' && ch <= '9') {
            lex += ch;
            fState4();
        } else if (ch >= 'A' && ch <= 'z'  && (ch < '[' || ch > '`')) {
            error("4");
        } else if (ch == 0)
            hasToRead = false;
        else {
            hasToRead = false;
            addLex(lex, "con");//lexClass = CON
        }
    }

    private  void fState4_0() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        } else hasToRead = true;
        if (ch == '.') {
            lex += ch;
            fState5();
        } else if (ch >= '0' && ch <= '9') {
            error("40");
        } else if (ch >= 'A' && ch <= 'z' && (ch < '[' || ch > '`')) {
            error("40");
        } else if (ch == 0)
            hasToRead = false;
        else {
            hasToRead = false;
            addLex(lex, "con");//lexClass = CON
        }
    }


    private  void fState5() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        }
        if (ch >= '0' && ch <= '9') {
            lex += ch;
            fState5();
        } else if (ch == 0)
            hasToRead = false;
        else {
//            if(!lex.endsWith(".")){
            //if(ch == ')')
            hasToRead = false;
            addLex(lex, "con");//lexClass = CON
//            }
//            else
//                error("5");
        }
    }

    private  void fState6() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        }
        if (ch >= '0' && ch <= '9') {
            lex += ch;
            fState5();
        } else if (ch == 0)
            hasToRead = false;
        else {
            error("6");
        }
    }

    private  void fState10() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        }
        if (ch == '<') {
            lex += ch;
            addLex(lex, "<<");  // lex = <<
        } else if (ch == '=') {
            lex += ch;
            addLex(lex, "=<");   // lex = <=
        } else if (ch == 0)
            hasToRead = false;
        else {
            hasToRead = false;
            addLex(lex, "<");  // lex = <
        }
    }

    private  void fState11() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        }

        if (ch == '>') {
            lex += ch;
            addLex(lex, ">>");   // lex = >>
        } else if (ch == '=') {
            lex += ch;
            addLex(lex, ">=");    // lex = >=
        } else if (ch == 0)
            hasToRead = false;
        else {
            hasToRead = false;
            addLex(lex, ">");    // lex = >
        }
    }

    private  void fState12() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        } else hasToRead = true;

        if (ch == '=') {
            lex += ch;
            addLex(lex, "==");     // lex = ==
        } else if (ch == 0)
            hasToRead = false;
        else {
            hasToRead = false;
            addLex(lex, "=");    // lex = =
        }
    }

    private  void fState13() throws LexicalError {
        if (hasToRead) {
            ch = getChar();
        }

        if (ch == '=') {
            lex += ch;
            addLex(lex, "!=");    // lex = !=
        } else if (ch == 0)
            hasToRead = false;
        else {
            error("13");
        }
    }
}
