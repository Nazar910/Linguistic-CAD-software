import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyvov on 06.10.2016.
 */
public class LexicalAnalyzer{
    private static boolean hasToRead = false;
    private static char ch;
    private static int i;
    private static String str;
    private static String lex="";
    private static int operands;
    private static int brackets=-1;
    private static int line=1;
    private static int column=1;
    private static boolean or;
    private static String type="";
    private static TableManager tableManager;
    private static List<String> lexDB=new ArrayList<>();
    private static FileManager file= new FileManager("E:/OneDrive/program.txt");

    public static FileManager getFile() {
        return file;
    }

    public static void initLexDB(){

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
    }
    public static int checkLex(String lex){
        String s;
        for(int i=0;i<lexDB.size();i++){
            s = " "+lexDB.get(i);
            if(lex.equals(s))
                return i+1;
        }
        return 0;
    }
    private static void def(){
        hasToRead=false;
        ch=0;
        i=0;
        str="";
        lex="";
        operands=0;
        line=1;
        column=1;
        or =false;
        type="";
        file= new FileManager("E:/OneDrive/program.txt");
        tableManager = new TableManager();
        lexDB = new ArrayList<>();
    }
    public static TableManager getTableManager(){
        return tableManager;
    }
    public static void start/*main*/(/*String[] args*/) throws LexicalError{
        def();
        FileManager fileLex = new FileManager("E:/OneDrive/tableLex.txt");
        FileManager fileId = new FileManager("E:/OneDrive/tableId.txt");
        FileManager fileCon = new FileManager("E:/OneDrive/tableCon.txt");
        str = file.read();
        //System.out.println(str);
        if(str!=null) hasToRead=true;
        initLexDB();
        tableManager = new TableManager();
        fState1();
        StringBuilder sb=new StringBuilder();
        List<LexRecord> list = tableManager.getLexRecords();
        for (LexRecord lr : list){
           // System.out.println("N = "+lr.getId()+"; Line = "+lr.getLine()+"; Lex = "+lr.getLex()+"; Kod = "+lr.getKod()+"; Kod ID/CON = "+lr.getKodIdCon());
            sb.append("N = "+lr.getId()+"; Line = "+lr.getLine()+"; Lex = "+lr.getLex()+"; Kod = "+lr.getKod()+"; Kod ID/CON = "+lr.getKodIdCon()+'\n');

        }
        fileLex.write(sb.toString());
        sb= new StringBuilder();
        List<IdRecord> listId = tableManager.getIdRecords();
        for (IdRecord ir : listId){
            sb.append("Kod = "+ir.getKod()+"; Id = "+ir.getId()+"; Type = "+ir.getType()+"; Value = "+ir.getValue()+'\n');

        }
        fileId.write(sb.toString());

        sb= new StringBuilder();
        List<ConRecord> listCon = tableManager.getConRecords();
        for (ConRecord cr : listCon){
            sb.append("Kod = "+cr.getKod()+"; Lex = "+cr.getLex()+'\n');

        }
        fileCon.write(sb.toString());
    }
    private static char getChar(){
        if(i<str.length()){
            column++;
            return str.charAt(i++);
        }
        else{
            return 0;
        }
    }
    private static void addLex(String lex, String clas){
        if(lex.charAt(1) == '\n') {
            lex = " ⁋";
            type="";
        }
        int kod=0,kodIdCon=0;
       /* if(lex.charAt(1) != '\t')
            System.out.println("Line = "+line+"; Column = "+((!or)?column-lex.length():column)+"; Lex = "+lex);*/

        if(lex.charAt(1) != '\t') {
            boolean idn = clas.equals("idn");
            int id = checkLex(lex);
            int tId=tableManager.idOfExistingId(lex);
            int idCount = IdRecord.getCount();
            if(clas.equals("idn") && (checkLex(lex)==0 /*&& type!=""*/ || (!(tableManager.idOfExistingId(lex)-1==IdRecord.getCount())))/*!lex.equals(" prog")
                    && !lex.equals(" if")&& !lex.equals(" for")
                    && !lex.equals(" cin")&& !lex.equals(" cout")
                    && !lex.equals(" var")&& !lex.equals(" int")
                    && !lex.equals(" real")*/ ){
                kod=28;
                if(tableManager.idOfExistingId(lex)-1==IdRecord.getCount()) {
                    //if(type!="") {
                        tableManager.addRecord(new IdRecord(lex, type, ""));
                        kodIdCon = IdRecord.getCount();
//                    }
//                    else {
//                        error("0");
//                    }
                }
                else {
                   // String idType=tableManager.getIdRecords().get(tableManager.idOfExistingCon(lex)).getType();
                    if(type=="")//!idType.equals(type))
                        kodIdCon = tableManager.idOfExistingId(lex);
                    else error("0");
                }
            }
            else if(clas.equals("con")){
                kod=29;
                if(tableManager.idOfExistingCon(lex)-1 == ConRecord.getCount()) {
                    tableManager.addRecord(new ConRecord(lex));
                    kodIdCon = ConRecord.getCount();
                }
                else
                    kodIdCon=ConRecord.getCount();

            }
            else kod = checkLex(lex);
            if(lex.equals(" int") || lex.equals(" real") || lex.equals(" prog"))
                type=lex;
            if(kod == 0){
                error("0");
                return;
            }
            tableManager.addRecord(new LexRecord(line, lex, kod, kodIdCon));

        }
        //hasToRead = false;
        fState1();
    }

    private static void fState1(){
        if(hasToRead){
            ch = getChar();
        }
        else hasToRead=true;
        or = false;
        if(ch==' '){
            fState1();
        }
        else if(ch >= 'A' && ch <= 'z'){
            lex = " "+ch;
            fState2();
        }
//        else if(ch == '+' || ch == '-'){
//            if(lex.equals(" )")) hasToRead=false;
//            lex= " "+ch;
//            if(operands!=0)
//                hasToRead=false;
//            fState3();
//        }
        else if(ch >= '1' && ch <= '9'){
            lex=" "+ch;
            fState4();
        }
        else if(ch == '0'){
            lex=" "+ch;
            fState4_0();
        }
        else if(ch == '.'){
            lex=" "+ch;
            fState6();
        }
        else if(ch == '<'){
            lex=" "+ch;
            fState10();
        }
        else if(ch == '>'){
            lex=" "+ch;
            fState11();
        }
        else if(ch == '='){
            lex=" "+ch;
            //hasToRead=false;
            fState12();
        }
        else if(ch == '!'){
            lex = " "+ch;
            fState13();
        }
        else if(ch == '\n' || ch == '\t' || ch == ',' || ch == '{' || ch == '}' || ch == '(' ||
                ch == ')' || ch == '*' || ch =='/' || ch == '?' || ch == ':' || ch == ';' ||
                ch == '+' || ch == '-'){
            if(ch == '\n'){
                line++;
                column=1;
            }
//            if(ch == '(')
//                brackets++;
//            else if(ch == ')')
//                brackets--;
//            if(ch == '?' && lex.equals(" )")){
//                lex=" "+ch;
//                addLex(lex,""+ch);
//            }

            lex=" "+ch;
            hasToRead=true;
            or=true;
            operands=0;
            if(ch != '\n' || ch !='\t')
                addLex(lex, ""+ch);
            else
                addLex(lex, "⁋");
        }
        else if(ch == '%'){
            while (getChar()!='\n');
            ch = '\n';
            fState1();
        }
        else if(ch==0)
            hasToRead=false;
        else{
            error("1");
        }
    }

    private static void error(String state){
        //System.out.println("Error in stage number "+state);

            LexicalError lexicalError = new LexicalError("Лексична помилка! рядок = "+line/*+"; стовпчик = "+column*/, line,column,Integer.parseInt(state));
            App.setError(lexicalError);

        // System.exit(1);
    }

    private static void fState2() {
        if(hasToRead){
            ch = getChar();
        }
        if(ch >= 'A' && ch <= 'z' || ch == '_'){
            lex +=ch;
            fState2();
        }
        else if(ch >= '0' && ch <= '9'){
            lex +=ch;
            fState2();
        }
        else if(ch==0)
            hasToRead=false;
        else{
            hasToRead=false;
            operands=1;
            addLex(lex,"idn");   //lexClass = IDN / TRN
        }
    }



    private static void fState3() {
        if(hasToRead){
            ch = getChar();
        } else hasToRead=true;
        if(ch == '.'){
            lex+=ch;
            fState5();
        }
        else if(ch >= '1' && ch <= '9'){
            lex+=ch;
            //hasToRead=false;
            fState4();
        }
        else if(ch==0)
            hasToRead=false;
        else{
            if(ch == ')')
                hasToRead=false;
           // operands=1;
            addLex(lex, ""+ch); // LexClass = +/-
        }
    }

    private static void fState4() {
        if(hasToRead){
            ch = getChar();
        }
        else hasToRead=true;
        if(ch == '.'){
            lex+=ch;
            fState5();
        }
        else if(ch >= '0' && ch <= '9'){
            lex+=ch;
            fState4();
        }
        else if(ch >= 'A' && ch <= 'z'){
            error("4");
        }
        else if(ch==0)
            hasToRead=false;
        else{
            hasToRead=false;
            addLex(lex,"con");//lexClass = CON
        }
    }
    private static void fState4_0() {
        if(hasToRead){
            ch = getChar();
        }
        else hasToRead=true;
        if(ch == '.'){
            lex+=ch;
            fState5();
        }
        else if(ch >= '0' && ch <= '9'){
            error("4_0");
        }
        else if(ch >= 'A' && ch <= 'z'){
            error("4_0");
        }
        else if(ch==0)
            hasToRead=false;
        else{
            hasToRead=false;
            addLex(lex,"con");//lexClass = CON
        }
    }


    private static void fState5()  {
        if(hasToRead){
            ch = getChar();
        }
        if(ch >= '0' && ch <= '9'){
            lex+=ch;
            fState5();
        }
        else if(ch==0)
            hasToRead=false;
        else{
//            if(!lex.endsWith(".")){
                if(ch == ')')
                    hasToRead=false;
                addLex(lex,"con");//lexClass = CON
//            }
//            else
//                error("5");
        }
    }

    private static void fState6() {
        if(hasToRead){
            ch = getChar();
        }
        if(ch >= '0' && ch <= '9'){
            lex+=ch;
            fState5();
        }
        else if(ch==0)
            hasToRead=false;
        else{
            error("6");
        }
    }

    private static void fState10() {
        if(hasToRead){
            ch = getChar();
        }
        if(ch == '<'){
            lex +=ch;
            addLex(lex,"<<");  // lex = <<
        }
        else if(ch == '='){
            lex +=ch;
            addLex(lex,"=<");   // lex = <=
        }
        else if(ch==0)
            hasToRead=false;
        else{
            hasToRead=false;
            addLex(lex,"<");  // lex = <
        }
    }

    private static void fState11() {
        if(hasToRead){
            ch = getChar();
        }

        if(ch == '>'){
            lex+=ch;
            addLex(lex,">>");   // lex = >>
        }
        else if(ch == '='){
            lex+=ch;
            addLex(lex,">=");    // lex = >=
        }
        else if(ch==0)
            hasToRead=false;
        else{
            hasToRead=false;
            addLex(lex,">");    // lex = >
        }
    }

    private static void fState12() {
        if(hasToRead){
            ch = getChar();
        }
        else hasToRead=true;

        if(ch == '='){
            lex+=ch;
            addLex(lex,"==");     // lex = ==
        }
        else if(ch==0)
            hasToRead=false;
        else{
            hasToRead=false;
            addLex(lex,"=");    // lex = =
        }
    }

    private static void fState13() {
        if(hasToRead){
            ch = getChar();
        }

        if(ch == '='){
            lex+=ch;
            addLex(lex,"!=");    // lex = !=
        }
        else if(ch==0)
            hasToRead=false;
        else{
            error("13");
        }
    }
}
