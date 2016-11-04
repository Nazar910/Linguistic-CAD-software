import java.util.List;

/**
 * Created by pyvov on 30.10.2016.
 */
public class SyntaxAnalyzer {
    private static List<LexRecord> lexList;
    private static int counter;
    private static boolean flagLoop;
    private static boolean flagReference;
    private static boolean flagLogical;
    private static StringBuffer errorBuffer;

    private static boolean setCounter(int counter) {
        SyntaxAnalyzer.counter = counter; return true;
    }

    private static void errorLog(String str){
        errorBuffer.append(str+" : Line = "+lexList.get(i()).getLine()+'\n');
    }

    private static String getErrorLog(){
        return errorBuffer.toString();
    }

    private static int getBracesToBeClosed() {
        return bracesToBeClosed;
    }

    private static int bracesToBeClosed;

    private static boolean isEnd(){
        return counter == lexList.size()-1?true:false;
    }

    private static int i(){return counter;}

    private static void inc(){
        if(lexList.size()>counter-1) counter++;
    }

    private static void incBraces(){ bracesToBeClosed++; }

    private static void decBraces(){ bracesToBeClosed--; }

    private static boolean isDefined(int id){
        int i = lexList.get(id).getKodIdCon()-1;
        if(LexicalAnalyzer.getTableManager().getIdRecords().get(i).getType()!=""){ return true; }
        else{ errorLog("Використання незазначеної змінної"); return false; }
    }

    private static boolean isNotDefined(int id){
        int i = lexList.get(id).getKodIdCon()-1;
        if(LexicalAnalyzer.getTableManager().getIdRecords().get(i).getType()==""){ return true; }
        else { errorLog("Зазначення вже зазнченої змінної"); return false; }
    }

    private static void resetStatic(){
        counter=0;
        flagLoop=false;
        flagReference=false;
        bracesToBeClosed=0;
        errorBuffer = new StringBuffer("");
    }

    private static void endOfLine(){
        while(lexList.get(i()).getKod() == 11){// ⁋
            inc();
        }
    }

    private static boolean openBraces(){
        if(lexList.get(i()).getKod() == 9) {// {
            inc();
            incBraces();
            while (lexList.get(i()).getKod() == 9) {// {
                inc();
                incBraces();
            }
            return true;
        }
        else{ errorLog("Немає відкриваючої фігурнох дужки"); return false; }
    }

    private static boolean closeBraces(){
        while(lexList.get(i()).getKod() == 10){// }
            inc();
            decBraces();
        }
        if(getBracesToBeClosed() == 0){ return true; }
        else if(getBracesToBeClosed() > 0){ errorLog("Не вистачає закриваючої фігурної дужки"); return false; }
        else{ errorLog("Забагато закриваючих фігурних дужок"); return false; }
    }

    private static boolean prog(){
        lexList = LexicalAnalyzer.getTableManager().getLexRecords();
        if(lexList.get(i()).getKod() == 1){//prog
            inc();
            if(lexList.get(i()).getKod() == 28){// IDN
                inc();
                if(lexList.get(i()).getKod() == 11){// ⁋
                    inc();
                    endOfLine();
                    if(lexList.get(i()).getKod() == 2){// var
                        inc();
                        if(spOg()){
                            endOfLine();
                            if(openBraces()){ // {
                                endOfLine();
                                if(spOp()){
                                    if(closeBraces()){ return true; }// }
                                    else{ return false; }
                                }
                                else{ errorLog("Невірний список операторів"); return false; }
                            }
                            else{ return false; }
                        }
                        else{ errorLog("Невірний список оголошень"); return false; }
                    }
                    else{ errorLog("Немає var"); return false; }
                }
                else{ errorLog("Очікується перехід на новий рядок"); return false; }
            }
            else{ errorLog("Нема назви програми"); return false; }
        }
        else{ errorLog("Програма має починатися з prog"); return false; }
    }

    private static boolean spOp(){
        if(op()){
            if(lexList.get(i()).getKod() == 11 || lexList.get(i()).getKod() == 10){// ⁋
                while(lexList.get(i()).getKod() != 10){// }
                    if(isEnd()) { errorLog("Неочікуваний кінець програми. Відсутня закриваюча фігурна дужка"); return false; }
                    endOfLine();
                    if(lexList.get(i()).getKod() == 10){continue;}
                    if(op()){
                        if(lexList.get(i()).getKod() == 11){ inc(); }// ⁋
                        else{ errorLog("Немає ⁋"); return false; }
                    }
                    else{ errorLog("Невірний оператор"); return false; }
                }
                return true;
            }
            else{ errorLog("Немає ⁋ після першого операнду"); return false; }
        }
        else{ errorLog("Невірний перший операнд"); return false; }
    }

    private static boolean op(){
        if(lexList.get(i()).getKod() == 35){ errorLog("Заборонено використання ; не в циклі"); return false; }
        if(lexList.get(i()).getKod() == 5){// cout
            inc();
            if(cout()){ return true; }
            else { errorLog("Невірна операція виведення"); return false; }
        }
        else if(lexList.get(i()).getKod() == 6){// cin
            inc();
            if(cin()){ return true; }
            else{ errorLog("Невірна операція введення"); return false; }
        }
        else if(lexList.get(i()).getKod() == 28 && isDefined(i())){// idn
            inc();
            if(lexList.get(i()).getKod() == 19){// =
                inc();
                int temp = getCounter();// saving counter in a temp in case it is a logical expression, not just expression
                if(expression()){ return true; }
                else if(setCounter(temp) && logicalExpression()){
                    if(lexList.get(i()).getKod() == 33){// ?
                        inc();
                        if(expression()){
                            if(lexList.get(i()).getKod() == 34){// :
                                inc();
                                if(expression()){
                                        return true;
                                }
                                else { errorLog("Невірний else вираз тернарного оператору"); return false; }
                            }
                            else { errorLog("Очікується двокрапка \":\""); return false; }
                        }
                        else { errorLog("Невірний істинний вираз тернарного оператора"); return false; }
                    }
                    else{ errorLog("Очікується знак питання, якщо це тернарний оператор"); return false; }
                }
                else { errorLog("Невірний другий операнд присвоювання"); return false; }
        }
            else { errorLog("Невірна операція присвоювання"); return false; }
        }
        else if(lexList.get(i()).getKod() == 8){// for
            inc();
            if(loop()){ return true; }
            else{ errorLog("Невірний цикл"); return false; }
        }
        else if(lexList.get(i()).getKod() == 7){// if
            inc();
            if(conditionalBrunch()){ return true; }
            else{ errorLog("Невірний умовний перехід"); return false; }
        }
        else{ errorLog("Оператором може бути операції вводу/виводу, присвоювання, цикли та умовні переходи"); return false; }
    }

    private static boolean expression(){
        if(therm()){
            //inc();
            if(!flagLoop && lexList.get(i()).getKod() == 35){ errorLog("Заборонено використання \";\" не в циклі"); return false; }
            if(lexList.get(i()).getKod() == 34){ return true; }
            if(flagLoop && (lexList.get(i()).getKod() == 35 || lexList.get(i()).getKod() == 18) || lexList.get(i()).getKod() == 33) return true;
            if(flagReference) return true;
            if(flagLogical && lexList.get(i()).getKod()>=22 &&  lexList.get(i()).getKod() <= 27) return true;
            flagLogical=false;
            boolean first = lexList.get(i()).getKod() != 18 && lexList.get(i()).getKod() != 35,
                    second = lexList.get(i()).getKod() != 11 && lexList.get(i()).getKod() !=10 && lexList.get(i()).getKod() != 18;
            boolean fl = flagLoop?first:second;
            while(fl
                    /*lexList.get(i()).getKod() != 11 && (flagLoop && lexList.get(i()).getKod() != 18  && lexList.get(i()).getKod() !=10)*/){// ⁋
                if(lexList.get(i()).getKod() == 13){// +
                    inc();
                    if(therm()){}
                    else{ errorLog("Відсутній операнд для додавання"); return false; }
                }
                else if(lexList.get(i()).getKod() == 14){// -
                    inc();
                    if(therm()){}
                    else{ errorLog("Відсутній операнд для віднімання"); return false; }
                }
                else{ errorLog("Невірний знак (+ або -)"); return false; }
                first = lexList.get(i()).getKod() != 18 && lexList.get(i()).getKod() != 35;
                second = lexList.get(i()).getKod() != 11 && lexList.get(i()).getKod() !=10 && lexList.get(i()).getKod() != 18;
                fl = flagLoop?first:second;
            }
            return true;
        }
        else{ errorLog("Невірний терм"); return false; }
    }

    private static boolean therm(){
        if(c()){
            if(!flagLoop && lexList.get(i()).getKod() == 35){ errorLog("Заборонено використання ; не в циклі"); return false; }
            if(flagLoop && (lexList.get(i()).getKod() == 35 || lexList.get(i()).getKod() == 13 || lexList.get(i()).getKod() == 14 || lexList.get(i()).getKod() == 18)) return true;
            if(flagReference) return true;
            if(flagLogical && lexList.get(i()).getKod()>=22 &&  lexList.get(i()).getKod() <= 27 ) return true;

            while(lexList.get(i()).getKod() != 11 && lexList.get(i()).getKod() != 34 && lexList.get(i()).getKod() !=18 && lexList.get(i()).getKod() !=10
                    && lexList.get(i()).getKod() != 13 && lexList.get(i()).getKod() != 14){// ⁋
                if(lexList.get(i()).getKod() == 15){// *
                    inc();
                    if(c()){}
                    else{ errorLog("Відсутній операнд для множення"); return false; }
                }
                else if(lexList.get(i()).getKod() == 16) {// /
                    inc();
                    if (c()) {}
                    else{ errorLog("Відсутній операнд для ділення"); return false; }
                } else if(lexList.get(i()).getKod()==13 || lexList.get(i()).getKod()==14){return true;}
                else{ errorLog("Невірний знак(* або /)"); return false; }
            }
            flagLoop=false;
            return true;

        }
        else{ errorLog("Невірний с()"); return false; }
    }

    private static boolean c(){
        if(lexList.get(i()).getKod() == 28 && isDefined(i()) || lexList.get(i()).getKod() == 29){// idn or con
            inc();
            return true;
        }
        else if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(expression()){
                if(lexList.get(i()).getKod() == 18){ inc(); return true; }// )
                else{ errorLog("Відсутня закриваюча дужка"); return false; }
            }
            else{ errorLog("Невірний вираз"); return false; }
        }
        else{ errorLog("Повинно бути цифрою, число або виразом"); return false; }
    }

    private static boolean conditionalBrunch(){
        if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(reference()){
                if(lexList.get(i()).getKod() == 18){// )
                    inc();
                    if(lexList.get(i()).getKod() == 9){// {
                        inc();
                        if(spOp()){
                            if(lexList.get(i()).getKod() == 10){ inc(); return true; }// }
                            else{ errorLog("Очікується закриваюча дужка \"}\""); return false; }
                        }
                        else{ errorLog("Невірний список операторів"); return false; }
                    }
                    else{ errorLog("Очікується відкриваюча дужка \"{\""); return false; }
                }
                else{ errorLog("Очікується закриваюча \")\""); return false; }
            }
            else{ errorLog("Невірне відношення"); return false; }
        }
        else{ errorLog("Очікується відкриваюча дужка \"(\""); return false; }
    }

    private static boolean loop(){
        flagLoop=true;
        if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(op()){
                if(lexList.get(i()).getKod() == 35){// ;
                    inc();
                    if(reference()){
                        if(lexList.get(i()).getKod() == 35){// ;
                            inc();
                            if(lexList.get(i()).getKod() == 28 && isDefined(i())){// idn
                                inc();
                                if(lexList.get(i()).getKod() == 19){// =
                                    inc();
                                    if(expression()){
                                        if(lexList.get(i()).getKod() == 18){// )
                                            inc();
                                            if(op()){ return true; }
                                            else{ errorLog("Невірний оператор у циклі"); return false; }
                                        }
                                        else{ errorLog("Відсутня закриваюча дужка"); return false; }
                                    }
                                    else{ errorLog("Невірний вираз"); return false; }
                                }
                                else{ errorLog("Очікується \"=\""); return false; }
                            }
                            else{ errorLog("Очікується ідентифікатор"); return false; }
                        }
                        else{ errorLog("Відсутній роздільник \";\""); return false; }
                    }
                    else{ errorLog("Невірне відношення у циклі"); return false; }
                }
                else{ errorLog("Відсутній роздільник"); return false;
                }
            }
            else{ errorLog("Невірний оператор у ініціалізації циклу"); return false; }
        }
        else{ errorLog("Відсутня відкриваюча дужка"); return false; }
}

    private static boolean logicalExpression(){
        if(logicalTherm()){
            while(lexList.get(i()).getKod() == 31){// or
                inc();
                if(logicalTherm()){}
                else{ errorLog("Невірний логічний терм пілся OR"); return false; }
            }
            return true;
        }
        else{ errorLog("Невірний логічний терм"); return false; }
    }

    private static boolean logicalTherm(){
        if(logicalMul()){
            //inc();
            while(lexList.get(i()).getKod() == 30) {// and
                inc();
                if(logicalMul()){}
                else{ errorLog("Невірний логічний множник після AND"); return false; }
            }
            return true;
        }
        else{ errorLog("Невірний логічний множник"); return false; }
    }

    private static boolean logicalMul(){
        flagLogical=true;
        if(/*expression()*/reference()){ /*inc();*/ return true; }
        else if(lexList.get(i()).getKod() == 32){// not
            inc();
            if(logicalTherm()){ inc(); return true; }
            else{ errorLog("Невірний логічний терм після NOT"); return false; }
        }
        else if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(logicalExpression()){
                if(lexList.get(i()).getKod() == 18){ inc(); return true; }// )
                else{ errorLog("Відсутня закриваюча дужка \")\""); return false; }
            }
            else{ errorLog("Невірний логічний вираз"); return false; }
        }
        else{ errorLog("Очікується вираз, ( логічний вираз ) або not ( логічний терм ) "); return false; }
    }

    private static boolean reference(){
        flagReference = true;
        if(expression()){
            if(referenceSign()){
                if(expression()){ flagReference=false; return true; }
                else{ errorLog("Невірний вираз після знаку відношення"); return false; }
            }
            else{ errorLog("Невірний знак відношення"); return false; }
        }
        else{ errorLog("Невірний вираз перед знаком відношення"); return false; }
    }

    private static boolean referenceSign(){
        if(lexList.get(i()).getKod() >=22 && lexList.get(i()).getKod() <= 27){// < > <= >= == !=
            inc();
            return true;
        }
        else{ errorLog("Очікується знак відношення(<,>,<=,>=,==,!=)"); return false; }
    }

    private static boolean cin(){
        if(lexList.get(i()).getKod() == 21){// >>
            inc();
            if(lexList.get(i()).getKod() == 28 && isDefined(i())){// idn
                inc();
                while(lexList.get(i()).getKod() != 11){// ⁋
                    if(lexList.get(i()).getKod() == 21){// >>
                        inc();
                        if(lexList.get(i()).getKod() == 28 && isDefined(i())){/*idn*/inc();}
                        else{ errorLog("Невірний операнд вводу"); return false; }
                    }
                    else{ errorLog("Операнди введення мають розділятися >>"); return false; }
                }
                return true;
            }
            else{ errorLog("Невірний операнд вводу"); return false; }
        }
        else{ errorLog("Операнди введення мають розділятися >>"); return false; }
    }

    private static boolean cout(){
        if(lexList.get(i()).getKod() == 20){// <<
            inc();
            if(lexList.get(i()).getKod() == 28 && isDefined(i()) || lexList.get(i()).getKod() == 29){// idn or con
                inc();
                while(lexList.get(i()).getKod() != 11){// ⁋
                    if(lexList.get(i()).getKod() == 20){// <<
                        inc();
                        if(lexList.get(i()).getKod() == 28 || lexList.get(i()).getKod() == 29){/*idn or con*/}
                        else{ errorLog("Невірний операнд виводу"); return false; }
                    }
                    else{ errorLog("Операнди виведення мають розділятися <<"); return false; }
                }
                return true;
            }
            else{ errorLog("Невірний операнд виводу"); return false; }
        }
        else{ errorLog("Операнди виведення мають розділятися <<"); return false; }
    }

    private static boolean spOg(){
        if(type()){
            if(spId()){
                while(lexList.get(i()).getKod() == 11){// ⁋
                    inc();
                    if(lexList.get(i()).getKod() == 9) return true;
                    if(type()){
                        if(spId()){/*idn*/}
                        else{ errorLog("Очікується ідентифікатор"); return false; }
                    }
                    else{ errorLog("Нема типу"); return false; }
                }
                return true;
            }
            else{ errorLog("Очікується список ідентифікаторів"); return false; }
        }
        else{ errorLog("Відсутній тип"); return false; }
    }

    private static boolean spId(){
        if(lexList.get(i()).getKod() == 28){ // IDN
            inc();
            while(lexList.get(i()).getKod() == 12){// ,
                inc();
                if(lexList.get(i()).getKod() == 28){/* IDN*/inc(); }
                else{ errorLog("В списку ідентифікаторів очікується ідентифікатор"); return false; }
            }
            return true;
        }
        else{ errorLog("В оголошенні очікується ідентифікатор"); return false; }
    }

    private static boolean type(){
        if(lexList.get(i()).getKod() == 3 || lexList.get(i()).getKod() == 4){ inc(); return true; }
        else if(op()){ errorLog("Тіло програми має бути обмежене фігурними дужками"); return false; }
        else{ errorLog("Тип може бути real або int"); return false; }
    }

    public static void start() throws SyntaxError {
        resetStatic();
        if(prog()) System.out.println("Yeeeeeees");
        else throw new SyntaxError(getErrorLog());
    }

    public static int getCounter() {
        return counter;
    }
}
