// Last Modified 12.17.2020
package validate;

/**
 * This class is for this recording program's exception.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class InvalidInfoException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * To be thrown when validateInfo found invalid input
     *
     * @param message The error message
     */
    public InvalidInfoException(String message) {
        super(message);
    }
}
