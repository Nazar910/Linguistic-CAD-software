package com.pyvovar.nazar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyvov on 07.10.2016.
 */
public class TableManager {
    private List<LexRecord> lexTable = new ArrayList<>();
    private List<IdRecord> idTable= new ArrayList<>();
    private List<ConRecord> conTable= new ArrayList<>();

    public TableManager() {
        IdRecord.resetCount();
        ConRecord.resetCount();
        LexRecord.resetCount();
    }

    public void addRecord(LexRecord lexRecord){
        lexTable.add(lexRecord);
    }
    public void addRecord(IdRecord idRecord){
        idTable.add(idRecord);
    }
    public void addRecord(ConRecord conRecord){
        conTable.add(conRecord);
    }

    public List<LexRecord> getLexRecords() {
        return lexTable;
    }

    public List<IdRecord> getIdRecords() {
        return idTable;
    }

    public List<ConRecord> getConRecords() {
        return conTable;
    }
    public int idOfExistingId(String id){
        int i=0;
//        for(i=0;i<com.pyvovar.nazar.IdRecord.getCount();i++){
//            if(idTable.get(i).getId().equals(id)) break;
//        }
        for(IdRecord ir:idTable){
            if(ir.getId().equals(id)) break;
            i++;
        }
        return i+1;
    }
    public int idOfExistingCon(String con){
        int i=0;
//        for(i=0;i<com.pyvovar.nazar.ConRecord.getCount();i++){
//            if(conTable.get(i).getLex().equals(con)) break;
//        }
        for(ConRecord cr:conTable){

            if(cr.getLex().equals(con)) break;
            i++;
        }
        return i+1;
    }
}
