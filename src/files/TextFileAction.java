// Last Modified 12.17.2020
package files;

import obj.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class is for menu actions, involving with files. Such as getting a file
 * name, creating header for a TableView, creating new file, opening an existing
 * file, saving a file, loading a file to the current one.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class TextFileAction {

    /**
     * Default constructor. Omits implementation
     */
    public TextFileAction() {

    }

    /**
     * Getting a file name.
     *
     * @return file name (YYYYMMDD)
     */
    public String getFilename(TextField yearTf, TextField monthTf, TextField dayTf) {
        String y = "", m = "", d = "";
        if (!yearTf.getText().isEmpty()) {
            y = yearTf.getText();
        }
        if (!monthTf.getText().isEmpty()) {
            m = monthTf.getText();
        }
        if (!dayTf.getText().isEmpty()) {
            d = dayTf.getText();
        }
        return (String) (y + m + d);
    }

    /**
     * Action for importBtn. Acts mostly like openBtnAction, but has a new
     * ArrayList for loading the loaded file. And the current balance will be
     * added by the loaded data.
     *
     * @throws FileNotFoundException If file is not found.
     */
    public void importBtnAction(BinaryFileBalance balance, TextField balanceTf,
            TextField yearTf, TextField monthTf, TextField dayTf,
            TableView right, TableColumn dateCol, TableColumn cateCol, TableColumn amountCol,
            ArrayList<IOList> lists, Label errorLine)
            throws FileNotFoundException {

        String filename = getFilename(yearTf, monthTf, dayTf);

        ArrayList<IOList> loadList = new ArrayList<>();

        if (new File("files/" + filename + ".txt").exists()) {
            try (
                    Scanner read = new Scanner(new File("files/" + filename + ".txt"));) {
                while (read.hasNext()) {
                    String date = read.next();
                    String cate = read.next();
                    String cost = read.next();
                    String note = read.next();
                    if (right.getItems().isEmpty()) {
                    }

                    if (cost.charAt(0) == '-') {
                        cost = cost.substring(1);
                        lists.add(new ExpenseList(date, cate, cost, note));
                        loadList.add(new ExpenseList(date, cate, cost, note));
                    } else {
                        lists.add(new IncomeList(date, cate, cost, note));
                        loadList.add(new IncomeList(date, cate, cost, note));
                    }
                }
                right.getItems().addAll(loadList);
                dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
                cateCol.setCellValueFactory(new PropertyValueFactory<>("category"));
                amountCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
            }
        } else {
            errorLine.setText("File not found.");
        }

        try {
            BinaryFileBalance data = (BinaryFileBalance) BinaryFileBalance.read(filename);
            balance.value = Double.valueOf(balanceTf.getText()) + data.value;
            balanceTf.setText(String.valueOf(balance.value));
        } catch (Exception ex) {
            errorLine.setText(ex.getMessage());
        }

    }

    /**
     * Action for newBtn. Reinitialize the program.
     */
    public void newBtnAction(BinaryFileBalance balance, TextField balanceTf, TextField yearTf,
            TextField monthTf, TextField dayTf, TextField costTf,
            TextField noteTf, ToggleButton inExBtn, ComboBox incomeCombo,
            TableView right, ArrayList<IOList> lists, Label errorLine) {
        // reset balance
        balance.value = 0;
        balanceTf.setText(String.valueOf(balance.value));
        // empty all TextFields
        yearTf.setText("");
        monthTf.setText("");
        dayTf.setText("");
        costTf.setText("");
        noteTf.setText("");
        // deselect the inExBtn
        inExBtn.setSelected(false);
        incomeCombo.getSelectionModel().selectFirst();
        // clear the ArrayList
        lists.clear();
        // clear the TableView and set header
        right.getItems().clear();
        // reset errorLine
        errorLine.setText("");
    }

    /**
     * Action for openBtn. Gets file name from the dateLine by getFilename()
     * method. Initializes the program. Opens existing files, both text file and
     * binary file. Reads the text file and differentiates between IncomeList
     * and ExpenseLists. Puts all the lists to the IOList ArrayList and shows it
     * on the TableView. Reads the binary file to get the file's balance.
     *
     * @throws FileNotFoundException If file is not found.
     */
    public void openBtnAction(TextField balanceTf, TextField yearTf, TextField monthTf,
            TextField dayTf, TableView right, ArrayList<IOList> lists,
            Label errorLine) throws FileNotFoundException {

        String filename = getFilename(yearTf, monthTf, dayTf);

        lists.clear();
        right.getItems().clear();
        if (new File("files/" + filename + ".txt").exists()) {
            try (
                    Scanner read = new Scanner(new File("files/" + filename + ".txt"));) {
                while (read.hasNext()) {
                    String date = read.next();
                    String cate = read.next();
                    String cost = read.next();
                    String note = read.next();
                    if (cost.charAt(0) == '-') {
                        cost = cost.substring(1);
                        lists.add(new ExpenseList(date, cate, cost, note));
                    } else {
                        lists.add(new IncomeList(date, cate, cost, note));
                    }
                }
                right.getItems().addAll(lists);
            }
        } else {
            errorLine.setText("File not found.");
        }

        try {
            BinaryFileBalance data = (BinaryFileBalance) BinaryFileBalance.read(filename);
            balanceTf.setText(String.valueOf(data.value));
        } catch (Exception ex) {
            errorLine.setText(ex.getMessage());
        }
    }

    /**
     * Action for saveBtn. Gets file name from the dateLine by getFilename()
     * method. Writes all the IOList objects to the text file, but not the
     * deleted objects. Also writes value of balance to the binary file.
     */
    public void saveBtnAction(BinaryFileBalance balance, TextField balanceTf, TextField yearTf,
            TextField monthTf, TextField dayTf, TableView right, ToggleButton sortedBtn,
            ArrayList<IOList> lists, Label errorLine) {

        String filename = getFilename(yearTf, monthTf, dayTf);

        try (FileWriter fw = new FileWriter("files/" + filename + ".txt");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter output = new PrintWriter(bw);) {
            if (sortedBtn.isSelected()) {
                ArrayList<IOList> sortedLists = (ArrayList<IOList>) lists.clone();
                Collections.sort(sortedLists);
                for (IOList l : sortedLists) {
                    output.println(l.toFile());
                }
            } else {
                for (IOList l : lists) {
                    output.println(l.toFile());
                }
            }
        } catch (IOException ex) {
            errorLine.setText(ex.getMessage());
        }

        try {
            BinaryFileBalance.write(balance, filename);
        } catch (Exception ex) {
            errorLine.setText(ex.getMessage());
        }
    }
}
