// Last Modified 12.17.2020
package obj;

import validate.*;

/**
 * Abstract class for IncomeList and ExpenseList
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public abstract class IOList
        implements Comparable<IOList>, ValidateInfo {

    /**
     * Stores category.
     */
    protected String category;

    /**
     * Stores the amount of money.
     */
    protected String cost;

    /**
     * Stores date.
     */
    protected String date;

    /**
     * Stores cost.
     */
    protected String note;

    /**
     * Default Constructor. Sets all data to default value.
     */
    public IOList() {
        try {
            setDate("");
            setCategory("");
            setCost("");
            setNote("[NO_NOTE]");
        } catch (InvalidInfoException ex) {

        }
    }

    /**
     * Second Constructor. Assigns date, category, and cost with the parameters
     * below. Sets note and deleted to default.
     *
     * @param date Stores date
     * @param category Stores category
     * @param cost Stores the amount of money
     */
    public IOList(String date, String category, String cost) {
        try {
            setDate(date);
            setCategory(category);
            setCost(cost);
            setNote("[NO_NOTE]");
        } catch (InvalidInfoException ex) {

        }
    }

    /**
     * Third Constructor. Assigns date, category, cost, and note with the
     * parameters below. Sets deleted to default.
     *
     * @param date Stores date
     * @param category Stores category
     * @param cost Stores the amount of money
     * @param note Stores note
     */
    public IOList(String date, String category, String cost, String note) {
        try {
            setDate(date);
            setCategory(category);
            setCost(cost);
            setNote(note);
        } catch (InvalidInfoException ex) {

        }
    }

    /**
     * Checks the length of DD and date of each month. dataM = 01, 03, 05, 07,
     * 08, 10, 12 have [0,31] days. dataM = 04, 06, 09, 11 have [0,30] days.
     * dataM = 02 has 29 days if yearTf % 4 = 0. Otherwise, 28 days.
     *
     * @param dataY Year.
     * @param dataM Month.
     * @param dataD Day.
     * @throws InvalidInfoException If invalid input is found.
     */
    @Override
    public void checkDay(String dataY, String dataM, String dataD) throws InvalidInfoException {

        // check if there any special character
        checkSpecialChar(dataD, "DD");

        if (dataD.length() < 2 || dataD.length() > 2) {
            throw new InvalidInfoException("DD must have 2 digits.");
        }

        switch (dataM) {
            case "01":
            case "03":
            case "05":
            case "07":
            case "08":
            case "10":
            case "12":
                if (Integer.valueOf(dataD) < 0 || Integer.valueOf(dataD) > 31) {
                    throw new InvalidInfoException("DD is in the range of [0,31]");
                }
                break;
            case "02":
                if ((Integer.valueOf(dataY) % 4) == 0) {
                    if (Integer.valueOf(dataD) < 0 || Integer.valueOf(dataD) > 29) {
                        throw new InvalidInfoException("DD is in the range of [0,29]");
                    }
                } else {
                    if (Integer.valueOf(dataD) < 0 || Integer.valueOf(dataD) > 28) {
                        throw new InvalidInfoException("DD is in the range of [0,28]");
                    }
                }
                break;
            default:
                if (Integer.valueOf(dataD) < 0 || Integer.valueOf(dataD) > 30) {
                    throw new InvalidInfoException("DD is in the range of [0,30]");
                }
                break;
        }
    }

    /**
     * Checks input by finding letter and white space. Throws
     * InvalidInfoException if the input is not valid
     *
     * @param input For date and cost
     * @throws InvalidInfoException If invalid input is found.
     */
    @Override
    public void checkLetterAndWhiteSpace(String input) throws InvalidInfoException {
        int numChar = 0, numWs = 0;

        for (int i = 0; i < input.length(); i++) {
            if (Character.isLetter(input.charAt(i))) {
                numChar++;
            }
            if (Character.isWhitespace(input.charAt(i))) {
                numWs++;
            }
        }
        if (numChar > 0) {
            throw new InvalidInfoException("must have ONLY digits.");
        }
        if (numWs > 0) {
            throw new InvalidInfoException("must NOT have white space.");
        }
    }

    /**
     * Checks if there any special character in every box but the note one.
     *
     * @param data Input.
     * @param message The error message.
     * @throws InvalidInfoException If invalid input is found.
     */
    @Override
    public void checkSpecialChar(String data, String message) throws InvalidInfoException {
        int numSp = 0;

        for (int i = 0; i < data.length(); i++) {
            if (!Character.isLetterOrDigit(data.charAt(i)) || data.charAt(i) == '.') {
                numSp++;
            }
        }
        if (numSp > 0) {
            throw new InvalidInfoException(message + " must have ONLY digits.");
        }
    }

    /**
     * Checks the length and quantity limit of year and month.
     *
     * @param data Input.
     * @param length Length limit.
     * @param quantity Quantity limit.
     * @param message The error message.
     * @throws InvalidInfoException If invalid input is found.
     */
    @Override
    public void checkYM(String data, int length, int quantity, String message) throws InvalidInfoException {

        // check if there any special character
        checkSpecialChar(data, message);

        if (data.length() < length || data.length() > length) {
            throw new InvalidInfoException(message + " must have " + length + " digits.");
        }

        if (Integer.valueOf(data) < 0 || Integer.valueOf(data) > quantity) {
            throw new InvalidInfoException(message + " is in the range of [0," + quantity + "]");
        }
    }

    /**
     * Sorts date, category, note, and cost.
     *
     * @param o IOList
     * @return int
     */
    @Override
    public int compareTo(IOList o) {
        if (getDate().equalsIgnoreCase(o.getDate())) {
            if (getCategory().equalsIgnoreCase(o.getCategory())) {
                if (getNote().equalsIgnoreCase(o.getNote())) {
                    return getCost().compareToIgnoreCase(o.getCost());
                } else {
                    return getNote().compareToIgnoreCase(o.getNote());
                }
            } else {
                return getCategory().compareToIgnoreCase(o.getCategory());
            }
        } else {
            return getDate().compareToIgnoreCase(o.getDate());
        }
    }

    /**
     * Getter for category.
     *
     * @return String category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter for cost.
     *
     * @return String cost
     */
    public String getCost() {
        return cost;
    }

    /**
     * Getter for date.
     *
     * @return String date
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter for note.
     *
     * @return String note
     */
    public String getNote() {
        return note;
    }

    /**
     * Setter for category.
     *
     * @param category Stores category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Setter for cost. Using checkLetterAndWhiteSpace to find letters and white
     * spaces.
     *
     * @param cost Stores the amount of money
     * @throws InvalidInfoException If invalid data is found
     */
    public void setCost(String cost) throws InvalidInfoException {
        try {
            checkLetterAndWhiteSpace(cost);
            this.cost = cost;
        } catch (InvalidInfoException ex) {
            throw new InvalidInfoException("The cost " + ex.getMessage());
        }
    }

    /**
     * Setter for date. Using checkLetterAndWhiteSpace to find letters and white
     * spaces.
     *
     * @param date Stores date
     * @throws InvalidInfoException If invalid data is found
     */
    public void setDate(String date) throws InvalidInfoException {
        try {
            checkLetterAndWhiteSpace(date);
            this.date = date;
        } catch (InvalidInfoException ex) {
            throw new InvalidInfoException("DD " + ex.getMessage());
        }
    }

    /**
     * Setter for note.
     *
     * @param note Stores note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Set String format for file writing
     *
     * @return String for file
     */
    public String toFile() {
        return String.format("%s %1s %1s %1s", date, category, cost, note);
    }

    /**
     * Set String conditions and formats.
     *
     * @return String of that condition
     */
    @Override
    public String toString() {
        if (note.equals("[NO_NOTE]")) {
            return String.format("%s %1s %1s %1s", date, category, cost);
        } else {
            return String.format("%s %1s %1s %1s", date, category + "(" + note + ")", cost);
        }
    }
}
