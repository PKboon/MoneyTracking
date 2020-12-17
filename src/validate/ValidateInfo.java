// Last Modified 12.17.2020
package validate;

/**
 * For throwing InvalidInfoException.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public interface ValidateInfo {

    /**
     * Checks the length of DD and number of days of each month. dataM = 01, 03,
     * 05, 07, 08, 10, 12 have [0,31] days. dataM = 04, 06, 09, 11 have [0,30]
     * days. dataM = 02 has 29 days if yearTf % 4 = 0. Otherwise, 28 days.
     *
     * @param dataY Year
     * @param dataM Month
     * @param dataD Day
     * @throws InvalidInfoException If invalid input is found.
     */
    public abstract void checkDay(String dataY, String dataM, String dataD) throws InvalidInfoException;

    /**
     * Validate input by finding letter and white space. Throws
     * InvalidInfoException if the input is not valid
     *
     * @param data For date and cost.
     * @throws InvalidInfoException If invalid input is found.
     */
    public abstract void checkLetterAndWhiteSpace(String data) throws InvalidInfoException;

    /**
     * Checks if there any special character in every box but the note one. The
     * reason that it cannot be in the validateInfo in IOList class is that date
     * form is YYYY/MM/DD and cost for ExpenseList is -cost
     *
     * @param data Input.
     * @param message The error message.
     * @throws InvalidInfoException If invalid input is found.
     */
    public abstract void checkSpecialChar(String data, String message) throws InvalidInfoException;

    /**
     * Checks the length and quantity limit of year and month.
     *
     * @param data Input.
     * @param length Length limit.
     * @param quantity Quantity limit.
     * @param message The error message.
     * @throws InvalidInfoException If invalid input is found.
     */
    public abstract void checkYM(String data, int length, int quantity, String message) throws InvalidInfoException;
}
