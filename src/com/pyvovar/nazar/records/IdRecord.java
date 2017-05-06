package com.pyvovar.nazar.records;

/**
 * Created by pyvov on 07.10.2016.
 */
public class IdRecord {
    private int kod;
    private String id;
    private String type;
    private String value;
    private static int count;

    public IdRecord(String id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.kod = ++count;
    }

    public int getKod() {
        return kod;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static int getCount() {
        return count;

    }
    public static void resetCount(){
        count = 0;
    }

}
