/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mst;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import java.text.ParseException;
import mst.*;

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
        //db.generateTestData(10);
        //db.generateTestDataFromFile("F:\\Amelia\\Documents\\Teknik Informatika 2010\\Semester 7\\IF4071 Machine Learning\\Clustering\\Clustering\\MST\\src\\vertices_pengawasan_2.txt");
        ArffParser p = new ArffParser("F:\\Amelia\\Documents\\Teknik Informatika 2010\\Semester 7\\IF4071 Machine Learning\\Clustering\\Clustering\\MST\\src\\playtennis.arff");
        System.out.println(p.Ex.getData().toString());
        db.CountWeightPlayTennis(p.Ex);
    }
}
