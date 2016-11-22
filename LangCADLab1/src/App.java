import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by pyvov on 02.10.2016.
 */
public class App extends Application {
    Stage window;
    FileManager file= LexicalAnalyzer.getFile();
    TextArea textArea=new TextArea(file.read());
    //TextArea numberLines=new TextArea();
    Label numberLines= new Label();
    TextArea errors = new TextArea();
    static LexicalError lexicalError;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Лексичний+Семантичний аналізатор. Пивовар Назарій Вар 10 група ТР-41");
        Button buttonStart=new Button("Почати");
        buttonStart.setOnAction(e ->initLexicalAnalyzer());
        numberLines.setMinWidth(30);
        numberLines.setPadding(new Insets(7,0,0,0));
        textArea.textProperty().addListener(e->numberLines(textArea.getText()));
        textArea.setMinSize(400,450);
        textArea.setStyle("-fx-font-size: 14px");
        numberLines.setStyle("-fx-font-size: 14px;" +
                "-fx-alignment: center");
        numberLines(textArea.getText());
        errors.setMaxHeight(85);
        VBox vBox = new VBox();
       // vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(textArea,errors);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(numberLines,vBox, buttonStart);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(hBox);


        Scene scene = new Scene(stackPane);
        window.setMinWidth(550);
        window.setMinHeight(580);
        window.setScene(scene);
        window.show();
    }

    private ObservableList<LexRecord> getLexes(){
        ObservableList<LexRecord> lexRecords = FXCollections.observableArrayList();
        List<LexRecord> list = LexicalAnalyzer.getTableManager().getLexRecords();
        lexRecords.addAll(list);
        return lexRecords;
    }
    private ObservableList<IdRecord> getIds(){
        ObservableList<IdRecord> idRecords = FXCollections.observableArrayList();
        idRecords.addAll(LexicalAnalyzer.getTableManager().getIdRecords());
        return idRecords;
    }
    private ObservableList<ConRecord> getCons(){
        ObservableList<ConRecord> conRecords = FXCollections.observableArrayList();
        conRecords.addAll(LexicalAnalyzer.getTableManager().getConRecords());
        return conRecords;
    }
    private void initLexicalAnalyzer(){
        errors.setText("");
        file.write(textArea.getText());
        try {
            lexicalError=null;
            LexicalAnalyzer.start();
            errors();
//            setTableLex();
//            setTableId();
//            setTableCon();
            SyntaxAvtomatAnalyzer.start();
            errors.setText("Successfully!");
        } catch (LexicalError lexicalError) {
            errors.setText(lexicalError.getMessage()+"\nState="+lexicalError.getState());
        } catch (SyntaxError syntaxError) {
            System.out.println(syntaxError.getMessage());
            errors.setText(syntaxError.getMessage());
        }

    }
    private void setTableLex(){
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

        table.setItems(getLexes());
        table.getColumns().addAll(lexIdColumn, lexLineColumn, lexLexColumn, lexKodColumn, lexKodIdConColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        Scene scene = new Scene(vBox);
        Stage window1=new Stage();
        window1.setScene(scene);
        window1.show();
    }
    private void setTableId(){
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
        table.setItems(getIds());
        table.getColumns().addAll(idKodColumn, idLexColumn, idTypeColumn, idValueColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        Scene scene = new Scene(vBox);
        Stage window1=new Stage();
        window1.setScene(scene);
        window1.show();

    }
    private void setTableCon(){
        TableView<ConRecord> table;
        TableColumn<ConRecord, Integer> conKodColumn = new TableColumn<>("Код");
        conKodColumn.setMinWidth(20);
        conKodColumn.setCellValueFactory(new PropertyValueFactory<>("kod"));

        TableColumn<ConRecord, String> conLexColumn = new TableColumn<>("Лексема");
        conLexColumn.setMinWidth(100);
        conLexColumn.setCellValueFactory(new PropertyValueFactory<>("lex"));


        table = new TableView<>();
        table.setItems(getCons());
        table.getColumns().addAll(conKodColumn, conLexColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        Scene scene = new Scene(vBox);
        Stage window1=new Stage();
        window1.setScene(scene);
        window1.show();

    }
    private void numberLines(String str){
        int k=textArea.getText().split("\n").length;
        str ="";
        for(int i=1;i<=k;i++)
            str+=i+"\n";
        numberLines.setText(str);
    }
    public void errors() throws LexicalError{
        if(lexicalError!=null)
                    throw lexicalError;
    }
    public static void setError(LexicalError lexicalErrorBuf){
        lexicalError = lexicalErrorBuf;
    }
}
