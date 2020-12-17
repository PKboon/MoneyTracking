// Last Modified 12.17.2020
package obj;

/**
 * This subclass is for income. Extends IOList abstract class.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class IncomeList extends IOList {

    /**
     * Default Constructor. Calls IOList's constructor
     */
    public IncomeList() {
        super();
    }

    /**
     * Second Constructor. Calls IOList's second constructor but change cost to
     * negative value.
     *
     * @param date Stores date
     * @param category Stores category
     * @param cost Stores the amount of money
     */
    public IncomeList(String date, String category, String cost) {
        super(date, category, cost);
    }

    /**
     * Third Constructor. Calls IOList's third constructor but change cost to
     * negative value.
     *
     * @param date Stores date
     * @param category Stores category
     * @param cost Stores the amount of money
     * @param note Stores note
     */
    public IncomeList(String date, String category, String cost, String note) {
        super(date, category, cost, note);
    }
}
