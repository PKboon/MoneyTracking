// Last Modified 12.17.2020
package obj;

/**
 * This subclass is for expense. Extends IOList abstract class. Changes cost to
 * negative value.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class ExpenseList extends IOList {

    /**
     * Default Constructor. Calls IOList's constructor
     */
    public ExpenseList() {
        super();
    }

    /**
     * Second Constructor. Calls IOList's second constructor but change cost to
     * negative value.
     *
     * @param date Stores date.
     * @param category Stores category.
     * @param cost Stores the amount of money.
     */
    public ExpenseList(String date, String category, String cost) {
        super(date, category, "-" + cost);
    }

    /**
     * Third Constructor. Calls IOList's third constructor but change cost to
     * negative value.
     *
     * @param date Stores date.
     * @param category Stores category.
     * @param cost Stores the amount of money in negative.
     * @param note Stores note.
     */
    public ExpenseList(String date, String category, String cost, String note) {
        super(date, category, "-" + cost, note);
    }
}
