package com.pyvovar.nazar.syntax;

import com.pyvovar.nazar.records.IdRecord;
import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.SyntaxError;

import java.util.List;

/**
 * Created by pyvov on 30.10.2016.
 */
public class SyntaxAnalyzer {
    private List<LexRecord> lexList;
    private List<IdRecord> idRecords;
    private int counter;
    private StringBuffer errorBuffer;

    public SyntaxAnalyzer(List<LexRecord> lexList, List<IdRecord> idRecords) {
        this.lexList = lexList;
        this.idRecords = idRecords;
    }

    private boolean setCounter(int counter) {
        this.counter = counter;
        return true;
    }

    private void errorLog(String str) {
        errorBuffer.append(str + " : Line = " + lexList.get(i()).getLine() + '\n');
    }

    private void clearErrorLog() {
        errorBuffer.delete(0, errorBuffer.length());
    }

    private String getErrorLog() {
        return errorBuffer.toString();
    }

    private int getBracesToBeClosed() {
        return bracesToBeClosed;
    }

    private int bracesToBeClosed;

    private boolean isEnd() {
        return counter == lexList.size() - 1 ? true : false;
    }

    private int i() {
        return counter;
    }

    private void inc() {
        if (lexList.size() > counter - 1) counter++;
    }

    private void incBraces() {
        bracesToBeClosed++;
    }

    private void decBraces() {
        bracesToBeClosed--;
    }

    private boolean isDefined(int id) {
        int i = lexList.get(id).getKodIdCon() - 1;

        if (this.idRecords.get(i).getType().equals(" prog")) {
            errorLog("Не можна використовувати змінну типу prog");
            return false;
        } else if (this.idRecords.get(i).getType() != "") {
            return true;
        } else {
            errorLog("Використання незазначеної змінної");
            return false;
        }
    }

    private void reset() {
        counter = 0;
        bracesToBeClosed = 0;
        errorBuffer = new StringBuffer("");
    }

    private boolean openBraces() {
        if (lexList.get(i()).getKod() == 9) {// {
            inc();
            incBraces();
            while (lexList.get(i()).getKod() == 9) {// {
                inc();
                incBraces();
            }
            return true;
        } else {
            errorLog("Немає відкриваючої фігурнох дужки");
            return false;
        }
    }

    private boolean closeBraces() {
        while (lexList.get(i()).getKod() == 10) {// }
            inc();
            decBraces();
        }
        if (getBracesToBeClosed() == 0) {
            int i = i();
            while (i < lexList.size()) {
                if (lexList.get(i).getKod() != 11) {
                    errorLog("За межами тіла програми не має нічого бути");
                    return false;
                }
                i++;
            }
            return true;
        } else if (getBracesToBeClosed() > 0) {
            errorLog("Не вистачає закриваючої фігурної дужки");
            return false;
        } else {
            errorLog("Забагато закриваючих фігурних дужок");
            return false;
        }
    }

    private boolean prog() {
        if (lexList.get(i()).getKod() == 1) {//prog
            inc();
            if (lexList.get(i()).getKod() == 28) {// IDN
                inc();
                if (lexList.get(i()).getKod() == 11) {// ⁋
                    inc();
                    if (lexList.get(i()).getKod() == 2) {// var
                        inc();
                        if (spOg()) {
                            if (openBraces()) { // {
                                if (spOp()) {
                                    if (closeBraces()) {// }
                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    errorLog("Невірний список операторів");
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            errorLog("Невірний список оголошень");
                            return false;
                        }
                    } else {
                        errorLog("Немає var");
                        return false;
                    }
                } else {
                    errorLog("Очікується перехід на новий рядок");
                    return false;
                }
            } else {
                errorLog("Нема назви програми");
                return false;
            }
        } else {
            errorLog("Програма має починатися з prog");
            return false;
        }
    }

    private boolean spOp() {
        if (op()) {
            if (lexList.get(i()).getKod() == 11) {// ⁋
                inc();
                while (lexList.get(i()).getKod() != 10) {// }
                    if (isEnd()) {
                        errorLog("Неочікуваний кінець програми. Відсутня закриваюча фігурна дужка");
                        return false;
                    }
                    if (op()) {
                        if (lexList.get(i()).getKod() == 11) {
                            inc();
                        }// ⁋
                        else {
                            errorLog("Немає ⁋");
                            return false;
                        }
                    } else {
                        errorLog("Невірний оператор");
                        return false;
                    }
                }
                return true;
            } else {
                errorLog("Немає ⁋ після першого операнду");
                return false;
            }
        } else {
            errorLog("Невірний перший операнд");
            return false;
        }
    }

    private boolean op() {
        /*if (lexList.get(i()).getKod() == 35) {
            errorLog("Заборонено використання ; не в циклі");
            return false;
        }*/
        if (lexList.get(i()).getKod() == 5) {// cout
            inc();
            if (cout()) {
                return true;
            } else {
                errorLog("Невірна операція виведення");
                return false;
            }
        } else if (lexList.get(i()).getKod() == 6) {// cin
            inc();
            if (cin()) {
                return true;
            } else {
                errorLog("Невірна операція введення");
                return false;
            }
        } else if (lexList.get(i()).getKod() == 28 && isDefined(i())) {// idn
            inc();
            if (lexList.get(i()).getKod() == 19) {// =
                inc();
                int temp = getCounter();// saving counter in a temp in case it is an expression, not logical expression
                if (logicalExpression()) {
                    if (lexList.get(i()).getKod() == 33) {// ?
                        inc();
                        if (expression()) {
                            if (lexList.get(i()).getKod() == 34) {// :
                                inc();
                                if (expression()) {
                                    return true;
                                } else {
                                    errorLog("Невірний else вираз тернарного оператору");
                                    return false;
                                }
                            } else {
                                errorLog("Очікується двокрапка \":\"");
                                return false;
                            }
                        } else {
                            errorLog("Невірний істинний вираз тернарного оператора");
                            return false;
                        }
                    } else {
                        errorLog("Очікується знак питання, якщо це тернарний оператор");
                        return false;
                    }
                } else if (setCounter(temp) && expression()) {
                    clearErrorLog();
                    return true;
                } else {
                    errorLog("Невірний другий операнд присвоювання");
                    return false;
                }
            } else {
                errorLog("Невірна операція присвоювання");
                return false;
            }
        } else if (lexList.get(i()).getKod() == 8) {// for
            inc();
            if (loop()) {
                return true;
            } else {
                errorLog("Невірний цикл");
                return false;
            }
        } else if (lexList.get(i()).getKod() == 7) {// if
            inc();
            if (conditionalBrunch()) {
                return true;
            } else {
                errorLog("Невірний умовний перехід");
                return false;
            }
        } else {
            errorLog("Оператором може бути операції вводу/виводу, присвоювання, цикли та умовні переходи");
            return false;
        }
    }

    private boolean expression() {
        if (therm()) {
            while (lexList.get(i()) != null
                    && (lexList.get(i()).getKod() == 13 || lexList.get(i()).getKod() == 14)) {// + -
                inc();
                if (therm()) {
                } else {
                    errorLog("Відсутній операнд для додавання");
                    return false;
                }
            }
            return true;
        } else {
            errorLog("Невірний терм");
            return false;
        }

    }

    private boolean therm() {
        if (c()) {
            while (lexList.get(i()) != null
                    && (lexList.get(i()).getKod() == 15 || lexList.get(i()).getKod() == 16)) {// * /
                inc();
                if (c()) {
                } else {
                    errorLog("Відсутній операнд для множення");
                    return false;
                }
            }
            return true;

        } else {
            errorLog("Невірний множник");
            return false;
        }
    }

    private boolean c() {
        if (lexList.get(i()).getKod() == 28 && isDefined(i()) || lexList.get(i()).getKod() == 29) {// idn or con
            inc();
            return true;
        } else if (lexList.get(i()).getKod() == 17) {// (
            inc();
            if (expression()) {
                if (lexList.get(i()).getKod() == 18) {// )
                    inc();
                    return true;
                } else {
                    errorLog("Відсутня закриваюча дужка");
                    return false;
                }
            } else {
                errorLog("Невірний вираз");
                return false;
            }
        } else {
            errorLog("Повинно бути цифрою, число або виразом");
            return false;
        }
    }

    private boolean conditionalBrunch() {
        if (lexList.get(i()).getKod() == 17) {// (
            inc();
            if (reference()) {
                if (lexList.get(i()).getKod() == 18) {// )
                    inc();
                    if (lexList.get(i()).getKod() == 9) {// {
                        inc();
                        if (spOp()) {
                            if (lexList.get(i()).getKod() == 10) {
                                inc();
                                return true;
                            }// }
                            else {
                                errorLog("Очікується закриваюча дужка \"}\"");
                                return false;
                            }
                        } else {
                            errorLog("Невірний список операторів");
                            return false;
                        }
                    } else {
                        errorLog("Очікується відкриваюча дужка \"{\"");
                        return false;
                    }
                } else {
                    errorLog("Очікується закриваюча \")\"");
                    return false;
                }
            } else {
                errorLog("Невірне відношення");
                return false;
            }
        } else {
            errorLog("Очікується відкриваюча дужка \"(\"");
            return false;
        }
    }

    private boolean loop() {
        if (lexList.get(i()).getKod() == 17) {// (
            inc();
            if (op()) {
                if (lexList.get(i()).getKod() == 35) {// ;
                    inc();
                    if (logicalExpression()) {
                        if (lexList.get(i()).getKod() == 35) {// ;
                            inc();
                            if (lexList.get(i()).getKod() == 28 && isDefined(i())) {// idn
                                inc();
                                if (lexList.get(i()).getKod() == 19) {// =
                                    inc();
                                    if (expression()) {
                                        if (lexList.get(i()).getKod() == 18) {// )
                                            inc();
                                            if (op()) {
                                                return true;
                                            } else {
                                                errorLog("Невірний оператор у циклі");
                                                return false;
                                            }
                                        } else {
                                            errorLog("Відсутня закриваюча дужка");
                                            return false;
                                        }
                                    } else {
                                        errorLog("Невірний вираз");
                                        return false;
                                    }
                                } else {
                                    errorLog("Очікується \"=\"");
                                    return false;
                                }
                            } else {
                                errorLog("Очікується ідентифікатор");
                                return false;
                            }
                        } else {
                            errorLog("Відсутній роздільник \";\"");
                            return false;
                        }
                    } else {
                        errorLog("Невірне відношення у циклі");
                        return false;
                    }
                } else {
                    errorLog("Відсутній роздільник");
                    return false;
                }
            } else {
                errorLog("Невірний оператор у ініціалізації циклу");
                return false;
            }
        } else {
            errorLog("Відсутня відкриваюча дужка");
            return false;
        }
    }

    private boolean logicalExpression() {
        if (logicalTherm()) {
            while (lexList.get(i()).getKod() == 31) {// or
                inc();
                if (logicalTherm()) {
                } else {
                    errorLog("Невірний логічний терм пілся OR");
                    return false;
                }
            }
            return true;
        } else {
            errorLog("Невірний логічний терм");
            return false;
        }
    }

    private boolean logicalTherm() {
        if (logicalMul()) {
            while (lexList.get(i()).getKod() == 30) {// and
                inc();
                if (logicalMul()) {
                } else {
                    errorLog("Невірний логічний множник після AND");
                    return false;
                }
            }
            return true;
        } else {
            errorLog("Невірний логічний множник");
            return false;
        }
    }

    private boolean logicalMul() {
        int temp = i();
        if (reference()) {
            return true;
        } else if (setCounter(temp) && lexList.get(i()).getKod() == 32) {// not
            inc();
            if (logicalTherm()) {
                //inc();
                return true;
            } else {
                errorLog("Невірний логічний терм після NOT");
                return false;
            }
        } else if (lexList.get(i()).getKod() == 17) {// (
            inc();
            if (logicalExpression()) {
                if (lexList.get(i()).getKod() == 18) {// )
                    inc();
                    return true;
                } else {
                    errorLog("Відсутня закриваюча дужка \")\"");
                    return false;
                }
            } else {
                errorLog("Невірний логічний вираз");
                return false;
            }
        } else {
            errorLog("Очікується вираз, ( логічний вираз ) або not ( логічний терм ) ");
            return false;
        }
    }

    private boolean reference() {
        if (expression()) {
            if (referenceSign()) {
                if (expression()) {
                    return true;
                } else {
                    errorLog("Невірний вираз після знаку відношення");
                    return false;
                }
            } else {
                errorLog("Невірний знак відношення");
                return false;
            }
        } else {
            errorLog("Невірний вираз перед знаком відношення");
            return false;
        }
    }

    private boolean referenceSign() {
        if (lexList.get(i()).getKod() >= 22 && lexList.get(i()).getKod() <= 27) {// < > <= >= == !=
            inc();
            return true;
        } else {
            errorLog("Очікується знак відношення(<,>,<=,>=,==,!=)");
            return false;
        }
    }

    private boolean cin() {
        if (lexList.get(i()).getKod() == 21) {// >>
            inc();
            if (lexList.get(i()).getKod() == 28 && isDefined(i())) {// idn
                inc();
                while (lexList.get(i()).getKod() == 21) {// >>
                    inc();
                    if (lexList.get(i()).getKod() == 28 && isDefined(i())) {/*idn*/
                        inc();
                    } else {
                        errorLog("Невірний операнд вводу");
                        return false;
                    }
                }
                return true;
            } else {
                errorLog("Невірний операнд вводу");
                return false;
            }
        } else {
            errorLog("Операнди введення мають розділятися >>");
            return false;
        }
    }

    private boolean cout() {
        if (lexList.get(i()).getKod() == 20) {// <<
            inc();
            if (lexList.get(i()).getKod() == 28 && isDefined(i()) || lexList.get(i()).getKod() == 29) {// idn or con
                inc();
                while (lexList.get(i()).getKod() == 20) {// <<
                    inc();
                    if (lexList.get(i()).getKod() == 28 || lexList.get(i()).getKod() == 29) {/*idn or con*/
                        inc();
                    } else {
                        errorLog("Невірний операнд виводу");
                        return false;
                    }
                }
                return true;
            } else {
                errorLog("Невірний операнд виводу");
                return false;
            }
        } else {
            errorLog("Операнди виведення мають розділятися <<");
            return false;
        }
    }

    private boolean spOg() {
        if (type()) {
            if (spId()) {
                while (lexList.get(i()).getKod() == 11 && lexList.get(i() + 1).getKod() != 9) {// ⁋
                    inc();
                    if (type()) {
                        if (spId()) {/*idn*/} else {
                            errorLog("Очікується ідентифікатор");
                            return false;
                        }
                    } else {
                        errorLog("Нема типу");
                        return false;
                    }
                }
                inc();
                return true;
            } else {
                errorLog("Очікується список ідентифікаторів");
                return false;
            }
        } else {
            errorLog("Відсутній тип");
            return false;
        }
    }

    private boolean spId() {
        if (lexList.get(i()).getKod() == 28) { // IDN
            inc();
            while (lexList.get(i()).getKod() == 12) {// ,
                inc();
                if (lexList.get(i()).getKod() == 28) {/* IDN*/
                    inc();
                } else {
                    errorLog("В списку ідентифікаторів очікується ідентифікатор");
                    return false;
                }
            }
            return true;
        } else {
            errorLog("В оголошенні очікується ідентифікатор");
            return false;
        }
    }

    private boolean type() {
        if (lexList.get(i()).getKod() == 3 || lexList.get(i()).getKod() == 4) {// int or real
            inc();
            return true;
        } else if (op()) {
            errorLog("Тіло програми має бути обмежене фігурними дужками");
            return false;
        } else {
            errorLog("Тип може бути real або int");
            return false;
        }
    }

    public void start() throws SyntaxError {
        reset();
        if (prog()) System.out.println("Yeeeeeees");
        else throw new SyntaxError(getErrorLog());
    }

    public int getCounter() {
        return counter;
    }
}
