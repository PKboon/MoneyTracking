// Last Modified 12.17.2020
package menu;

import files.*;
import obj.*;
import validate.*;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

/**
 * This class is for list actions. Such as sorting data, getting income lists
 * and expense lists, adding a list to an ArrayList, and removing the unwanted
 * list from the ArrayList
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class ListAction {

    /**
     * Default constructor. Omits implementation
     */
    public ListAction() {

    }

    /**
     * Action for addBtn. If the inExpBtn is selected, the actions will be for
     * ExpenseList. If not, the actions will be for IncomeList. Reset input
     * fields
     *
     * @see listAdd method
     */
    public void addBtnAction(ComboBox incomeCombo, ComboBox expenseCombo, ToggleButton inExBtn,
            BinaryFileBalance balance, TextField balanceTf, TextField yearTf,
            TextField monthTf, TextField dayTf, TextField costTf,
            TextField noteTf, TableView right, TableColumn dateCol, TableColumn cateCol, TableColumn amountCol, TableColumn noteCol,
            ArrayList<IOList> lists,
            Label errorLine) throws InvalidInfoException {

        if (inExBtn.isSelected()) {
            String exSelected = (String) expenseCombo.getSelectionModel().getSelectedItem();
            IOList newEx = new ExpenseList();
            listAdd(incomeCombo, expenseCombo, inExBtn, balance, balanceTf, yearTf, monthTf,
                    dayTf, costTf, noteTf, right, dateCol, cateCol, amountCol, noteCol, 
                    lists, errorLine, newEx, exSelected);
        } else {
            String inSelected = (String) incomeCombo.getSelectionModel().getSelectedItem();
            IOList newIn = new IncomeList();
            listAdd(incomeCombo, expenseCombo, inExBtn, balance, balanceTf, yearTf, monthTf,
                    dayTf, costTf, noteTf, right, dateCol, cateCol, amountCol, noteCol, 
                    lists, errorLine, newIn, inSelected);
        }
    }

    /**
     * Action for inExBtn. If the button is select, the ComboBox will be set for
     * the expense lists. If it is deselected, the ComboBox will be set for the
     * income lists.
     */
    public void inExBtnAction(TextField costTf, ComboBox incomeCombo, ComboBox expenseCombo,
            ToggleButton inExBtn, HBox selectionLine) {

        // reset the cost TextField and the ComboBoxes
        costTf.setText("");
        incomeCombo.getSelectionModel().selectFirst();
        expenseCombo.getSelectionModel().selectFirst();

        if (inExBtn.isSelected()) {
            inExBtn.setTooltip(new Tooltip("Click for Income"));
            inExBtn.setText("Expense");
            selectionLine.getChildren().set(1, expenseCombo);
        } else {
            inExBtn.setTooltip(new Tooltip("Click for Expense"));
            inExBtn.setText("Income");
            selectionLine.getChildren().set(1, incomeCombo);
        }
    }

    /**
     * Action for addBtnAction. Gets all variables for creating an IOList
     * object. Shows an error message if any.
     */
    public void listAdd(ComboBox incomeCombo, ComboBox expenseCombo, ToggleButton inExBtn,
            BinaryFileBalance balance, TextField balanceTf, TextField yearTf,
            TextField monthTf, TextField dayTf, TextField costTf,
            TextField noteTf, TableView right, TableColumn dateCol, TableColumn cateCol, TableColumn amountCol, TableColumn noteCol,
            ArrayList<IOList> lists,
            Label errorLine, IOList list, String category) {

        if (yearTf.getText().isEmpty()) {
            errorLine.setText("Enter YYYY.");
        } else if (monthTf.getText().isEmpty()) {
            errorLine.setText("Enter MM.");
        } else if (dayTf.getText().isEmpty()) {
            errorLine.setText("Enter DD.");
        } else if (category.equals("Choose One")) {
            errorLine.setText("Select a category.");
        } else if (costTf.getText().isEmpty()) {
            errorLine.setText("Enter the amount of income.");
        } else if (category.equals("Others") && noteTf.getText().isEmpty()) {
            errorLine.setText("Make a note.");
        } else if (noteTf.getText().contains(" ")) {
            errorLine.setText("Note cannot have space.");
        } else {
            try {
                list.checkYM(yearTf.getText(), 4, 9999, "YYYY");
                list.checkYM(monthTf.getText(), 2, 12, "MM");
                list.checkDay(yearTf.getText(), monthTf.getText(), dayTf.getText());
                list.checkSpecialChar(costTf.getText(), "The $ ");

                list.setDate(yearTf.getText() + "/"
                        + monthTf.getText() + "/"
                        + dayTf.getText());

                if (list instanceof IncomeList) {
                    list.setCost(costTf.getText());
                } else if (list instanceof ExpenseList) {
                    // if list is ExpenseList, change the cost to negative
                    list.setCost("-" + costTf.getText());
                }
                balance.value = Double.valueOf(balanceTf.getText()) + Double.parseDouble(list.getCost());
                balanceTf.setText(String.valueOf(balance.value));

                list.setCategory(category);
                
                if (!noteTf.getText().isEmpty()) {
                    list.setNote(noteTf.getText());
                }

                lists.add(list);
                right.getItems().add(list);
                dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
                cateCol.setCellValueFactory(new PropertyValueFactory<>("category"));
                amountCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
                noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
                errorLine.setText("");

                //reset input fields except date inputs
                incomeCombo.getSelectionModel().selectFirst();
                expenseCombo.getSelectionModel().selectFirst();
                costTf.setText("");
                noteTf.setText("");
            } catch (InvalidInfoException ex) {
                errorLine.setText(ex.getMessage());
            }
        }
    }

    /**
     * Action for removeBtn. When the list is removed, the balance will be
     * updated.
     */
    public void removeBtnAction(BinaryFileBalance balance, TextField balanceTf, TableView right,
            ArrayList<IOList> lists, Label errorLine) {
        // Get the selected line's index
        int index = right.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            // Calculate the balance
            balance.value = Double.valueOf(balanceTf.getText()) - Double.valueOf(lists.get(index).getCost());
            balanceTf.setText(String.valueOf(balance.value));
            // Set the deleted status of the index to true
            lists.remove(index);
            // Update the TableView
            right.getItems().remove(right.getSelectionModel().getSelectedItem());
            errorLine.setText("");
        }
    }

    /**
     * Action for showing just either sorted IncomeList or sorted ExpenseList
     * when one ToggleButton of the showGroup is selected. Shows normal lists if
     * none.
     */
    public void showListAction(TableView right, ToggleGroup showGroup, ToggleButton showIncome,
            ToggleButton showExpense, ArrayList<IOList> lists, Label errorLine) {

        // Clone the passing ArrayList first
        ArrayList<IOList> sortedList = (ArrayList<IOList>) lists.clone();
        // Then sort it
        Collections.sort(sortedList);
        right.getItems().clear();
        if (showGroup.getSelectedToggle() == showIncome) {
            // Show the sorted cloned ArrayList
            for (IOList l : sortedList) {
                if (l instanceof IncomeList) {
                    right.getItems().addAll(l);
                }
            }
        } else if (showGroup.getSelectedToggle() == showExpense) {
            // Show the sorted cloned ArrayList
            for (IOList l : sortedList) {
                if (l instanceof ExpenseList) {
                    right.getItems().addAll(l);
                }
            }
        } else {
            // Show the passing ArrayList
            for (IOList l : lists) {
                right.getItems().addAll(l);
            }
        }
    }

    /**
     * Action for sortBtn. Sorts date, category, then cost.
     */
    public void sortBtnAction(TableView right, ToggleButton sortBtn, ArrayList<IOList> lists) {

        // Clone the passing ArrayList first
        ArrayList<IOList> sortedDate = (ArrayList<IOList>) lists.clone();
        // Then sort it
        Collections.sort(sortedDate);
        right.getItems().clear();
        if (sortBtn.isSelected()) {
            sortBtn.setText("Unsort");
            // Show the sorted cloned ArrayList
            for (IOList l : sortedDate) {
                right.getItems().addAll(l);
            }
        } else {
            sortBtn.setText("Sort");
            // Show the passing ArrayList
            for (IOList l : lists) {
                right.getItems().addAll(l);
            }
        }
    }
}
