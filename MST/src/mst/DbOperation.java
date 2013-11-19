/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mst;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import model.Examples;
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
        
    public void PercentHPSPenawaran() throws ParseException{
        try{
            Statement s = connection.createStatement();
            String query ="SELECT \"id\", \"hps\", \"harga_penawaran\", \"delta_hps_penawaran\" FROM lelang;";
            ResultSet rs = s.executeQuery(query);
            ArrayList<Double> id = new ArrayList<Double>();
            ArrayList<Double> hps = new ArrayList<Double>();
            ArrayList<Double> delta_penawaran = new ArrayList<Double>();
            ArrayList<Double> percentage_col = new ArrayList<Double>();
            DecimalFormat df = new DecimalFormat("##.####");
            while(rs.next()){
                hps.add(rs.getDouble("hps"));
                delta_penawaran.add(rs.getDouble("delta_hps_penawaran"));
                id.add(rs.getDouble("id"));
            }
            double p = 0;
            for(int i = 0; i< hps.size(); i++){
                if(delta_penawaran.get(i) != 0){
                    p = (delta_penawaran.get(i)/hps.get(i))* 100;
                }else{
                    p = 0;
                }
                System.out.println(p);
                PreparedStatement ps = connection.prepareStatement
                        ("UPDATE \"lelang\" SET \"percent_delta_hps\"="+p+" WHERE \"id\"=" + id.get(i));
                ps.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void PercentPaguPenawaran() throws ParseException{
        try{
            Statement s = connection.createStatement();
            String query ="SELECT \"id\", \"pagu\", \"harga_penawaran\", \"delta_pagu_penawaran\" FROM lelang;";
            ResultSet rs = s.executeQuery(query);
            ArrayList<Double> id = new ArrayList<Double>();
            ArrayList<Double> pagu = new ArrayList<Double>();
            ArrayList<Double> delta_penawaran = new ArrayList<Double>();
            ArrayList<Double> percentage_col = new ArrayList<Double>();
            DecimalFormat df = new DecimalFormat("##.####");
            while(rs.next()){
                pagu.add(rs.getDouble("pagu"));
                delta_penawaran.add(rs.getDouble("delta_pagu_penawaran"));
                id.add(rs.getDouble("id"));
            }
            double p = 0;
            for(int i = 0; i< pagu.size(); i++){
                if(delta_penawaran.get(i) != 0){
                    p = (delta_penawaran.get(i)/pagu.get(i))* 100;
                }else{
                    p = 0;
                }
                System.out.println(p);
                PreparedStatement ps = connection.prepareStatement
                        ("UPDATE \"lelang\" SET \"percent_delta_pagu\"="+p+" WHERE \"id\"=" + id.get(i));
                ps.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void generateTestData(int limit){
        try{
            Statement s = connection.createStatement();
            String query ="SELECT \"id\", \"percent_delta_hps\" FROM lelang ORDER BY \"id\" LIMIT "+limit+";";
            ResultSet rs = s.executeQuery(query);
            ArrayList<Double> percentage = new ArrayList<Double>();
            while(rs.next()){
                percentage.add(rs.getDouble("percent_delta_hps"));
            }
            
            System.out.println("data yang akan digunakan: ");
            for(int i=0; i<percentage.size(); i++){
                System.out.println(percentage.get(i));
            }
            
            System.out.println("\npasangan yang terbentuk: ");
            System.out.println(limit);
            System.out.println((limit*(limit-1))/2);
            int j=0;
            double delta = 0;
            for(int i=0; i<percentage.size(); i++){
                j = i;
                while(j<percentage.size()){
                    if(i!=j){
                        System.out.print(i + " " +j);
                        delta = percentage.get(i) - percentage.get(j);
                        System.out.println(" "+delta);
                        j++;
                    }else{
                        j++;
                    }
                }   
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void generateTestDataFromFile(String filelocation){
        try{
            String currentLine;
            BufferedReader br = new BufferedReader(new FileReader(filelocation));
            ArrayList<Double> percentage = new ArrayList<Double>();
            while((currentLine = br.readLine()) != null){         
                percentage.add(Double.parseDouble(currentLine));
            }
            for(int i=0; i<percentage.size(); i++){
                System.out.println(percentage.get(i));
            }
            
            int vertices = percentage.size();
            System.out.println("\npasangan yang terbentuk: ");
            System.out.println(vertices);
            System.out.println((vertices*(vertices-1))/2);
            int j=0;
            double delta = 0;
            for(int i=0; i<percentage.size(); i++){
                j = i;
                while(j<percentage.size()){
                    if(i!=j){
                        System.out.print(i + " " +j);
                        delta = percentage.get(i) - percentage.get(j);
                        System.out.println(" "+delta);
                        j++;
                    }else{
                        j++;
                    }
                }   
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void CountWeightPlayTennis(Examples ex){
        int vertices = ex.getData().size();
        System.out.println("\npasangan yang terbentuk: ");
        System.out.println(vertices);
        System.out.println((vertices*(vertices-1))/2);
        int j=0;
        int delta = 0;
        for(int i=0; i<ex.getData().size(); i++){
            j = i;
            while(j<ex.getData().size()){
                if(i!=j){
                    System.out.print(i + " " +j);
                    for(int k=0; k<ex.getData().get(k).size(); k++){
                        if(!ex.getData().get(i).get(k).equals(ex.getData().get(j).get(k))){
                            delta++;
                        }
                    }
                    System.out.println(" "+delta);
                    j++;
                    delta = 0;
                }else{
                    j++;
                }
            }   
        }
    }
}
