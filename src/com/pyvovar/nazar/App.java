package com.pyvovar.nazar;

import com.pyvovar.nazar.helpers.Callback;
import com.pyvovar.nazar.helpers.FileManager;
import com.pyvovar.nazar.lexical.LexicalAnalyzer;
import com.pyvovar.nazar.records.ConRecord;
import com.pyvovar.nazar.records.IdRecord;
import com.pyvovar.nazar.records.LexRecord;
import com.pyvovar.nazar.errors.LexicalError;
import com.pyvovar.nazar.errors.SyntaxError;
import com.pyvovar.nazar.syntax.SyntaxPrecedenceTableAnalyzer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pyvov on 02.10.2016.
 */
public class App extends Application {
    Stage window;
    FileManager file = new FileManager("./program.txt");
    TextArea textArea = new TextArea(file.read());
    //TextArea numberLines=new TextArea();
    Label numberLines = new Label();
    TextArea errors = new TextArea();
    static LexicalError lexicalError;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Лексичний+Синтаксичний аналізатор. Пивовар Назарій Вар 10 група ТР-41");
        Button buttonStart = new Button("Почати");

        buttonStart.setOnAction(e -> initLexicalAnalyzer());
        numberLines.setMinWidth(30);
        numberLines.setPadding(new Insets(7, 0, 0, 0));
        textArea.textProperty().addListener(e -> numberLines(textArea.getText()));
        textArea.setMinSize(400, 450);
        textArea.setStyle("-fx-font-size: 28px");
        errors.setStyle("-fx-font-size: 28px");
        buttonStart.setStyle("-fx-font-size: 28px");
        numberLines.setStyle("-fx-font-size: 28px;" +
                "-fx-alignment: center");
        numberLines(textArea.getText());
        errors.setMaxHeight(85);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(textArea, errors);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(numberLines, vBox, buttonStart);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(hBox);


        Scene scene = new Scene(stackPane);
        final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.F5, KeyCombination.CONTROL_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyCombination.match(event)) {
                    buttonStart.fire();
                }
            }
        });
        window.setMinWidth(550);
        window.setMinHeight(580);
        window.setScene(scene);
        window.show();
    }

    private ObservableList<LexRecord> getLexes(LexicalAnalyzer lexical) {
        ObservableList<LexRecord> lexRecords = FXCollections.observableArrayList();
        List<LexRecord> list = lexical.getTableManager().getLexRecords();
        lexRecords.addAll(list);
        return lexRecords;
    }

    private ObservableList<IdRecord> getIds(LexicalAnalyzer lexical) {
        ObservableList<IdRecord> idRecords = FXCollections.observableArrayList();
        idRecords.addAll(lexical.getTableManager().getIdRecords());
        return idRecords;
    }

    private ObservableList<ConRecord> getCons(LexicalAnalyzer lexical) {
        ObservableList<ConRecord> conRecords = FXCollections.observableArrayList();
        conRecords.addAll(lexical.getTableManager().getConRecords());
        return conRecords;
    }

    class CallbackImpl implements Callback {
        @Override
        public void cin(String var, HashMap<String, Pair<String, String>> idns) {
            cinWindow(var, idns);
        }
    }

    private void initLexicalAnalyzer() {
        errors.setText("");
        file.write(textArea.getText());
        try {
            lexicalError = null;
            LexicalAnalyzer lexical = new LexicalAnalyzer(file);
            lexical.start();

            errors();
//            setTableLex();
//            setTableId();
//            setTableCon();
            SyntaxPrecedenceTableAnalyzer syntax
                    = new SyntaxPrecedenceTableAnalyzer(lexical.getTableManager().getLexRecords(),
                                                        lexical.getLexDB(),
                                                        lexical.getTableManager().getIdRecords(),
                                                        new CallbackImpl());
            syntax.start();
            errors.setText("Successfully!");
            System.out.println("Successfully!");
        } catch (LexicalError lexicalError) {
            System.out.println(lexicalError.getMessage() + "\nState=" + lexicalError.getState());
            errors.setText(lexicalError.getMessage() + "\nState=" + lexicalError.getState());
        } catch (SyntaxError syntaxError) {
            System.out.println(syntaxError.getMessage());
            errors.setText(syntaxError.getMessage());
        }

    }

    TextField textField;
    Stage varInput;

    public void cinWindow(String var, HashMap<String, Pair<String, String>> idns) {
        HBox hBox = new HBox();
        Label label = new Label("Enter " + var + " :");
        textField = new TextField();
        Button button = new Button("Button");
        button.setOnAction(e -> cin(var, idns));
        hBox.getChildren().addAll(label, textField, button);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(hBox);
        Scene scene = new Scene(stackPane);

        varInput = new Stage();
        varInput.setTitle("window");
        varInput.setScene(scene);
        varInput.showAndWait();
    }

    private void cin(String var, HashMap<String, Pair<String, String>> idns) {
        String value = textField.getText();
        idns.put(var, new Pair<>("int", value));
        varInput.close();
    }

    private void setTableLex(LexicalAnalyzer lexical) {
        TableView<LexRecord> table;
        TableColumn<LexRecord, Integer> lexIdColumn = new TableColumn<>("№");
        lexIdColumn.setMinWidth(20);
        lexIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<LexRecord, Integer> lexLineColumn = new TableColumn<>("№ рядку");
        lexLineColumn.setMinWidth(20);
        lexLineColumn.setCellValueFactory(new PropertyValueFactory<>("line"));

        TableColumn<LexRecord, Integer> lexLexColumn = new TableColumn<>("Лексема");
        lexLexColumn.setMinWidth(100);
        lexLexColumn.setCellValueFactory(new PropertyValueFactory<>("lex"));

        TableColumn<LexRecord, Integer> lexKodColumn = new TableColumn<>("Код");
        lexKodColumn.setMinWidth(20);
        lexKodColumn.setCellValueFactory(new PropertyValueFactory<>("kod"));

        TableColumn<LexRecord, Integer> lexKodIdConColumn = new TableColumn<>("Код ID/CON");
        lexKodIdConColumn.setMinWidth(20);
        lexKodIdConColumn.setCellValueFactory(new PropertyValueFactory<>("kodIdCon"));

        table = new TableView<>();

        table.setItems(getLexes(lexical));
        table.getColumns().addAll(lexIdColumn, lexLineColumn, lexLexColumn, lexKodColumn, lexKodIdConColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        Scene scene = new Scene(vBox);
        Stage window1 = new Stage();
        window1.setScene(scene);
        window1.show();
    }

    private void setTableId(LexicalAnalyzer lexical) {
        TableView<IdRecord> table;
        TableColumn<IdRecord, Integer> idKodColumn = new TableColumn<>("Код");
        idKodColumn.setMinWidth(20);
        idKodColumn.setCellValueFactory(new PropertyValueFactory<>("kod"));

        TableColumn<IdRecord, String> idLexColumn = new TableColumn<>("Ід");
        idLexColumn.setMinWidth(100);
        idLexColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<IdRecord, String> idTypeColumn = new TableColumn<>("Тип");
        idTypeColumn.setMinWidth(100);
        idTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<IdRecord, String> idValueColumn = new TableColumn<>("Значення");
        idValueColumn.setMinWidth(100);
        idValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        table = new TableView<>();
        table.setItems(getIds(lexical));
        table.getColumns().addAll(idKodColumn, idLexColumn, idTypeColumn, idValueColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        Scene scene = new Scene(vBox);
        Stage window1 = new Stage();
        window1.setScene(scene);
        window1.show();

    }

    private void setTableCon(LexicalAnalyzer lexical) {
        TableView<ConRecord> table;
        TableColumn<ConRecord, Integer> conKodColumn = new TableColumn<>("Код");
        conKodColumn.setMinWidth(20);
        conKodColumn.setCellValueFactory(new PropertyValueFactory<>("kod"));

        TableColumn<ConRecord, String> conLexColumn = new TableColumn<>("Лексема");
        conLexColumn.setMinWidth(100);
        conLexColumn.setCellValueFactory(new PropertyValueFactory<>("lex"));


        table = new TableView<>();
        table.setItems(getCons(lexical));
        table.getColumns().addAll(conKodColumn, conLexColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        Scene scene = new Scene(vBox);
        Stage window1 = new Stage();
        window1.setScene(scene);
        window1.show();

    }

    private void numberLines(String str) {
        int k = textArea.getText().split("\n").length;
        str = "";
        for (int i = 1; i <= k; i++)
            str += i + "\n";
        numberLines.setText(str);
    }

    public void errors() throws LexicalError {
        if (lexicalError != null)
            throw lexicalError;
    }

    public static void setError(LexicalError lexicalErrorBuf) {
        lexicalError = lexicalErrorBuf;
    }
}
