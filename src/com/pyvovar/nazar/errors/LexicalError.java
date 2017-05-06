package com.pyvovar.nazar.errors;

/**
 * Created by pyvov on 08.10.2016.
 */
public class LexicalError extends Exception {
    private int line;
    private int column;
    private int state;

    public LexicalError(String message, int line, int column, int state) {
        super(message);
        this.line = line;
        this.column = column;
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
