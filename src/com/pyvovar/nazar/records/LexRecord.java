package com.pyvovar.nazar.records;

/**
 * Created by pyvov on 07.10.2016.
 */
public class LexRecord {
    private int id;
    private int line;
    private String lex;
    private int kod;
    private int kodIdCon;
    private static int count;

    public LexRecord(int line, String lex, int kod, int kodIdCon) {
        this.line = line;
        this.lex = lex;
        this.kod = kod;
        this.kodIdCon = kodIdCon;
        this.id = ++count;
    }

    public int getId() {
        return id;
    }

    public int getLine() {
        return line;
    }

    public String getLex() {
        return lex;
    }

    public int getKod() {
        return kod;
    }

    public int getKodIdCon() {
        return kodIdCon;
    }

    public static int getCount() {
        return count;
    }
    public static void resetCount() { count = 0; }
}
