package com.pyvovar.nazar;

/**
 * Created by pyvov on 07.10.2016.
 */
public class ConRecord {
    private int kod;
    private String lex;
    private static int count;

    public ConRecord(String lex) {
        this.lex = lex;
        this.kod = ++count;
    }

    public static int getCount() {
        return count;
    }

    public static void resetCount(){ count = 0;}

    public int getKod() {
        return kod;
    }

    public String getLex() {
        return lex;
    }
}
