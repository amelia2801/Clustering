/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mst;
import java.sql.Connection;
import mst.DbConn;

/**
 *
 * @author Anasthasia
 */
public class DbOperation {
    private static Connection connection;
	
    public DbOperation(){
        connection = DbConn.getConnection();
    }
        
    public void PercentHPSPenawaran(){
        
        
    }
}
