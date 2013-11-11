/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mst;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import java.text.ParseException;
import mst.DbConn;

/**
 *
 * @author Anasthasia
 */
public class MST {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        DbOperation db = new DbOperation();
        db.generateTestData(10);
    }
}
