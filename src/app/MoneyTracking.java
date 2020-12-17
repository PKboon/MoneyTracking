// Last Modified 12.17.2020
package app;

import obj.*;
import files.*;
import menu.*;
import validate.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * A program for recording income and expenses.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class MoneyTracking extends Application {

    // final variable for space
    final int S_SPACE = 10;
    final int MAX_WIDTH = 140;
    final int MENU_BTN_WIDTH = 65;

    // this box groups list view part and the button part
    HBox all = new HBox();

    // the list view part is on the right
    TableView<IOList> right = new TableView<>();
    TableColumn<IOList, String> dateCol = new TableColumn<>("Date");
    TableColumn<IOList, String> cateCol = new TableColumn<>("Category");
    TableColumn<IOList, String> amountCol = new TableColumn<>("Amount");
    // the button part is on the left
    VBox left = new VBox(S_SPACE);

    // menu line has 4 actions: New, Open, Save, Import
    TextFileAction menuAction = new TextFileAction();
    HBox menuLine = new HBox(S_SPACE);
    Button newBtn = new Button("New");
    Button openBtn = new Button("Open");
    Button saveBtn = new Button("Save");
    Button importBtn = new Button("Import");

    // sort catalog
    HBox sortedLine = new HBox(S_SPACE);
    ToggleButton sortBtn = new ToggleButton("Sort");
    ToggleGroup showGroup = new ToggleGroup();
    ToggleButton showIncome = new ToggleButton("Income Only");
    ToggleButton showExpense = new ToggleButton("Expense Only");

    // balance part
    VBox balanceLine = new VBox();
    Label balanceLbl = new Label("Balance");
    TextField balanceTf = new TextField();
    // serializable variable for binary file
    BinaryFileBalance balance = new BinaryFileBalance();

    // date part
    HBox dateLine = new HBox(5);
    Label yearLbl = new Label("YYYY:");
    TextField yearTf = new TextField();
    Label monthLbl = new Label("  MM:");
    TextField monthTf = new TextField();
    Label dayLbl = new Label("  DD:");
    TextField dayTf = new TextField();

    // selection part for selecting income and expense
    HBox selectionLine = new HBox(S_SPACE);
    ListAction listAction = new ListAction();
    // shows expense if selected, shows income if not.
    ToggleButton inExBtn = new ToggleButton("Income");
    // ArrayList for storing all income and expense lists
    ArrayList<IOList> lists = new ArrayList<>();
    // ComboBox for income category
    ComboBox incomeCombo = new ComboBox();
    ObservableList<String> incomeCate = FXCollections.observableArrayList(
            "Choose One", "Dad", "Gift", "Mom", "Scholarship",
            "Work", "Others");
    // ComboBox for expense category
    ComboBox expenseCombo = new ComboBox();
    ObservableList<String> expenseCate = FXCollections.observableArrayList(
            "Choose One", "Clothing", "Eating", "Education",
            "Electricity", "Gas", "Internet",
            "Phone", "Rent", "Traveling", "Others");

    // for the amount of money
    HBox costLine = new HBox(S_SPACE);
    Label costLbl = new Label("\t\t\t     $ ");
    TextField costTf = new TextField();

    // note part
    HBox noteLine = new HBox(S_SPACE);
    Label noteLbl = new Label("\t   Note ");
    TextField noteTf = new TextField();

    // for adding and removing lists
    HBox recordLine = new HBox(S_SPACE);
    Button addBtn = new Button("   Add   ");
    Button removeBtn = new Button("Remove");

    // shows error message
    Label errorLine = new Label("");

    // for background changing
    Circle color = new Circle(S_SPACE);
    int counter = 0;

    // run
    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException {

        // Initialize settings
        setting();

        // Sort data
        sortBtn.setOnAction(e -> listAction.sortBtnAction(right, sortBtn, lists));

        // Toggle button
        inExBtn.setOnAction(e -> listAction.inExBtnAction(costTf, incomeCombo, expenseCombo, inExBtn, selectionLine));

        // Menu buttons
        // newBtn for a new file
        newBtn.setOnAction(e -> menuAction.newBtnAction(balance, balanceTf, yearTf, monthTf, dayTf, costTf, noteTf, inExBtn,
                incomeCombo, right, lists, errorLine));
        // openBtn for opening an existing file
        openBtn.setOnAction(e -> {
            try {
                menuAction.openBtnAction(balanceTf, yearTf, monthTf, dayTf, right, lists, errorLine);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MoneyTracking.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        // saveBtn for saving the current file
        saveBtn.setOnAction(e -> menuAction.saveBtnAction(balance, balanceTf, yearTf, monthTf, dayTf, right, sortBtn, lists, errorLine));
        // importBtn for importing an existing file to the current file
        importBtn.setOnAction(e -> {
            try {
                menuAction.importBtnAction(balance, balanceTf, yearTf, monthTf, dayTf, right, dateCol, cateCol, amountCol, lists, errorLine);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MoneyTracking.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });

        // Record buttons
        // addBtn for adding a new list of income or expense
        addBtn.setOnAction(e -> {
            try {
                listAction.addBtnAction(incomeCombo, expenseCombo, inExBtn, balance, balanceTf, yearTf,
                        monthTf, dayTf, costTf, noteTf, right, dateCol, cateCol, amountCol,
                        lists, errorLine);
            } catch (InvalidInfoException ex) {
                Logger.getLogger(MoneyTracking.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        // removeBtn for removing the unwanted list
        removeBtn.setOnAction(e -> listAction.removeBtnAction(balance, balanceTf, right, lists, errorLine));

        // for showing just either sorted IncomeList or sorted ExpenseList
        showIncome.setOnAction(e -> listAction.showListAction(right, showGroup, showIncome, showExpense,
                lists, errorLine));
        showExpense.setOnAction(e -> listAction.showListAction(right, showGroup, showIncome, showExpense,
                lists, errorLine));

        // for changing background
        colorCircleAction();
        color.setOnMouseClicked(e -> {
            try {
                colorCircleAction();
            } catch (IOException ex) {
                Logger.getLogger(MoneyTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Scene scene = new Scene(all, 720, 500);
        stage.setMaxWidth(720);
        stage.setTitle("Money Tracking by PK");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Changes the background for every click. When counter reaches 8, set it
     * back to zero.
     */
    public void colorCircleAction() throws IOException {
        counter++;
        if (counter == 8) {
            counter = 0;
        }
        switch (counter) {
            case 0: //classic
                left.setStyle("-fx-background-color: #F5F5F5");
                balanceLbl.setTextFill(Color.BLACK);
                yearLbl.setTextFill(Color.BLACK);
                monthLbl.setTextFill(Color.BLACK);
                dayLbl.setTextFill(Color.BLACK);
                costLbl.setTextFill(Color.BLACK);
                noteLbl.setTextFill(Color.BLACK);
                errorLine.setTextFill(Color.RED);
                break;
            case 1: //yellow
                left.setStyle("-fx-background-image: url(file: src/img/yellow.jpg)");
                break;
            case 2: //white
                left.setStyle("-fx-background-image: url(file: src/img/white.png)");
                break;
            case 3: //purple
                left.setStyle("-fx-background-image: url(file: src/img/purple.png)");
                break;
            case 4: //pink
                left.setStyle("-fx-background-image: url(file: src/img/pink.png)");
                break;
            case 5: //blue
                left.setStyle("-fx-background-image: url(file: src/img/blue.jpg)");
                errorLine.setTextFill(Color.YELLOW);
                break;
            case 6: //green
                left.setStyle("-fx-background-image: url(file: src/img/green.jpg)");
                break;
            case 7: //black
                left.setStyle("-fx-background-image: url(file: src/img/black.png)");
                balanceLbl.setTextFill(Color.WHITE);
                yearLbl.setTextFill(Color.WHITE);
                monthLbl.setTextFill(Color.WHITE);
                dayLbl.setTextFill(Color.WHITE);
                costLbl.setTextFill(Color.WHITE);
                noteLbl.setTextFill(Color.WHITE);
                break;
        }

        try (FileWriter fw = new FileWriter("src/img/bg.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter output = new PrintWriter(bw);) {
            output.print(counter);
        }
    }

    /**
     * All about initialize setting and program's interface.
     */
    public void setting() throws FileNotFoundException {
        try (
                Scanner read = new Scanner(new File("src/img/bg.txt"));) {
            while (read.hasNext()) {
                counter = read.nextInt();
                counter--;
            }
        }

        inExBtn.setMaxWidth(75);
        inExBtn.setMinWidth(75);
        inExBtn.setTooltip(new Tooltip("Click for Expense List"));

        incomeCombo.setItems(incomeCate);
        incomeCombo.getSelectionModel().selectFirst();
        incomeCombo.setMinWidth(MAX_WIDTH);

        expenseCombo.setItems(expenseCate);
        expenseCombo.getSelectionModel().selectFirst();
        expenseCombo.setMinWidth(MAX_WIDTH);

        all.getChildren().addAll(left, right);

        right.setMinWidth(400);
        right.setMaxWidth(400);
        dateCol.setMaxWidth(99);
        dateCol.setMinWidth(99);
        cateCol.setMaxWidth(200);
        cateCol.setMinWidth(200);
        amountCol.setMaxWidth(99);
        amountCol.setMinWidth(99);
        right.getColumns().addAll(dateCol, cateCol, amountCol);

        left.setAlignment(Pos.CENTER);
        left.getChildren().addAll(menuLine, sortedLine, balanceLine, dateLine, selectionLine,
                costLine, noteLine, recordLine, errorLine, color);

        menuLine.getChildren().addAll(newBtn, openBtn, saveBtn, importBtn);
        menuLine.setAlignment(Pos.CENTER);
        newBtn.setMaxWidth(MENU_BTN_WIDTH);
        newBtn.setMinWidth(MENU_BTN_WIDTH);
        openBtn.setMaxWidth(MENU_BTN_WIDTH);
        openBtn.setMinWidth(MENU_BTN_WIDTH);
        saveBtn.setMaxWidth(MENU_BTN_WIDTH);
        saveBtn.setMinWidth(MENU_BTN_WIDTH);
        importBtn.setMaxWidth(MENU_BTN_WIDTH);
        importBtn.setMinWidth(MENU_BTN_WIDTH);

        sortedLine.getChildren().addAll(showIncome, showExpense, sortBtn);
        sortedLine.setAlignment(Pos.CENTER);
        showIncome.setToggleGroup(showGroup);
        showExpense.setToggleGroup(showGroup);
        sortBtn.setMaxWidth(MENU_BTN_WIDTH);
        sortBtn.setMinWidth(MENU_BTN_WIDTH);
        showIncome.setMaxWidth(MENU_BTN_WIDTH * 2 - 27);
        showIncome.setMinWidth(MENU_BTN_WIDTH * 2 - 27);
        showExpense.setMaxWidth(MENU_BTN_WIDTH * 2 - 27);
        showExpense.setMinWidth(MENU_BTN_WIDTH * 2 - 27);

        balanceLine.getChildren().addAll(balanceLbl, balanceTf);
        balanceLine.setAlignment(Pos.CENTER);
        balanceLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));

        balanceTf.setMaxWidth(70);
        balanceTf.setEditable(false);
        balanceTf.setAlignment(Pos.CENTER);
        balanceTf.setText(String.valueOf(balance.value = 0));

        dateLine.getChildren().addAll(yearLbl, yearTf, monthLbl, monthTf, dayLbl, dayTf);
        yearLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));
        monthLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));
        dayLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));
        dateLine.setAlignment(Pos.CENTER);
        yearTf.setMaxWidth(50);
        monthTf.setMaxWidth(35);
        dayTf.setMaxWidth(35);
        yearTf.textProperty().addListener((ob, oldOb, newOb) -> {
            if (oldOb.length() < 4 && newOb.length() >= 4) {
                monthTf.requestFocus();
            }
        });
        monthTf.textProperty().addListener((ob, oldOb, newOb) -> {
            if (oldOb.length() < 2 && newOb.length() >= 2) {
                dayTf.requestFocus();
            }
        });
        dayTf.textProperty().addListener((ob, oldOb, newOb) -> {
            if (oldOb.length() < 2 && newOb.length() >= 2) {
                incomeCombo.requestFocus();
                expenseCombo.requestFocus();
            }
        });

        selectionLine.getChildren().addAll(inExBtn, incomeCombo);
        selectionLine.setAlignment(Pos.CENTER);

        costTf.setMaxWidth(MAX_WIDTH);
        costLine.getChildren().addAll(costLbl, costTf, new Label("          "));
        costLine.setAlignment(Pos.CENTER);
        costLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));

        noteTf.setMaxWidth(MAX_WIDTH);
        noteLine.getChildren().addAll(noteLbl, noteTf);
        noteLine.setAlignment(Pos.CENTER);
        noteLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));

        recordLine.setAlignment(Pos.CENTER);
        recordLine.getChildren().addAll(removeBtn, addBtn);

        errorLine.setTextFill(Color.RED);
        errorLine.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 14));

        color.setCenterX(5);
        color.setCenterY(5);
        color.setTranslateX(140);
        color.setTranslateY(25);
        color.setFill(Color.WHITE);
        color.setStroke(Color.GRAY);
        color.setOpacity(0.3);
        Tooltip bg = new Tooltip("Change Background");
        Tooltip.install(color, bg);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
