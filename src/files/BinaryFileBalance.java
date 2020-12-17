// Last Modified 12.17.2020
package files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * For serializable variable for binary file.
 *
 * @author Pk Boonpeng
 * @since 04.30.2019
 * @version 6th 12.10.2020
 */
public class BinaryFileBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Can be changed without using method
     */
    public double value;

    /**
     * Reads the data from the filename.
     *
     * @param filename File name.
     * @return Object IOList
     * @throws Exception If file error is found.
     */
    public static Object read(String filename) throws Exception {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("files/" + filename + ".dat"));) {
            return input.readObject();
        }
    }

    /**
     * Writes the serializable variable to a file.
     *
     * @param balance Serializable variable
     * @param filename File name
     * @throws Exception If file error is found.
     */
    public static void write(Serializable balance, String filename) throws Exception {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("files/" + filename + ".dat"));) {
            output.writeObject(balance);
        }
    }
}
