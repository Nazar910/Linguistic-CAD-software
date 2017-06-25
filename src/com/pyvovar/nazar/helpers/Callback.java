package com.pyvovar.nazar.helpers;

import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by nazar on 6/25/17.
 */
public interface Callback {
    void cin(String var, HashMap<String, Pair<String, String>> idns);
}
