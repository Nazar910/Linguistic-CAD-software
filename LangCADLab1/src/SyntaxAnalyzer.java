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

    private static int i(){return counter;}
    private static void inc(){
        if(lexList.size()>counter) counter++;
    }
    private static boolean isDefined(int id) throws SyntaxError {
        int i = lexList.get(id).getKodIdCon()-1;
        if(LexicalAnalyzer.getTableManager().getIdRecords().get(i).getType()!=""){
            return true;
        }
        else throw new SyntaxError("Використання незазначеної змінної",lexList.get(i()).getLine());
    }
    private static boolean isNotDefined(int id) throws SyntaxError {
        int i = lexList.get(id).getKodIdCon()-1;
        if(LexicalAnalyzer.getTableManager().getIdRecords().get(i).getType()==""){
            return true;
        }
        else throw new SyntaxError("Зазначення вже зазнченої змінної",lexList.get(i()).getLine());
    }
    private static void resetStatic(){
        counter=0;
        flagLoop=false;
        flagReference=false;
    }
    private static boolean prog() throws SyntaxError {
        lexList = LexicalAnalyzer.getTableManager().getLexRecords();
        if(lexList.get(i()).getKod() == 1){//prog
            inc();
            if(lexList.get(i()).getKod() == 28){// IDN
                inc();
                if(lexList.get(i()).getKod() == 11){// ⁋
                    inc();
                    if(lexList.get(i()).getKod() == 2){// var
                        inc();
                        if(spOg()){
                            while(lexList.get(i()).getKod() == 11) inc();// ⁋
                            if(lexList.get(i()).getKod() == 9){ // {
                                while(lexList.get(i()).getKod() == 11) inc();// ⁋
                                inc();
                                if(spOp()){
                                    if(lexList.get(i()).getKod() == 10){ // }
                                        inc();
                                        return true;
                                    }
                                    else throw new SyntaxError("Відсутня закриваюча фігурна дужка",lexList.get(i()).getLine());
                                }
                                else  throw new SyntaxError("Невірний список операторів",lexList.get(i()).getLine());
                            }
                            else throw new SyntaxError("Відсутня відкриваюча дужка",lexList.get(i()).getLine());
                        }
                        else throw new SyntaxError("Невірний список оголошень",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Немає var",lexList.get(i()).getLine());
                }
                else throw new SyntaxError("Очікується перехід на новий рядок",lexList.get(i()).getLine());
            }
            else throw new SyntaxError("Нема назви програми",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Програма має починатися з prog", lexList.get(i()).getLine());
    }

    private static boolean spOp() throws SyntaxError {
        if(op()){
            if(lexList.get(i()).getKod() == 11 || lexList.get(i()).getKod() == 10){// ⁋
                //inc();
                while(lexList.get(i()).getKod() != 10){// }
                    if(lexList.get(i()).getKod() == 11)inc();
                    if(lexList.get(i()).getKod() == 10){continue;}
                    if(op()){
                        if(lexList.get(i()).getKod() == 11){// ⁋
                            inc();
                        }
                        else throw new SyntaxError("Немає ⁋",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Невірний оператор", lexList.get(i()).getLine());
                }
                return true;
            }
            else throw new SyntaxError("Немає ⁋ після першого операнду", lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Невірний перший операнд", lexList.get(i()).getLine());
    }

    private static boolean op() throws SyntaxError {
        while(lexList.get(i()).getKod() == 11) inc();
        if(lexList.get(i()).getKod() == 35) throw  new SyntaxError("Заборонено використання ; не в циклі",lexList.get(i()).getLine());
        if(lexList.get(i()).getKod() == 5){// cout
            inc();
            if(cout()){
                return true;
            }
            else throw new SyntaxError("Невірна операція виведення",lexList.get(i()).getLine());
        }
        else if(lexList.get(i()).getKod() == 6){// cin
            inc();
            if(cin()){
                return true;
            }
            else throw new SyntaxError("Невірна операція введення",lexList.get(i()).getLine());
        }
        else if(lexList.get(i()).getKod() == 28 && isDefined(i())){// idn
            inc();
            if(lexList.get(i()).getKod() == 19){// =
                inc();
                if(expression()){
                    return true;
                }
                else throw new SyntaxError("Невірний другий операнд присвоювання",lexList.get(i()).getLine());
        }
            else throw new SyntaxError("Невірна операція присвоювання",lexList.get(i()).getLine());
        }
        else if(lexList.get(i()).getKod() == 8){// for
            inc();
            if(loop()){
                return true;
            }
            else throw new SyntaxError("Невірний цикл",lexList.get(i()).getLine());
        }
        else if(lexList.get(i()).getKod() == 7){// if
            inc();
            if(conditionalBrunch()){
                return true;
            }
            else throw new SyntaxError("Невірний умовний перехід",lexList.get(i()).getLine());
        }
        else if(ternOp()){
            return true;
        }
        else throw new SyntaxError("Оператором може бути операції вводу/виводу, присвоювання, цикли та умовні переходи",lexList.get(i()).getLine());
    }

    private static boolean ternOp() throws SyntaxError {
        if(lexList.get(i()).getKod() == 17){// (
            inc();
                    if(logicalExpression()){
                        if(lexList.get(i()).getKod() == 33){// ?
                            inc();
                            if(expression()){
                                if(lexList.get(i()).getKod() == 34){// :
                                    inc();
                                    if(expression()){
                                        if(lexList.get(i()).getKod() == 18){// )
                                            inc();
                                            return true;
                                        }
                                        else throw new SyntaxError("Відсутня закриваюча дужка у тернарному операторі",lexList.get(i()).getLine());
                                    }
                                    else throw new SyntaxError("Невірний else вираз у тернарному операторі",lexList.get(i()).getLine());
                                }
                                else throw new SyntaxError("Відсутня двокрапка у тернарному операторі",lexList.get(i()).getLine());
                            }
                            else throw new SyntaxError("Невірний істинний вираз у тернарному операторі",lexList.get(i()).getLine());
                        }
                        else throw new SyntaxError("Очікується знак питання",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Невірний логічний вираз",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Очікується відкриваюча дужка у тернарному операторі",lexList.get(i()).getLine());
    }

    private static boolean expression() throws SyntaxError {
        if(therm()){
            //inc();
            if(!flagLoop && lexList.get(i()).getKod() == 35) throw  new SyntaxError("Заборонено використання ; не в циклі",lexList.get(i()).getLine());
            if(flagLoop && (lexList.get(i()).getKod() == 35 || lexList.get(i()).getKod() == 18)) return true;
            if(flagReference) return true;
            if(flagLogical && lexList.get(i()).getKod()>=22 &&  lexList.get(i()).getKod() <= 27) return true;
            flagLogical=false;
            while(lexList.get(i()).getKod() != 11 && (flagLoop && lexList.get(i()).getKod() != 18  && lexList.get(i()).getKod() !=10)){// ⁋
                if(!flagLoop && lexList.get(i()).getKod() == 35) throw  new SyntaxError("Заборонено використання ; не в циклі",lexList.get(i()).getLine());
                if(lexList.get(i()).getKod() == 13){// +
                    inc();
                    if(therm()){}
                    else throw new SyntaxError("Відсутній операнд для додавання",lexList.get(i()).getLine());
                }
                else if(lexList.get(i()).getKod() == 14){// -
                    inc();
                    if(therm()){}
                    else throw new SyntaxError("Відсутній операнд для віднімання",lexList.get(i()).getLine());
                }
                else throw new SyntaxError("Невірний знак (+ або -)",lexList.get(i()).getLine());
            }
            return true;
        }
        else throw new SyntaxError("Невірний терм",lexList.get(i()).getLine());
    }

    private static boolean therm() throws SyntaxError {
        if(c()){
            //inc();
            if(!flagLoop && lexList.get(i()).getKod() == 35) throw  new SyntaxError("Заборонено використання ; не в циклі",lexList.get(i()).getLine());
            if(flagLoop && (lexList.get(i()).getKod() == 35 || lexList.get(i()).getKod() == 13 || lexList.get(i()).getKod() == 14 || lexList.get(i()).getKod() == 18)) return true;
            if(flagReference) return true;
            if(flagLogical && lexList.get(i()).getKod()>=22 &&  lexList.get(i()).getKod() <= 27 ) return true;

            while(lexList.get(i()).getKod() != 11 && lexList.get(i()).getKod() != 34 && lexList.get(i()).getKod() !=18 && lexList.get(i()).getKod() !=10){// ⁋
                //inc();

                if(lexList.get(i()).getKod() == 15){// *
                    inc();
                    if(c()){}
                    else throw new SyntaxError("Відсутній операнд для множення",lexList.get(i()).getLine());
                }
                else if(lexList.get(i()).getKod() == 16) {// /
                    inc();
                    if (c()) {}
                    else throw new SyntaxError("Відсутній операнд для ділення", lexList.get(i()).getLine());
                } else if(lexList.get(i()).getKod()==13 || lexList.get(i()).getKod()==14){return true;}
                else throw new SyntaxError("Невірний знак(* або /)",lexList.get(i()).getLine());
            }
            flagLoop=false;
            return true;

        }
        else throw new SyntaxError("Невірний с()",lexList.get(i()).getLine());
    }

    private static boolean c() throws SyntaxError {
        if(lexList.get(i()).getKod() == 28 && isDefined(i()) || lexList.get(i()).getKod() == 29){// idn or con
            inc();
            return true;
        }
        else if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(expression()){
                if(lexList.get(i()).getKod() == 18){// )
                    inc();
                    return true;
                }
                else throw new SyntaxError("Відсутня закриваюча дужка",lexList.get(i()).getLine());
            }
            else throw new SyntaxError("Невірний вираз",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Повинно бути цифрою, число або виразом",lexList.get(i()).getLine());
    }

    private static boolean conditionalBrunch() throws SyntaxError{
        if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(reference()){
                if(lexList.get(i()).getKod() == 18){// )
                    inc();
                    if(lexList.get(i()).getKod() == 9){// {
                        inc();
                        if(spOp()){
                            if(lexList.get(i()).getKod() == 10){// }
                                inc();
                                return true;
                            }
                            else throw new SyntaxError("Очікується закриваюча дужка }",lexList.get(i()).getLine());
                        }
                        else throw new SyntaxError("Невірний список операторів",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Очікується відкриваюча дужка {",lexList.get(i()).getLine());
                }
                else throw new SyntaxError("Очікується закриваюча )",lexList.get(i()).getLine());
            }
            else throw new SyntaxError("Невірне відношення",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Очікується відкриваюча дужка (",lexList.get(i()).getLine());
    }

    private static boolean loop() throws SyntaxError {
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
                                            //inc();
                                            if(op()){
                                                return true;
                                            }
                                            else throw new SyntaxError("Невірний оператор у циклі",lexList.get(i()).getLine());
                                        }
                                        else throw new SyntaxError("Відсутня закриваюча дужка",lexList.get(i()).getLine());
                                    }
                                    else throw new SyntaxError("Невірний вираз",lexList.get(i()).getLine());
                                }
                                else throw new SyntaxError("Очікується =",lexList.get(i()).getLine());
                            }
                            else throw new SyntaxError("Очікується ідентифікатор",lexList.get(i()).getLine());
                        }
                        else throw new SyntaxError("Відсутній роздільник ;",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Невірне відношення у циклі",lexList.get(i()).getLine());
                }
                else throw new SyntaxError("Відсутній роздільник",lexList.get(i()).getLine());
            }
            else throw new SyntaxError("Невірний оператор у ініціалізації циклу",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Відсутня відкриваюча дужка",lexList.get(i()).getLine());
}

    private static boolean logicalExpression() throws SyntaxError {
        if(logicalTherm()){
            //inc();
            while(lexList.get(i()).getKod() == 31){// or
                inc();
                if(logicalTherm()){}
                else throw new SyntaxError("Невірний логічний терм пілся OR",lexList.get(i()).getLine());
            }
            return true;
        }
        else throw new SyntaxError("Невірний логічний терм",lexList.get(i()).getLine());
    }

    private static boolean logicalTherm() throws SyntaxError {
        if(logicalMul()){
            inc();
            while(lexList.get(i()).getKod() == 30) {// and
                inc();
                if(logicalMul()){}
                else throw new SyntaxError("Невірний логічний множник після AND",lexList.get(i()).getLine());
            }
            return true;
        }
        else throw new SyntaxError("Невірний логічний множник",lexList.get(i()).getLine());
    }

    private static boolean logicalMul() throws SyntaxError {
        flagLogical=true;
        if(expression()){
            inc();
            return true;
        }
        else if(lexList.get(i()).getKod() == 32){// not
            inc();
            if(logicalTherm()){
                inc();
                return true;
            }
            else throw new SyntaxError("Невірний логічний терм після NOT",lexList.get(i()).getLine());
        }
        else if(lexList.get(i()).getKod() == 17){// (
            inc();
            if(logicalExpression()){
                if(lexList.get(i()).getKod() == 18){// )
                    inc();
                    return true;
                }
                else throw new SyntaxError("Відсутня закриваюча дужка )",lexList.get(i()).getLine());
            }
            else throw new SyntaxError("Невірний логічний вираз",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Очікується вираз, ( логічний вираз ) або not ( логічний терм ) ",lexList.get(i()).getLine());
    }

    private static boolean reference() throws SyntaxError {
        flagReference = true;
        if(expression()){
            //inc();
            if(referenceSign()){
                if(expression()){
                    //inc();
                    flagReference=false;
                    return true;
                }
                else throw new SyntaxError("Невірний вираз після знаку відношення",lexList.get(i()).getLine());
            }
            else throw new SyntaxError("Невірний знак відношення",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Невірний вираз перед знаком відношення",lexList.get(i()).getLine());
    }

    private static boolean referenceSign() throws SyntaxError {
        if(lexList.get(i()).getKod() >=22 && lexList.get(i()).getKod() <= 27){// < > <= >= == !=
            inc();
            return true;
        }
        else throw new SyntaxError("Очікується знак відношення(<,>,<=,>=,==,!=)",lexList.get(i()).getLine());
    }

    private static boolean cin() throws SyntaxError {
        if(lexList.get(i()).getKod() == 21){// >>
            inc();
            if(lexList.get(i()).getKod() == 28 && isDefined(i())){// idn
                inc();
                while(lexList.get(i()).getKod() != 11){// ⁋
                    if(lexList.get(i()).getKod() == 21){// >>
                        inc();
                        if(lexList.get(i()).getKod() == 28){/*idn or con*/inc();}
                        else throw new SyntaxError("Невірний операнд вводу",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Операнди введення мають розділятися >>",lexList.get(i()).getLine());
                }
                return true;
            }
            else throw new SyntaxError("Невірний операнд вводу",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Операнди введення мають розділятися >>",lexList.get(i()).getLine());
    }

    private static boolean cout() throws SyntaxError {
        if(lexList.get(i()).getKod() == 20){// <<
            inc();
            if(lexList.get(i()).getKod() == 28 && isDefined(i()) || lexList.get(i()).getKod() == 29){// idn or con
                inc();
                while(lexList.get(i()).getKod() != 11){// ⁋
                    if(lexList.get(i()).getKod() == 20){// <<
                        inc();
                        if(lexList.get(i()).getKod() == 28 || lexList.get(i()).getKod() == 29){/*idn or con*/}
                        else throw new SyntaxError("Невірний операнд виводу",lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Операнди виведення мають розділятися <<",lexList.get(i()).getLine());
                }
                return true;
            }
            else throw new SyntaxError("Невірний операнд виводу",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Операнди виведення мають розділятися <<",lexList.get(i()).getLine());
    }

    private static boolean spOg() throws SyntaxError {
        if(type()){
            if(spId()){
                while(lexList.get(i()).getKod() == 11){// ⁋
                    inc();
                    if(lexList.get(i()).getKod() == 9) return true;
                    if(type()){
                        if(spId()){/*idn*/}
                        else throw new SyntaxError("Очікується ідентифікатор", lexList.get(i()).getLine());
                    }
                    else throw new SyntaxError("Нема типу", lexList.get(i()).getLine());
                }
                return true;
            }
            else throw new SyntaxError("Очікується список ідентифікаторів",lexList.get(i()).getLine());
        }
        else throw new SyntaxError("Відсутній тип",lexList.get(i()).getLine());
    }

    private static boolean spId() throws SyntaxError {
        if(lexList.get(i()).getKod() == 28){ // IDN
            inc();
            while(lexList.get(i()).getKod() == 12){// ,
                inc();
                if(lexList.get(i()).getKod() == 28){/* IDN*/}
                else throw new SyntaxError("В списку ідентифікаторів очікується ідентифікатор",lexList.get(i()).getLine());
            }
            inc();
            return true;
        }
        else throw new SyntaxError("В оголошенні очікується ідентифікатор",lexList.get(i()).getLine());
    }

    private static boolean type() throws SyntaxError {
        if(lexList.get(i()).getKod() == 3 || lexList.get(i()).getKod() == 4){// int or real
            inc();
            return true;
        }
        else throw new SyntaxError("Тип може бути real або int",lexList.get(i()).getLine());
    }

    public static void start() throws SyntaxError {
        resetStatic();
        if(prog()) System.out.println("Yeeeeeees");
    }

    public static int getCounter() {
        return counter;
    }
}
