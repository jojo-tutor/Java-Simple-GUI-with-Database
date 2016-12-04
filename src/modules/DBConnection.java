/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jojo
 */
public class DBConnection {
    private static String IP = "localhost:3306";
    private static String PORT = "3306";
    private static String SHORTIP = "localhost";
    private static String DB = "simple_app";
    private static String USERNAME = "root";
    private static String PASSWORD = "root";
    
    private static String CONN_STRING = "jdbc:mysql://" + IP + "/" + DB;
    
    private static Connection con = null;
    
    public void setIP(String IpAdd){
        IP = IpAdd;
    }
    
    public void setSHORTIP(String ShortIpAdd){
        SHORTIP = ShortIpAdd;
    }
    
    public void setPORT(String PortAdd){
        PORT = PortAdd;
    }
    
    public void setDB(String Database){
        DB = Database;
    }
    
    public void setUID(String UID){
        USERNAME = UID;
    }
    
    public void setPWD(String PWD){
        PASSWORD = PWD;
    }
    
    public String getIP(){
        return DBConnection.IP;
    }
    
    public String getSHORTIP(){
        return DBConnection.SHORTIP;
    }
    
    public String getPORT(){
        return DBConnection.PORT;
    }
    
    public String getDB(){
        return DBConnection.DB;
    }
    
    public String getUID(){
        return DBConnection.USERNAME;
    }
    
    public String getPWD(){
        return DBConnection.PASSWORD;
    }
    
    public String getConString(){
        return DBConnection.CONN_STRING;
    }
    
    
    public void setConString(){
        CONN_STRING = "jdbc:mysql://" + DBConnection.IP + "/" + DBConnection.DB;
    }
    
    public DBConnection(){
    }
    
    public boolean checkConnection(){       
        try{
            con = DriverManager.getConnection(CONN_STRING, USERNAME,PASSWORD);
            con.close();
            return true;
        }catch(SQLException ex){
            return false;
        }
    }
    
    public Connection getCon(){
        connect();
        return DBConnection.con;
    }
   
    public void connect(){

        try {
            this.con.close();
        } catch (Exception ex) {
            //Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        try{
            con = DriverManager.getConnection(CONN_STRING, USERNAME,PASSWORD);
        }catch(SQLException ex){
            System.err.println(ex.toString());
        }

    }
    
    public void insertData(String table, String fields, String values){
        connect();
        try {
            String sqlCommand;
            Statement st;
            st = (Statement) con.createStatement();
            sqlCommand = "INSERT INTO " + table + "(" + fields + ") VALUES(" + values + ")";
            st.executeUpdate(sqlCommand);
            st.close();
            JOptionPane.showMessageDialog(null, "Data has been saved.");
        } catch (SQLException ex) {
            if(ex.getErrorCode()==1062){
                JOptionPane.showMessageDialog(null, "The system has encountered duplicate values, please re-check your data.");
            }else{
                //JOptionPane.showMessageDialog(null, ex);
                System.err.println(ex.toString());
            }
        }
    }
    
    public void updateData(String table, String SETclause, String WHEREclause){
        connect();
        try {
            String sqlCommand;
            Statement st = con.createStatement();
            sqlCommand = "UPDATE " + table + " SET " + SETclause + " WHERE " + WHEREclause + "";
            st.execute(sqlCommand);
            st.close();
            JOptionPane.showMessageDialog(null, "Data has been updated.");
        } catch (SQLException ex) {
            if(ex.getErrorCode()==1062){
                JOptionPane.showMessageDialog(null, "The system has encountered duplicate values, please re-check your data.");
            }else{
                //JOptionPane.showMessageDialog(null, ex);
                System.err.println(ex.toString());
            }
        }
    }
    
    public void removeData(String table, String WHEREclause){
        connect();
        try {
            String sqlCommand;
            Statement st = con.createStatement();
            sqlCommand = "DELETE FROM " + table + " WHERE " + WHEREclause + "";
            st.execute(sqlCommand);
            st.close();
            JOptionPane.showMessageDialog(null, "Data has been removed.");
        } catch (SQLException ex) {
            if(ex.getErrorCode()==1451){//catch if parent row is deleted, because it is primary key
                JOptionPane.showMessageDialog(null, "Notice: You cannot remove this field, because it is used by other constraints.");
            }else{
                //JOptionPane.showMessageDialog(null, ex);
                System.err.println(ex.toString());
            }
        }
    }
    
    public String findOneQuery(String field, String table, String WHEREClause){
        connect();
        String value="-1";
        try {
            Statement st = (Statement) con.createStatement();
            String query = "SELECT " + field + " FROM " + table + " WHERE " + WHEREClause + "";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                value=rs.getString(1);
            }
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null,ex);
            System.err.println(ex.toString());
        }
        return value;
    }

    public void fillTable(JTable table, String Query){
        connect();
        try
        {
            con = DriverManager.getConnection(CONN_STRING, USERNAME,PASSWORD);

            com.mysql.jdbc.Statement stat;
            stat = (com.mysql.jdbc.Statement) con.createStatement();
            ResultSet rs = stat.executeQuery(Query);

            //To remove previously added rows
            while(table.getRowCount() > 0) 
            {
                ((DefaultTableModel) table.getModel()).removeRow(0);
            }
            int columns = rs.getMetaData().getColumnCount();
            while(rs.next())
            {  
                Object[] row = new Object[columns];
                for (int i = 1; i <= columns; i++)
                {  
                    row[i - 1] = rs.getObject(i);
                }
                ((DefaultTableModel) table.getModel()).insertRow(rs.getRow()-1,row);
            }

            rs.close();
            stat.close();
        }
        catch(SQLException ex){
            System.err.println(ex.toString());
        }
    }

    public void fillListSingleCombo(JComboBox combo, String Query){
        connect();
        combo.removeAllItems();
        try {
            Statement st = (Statement) con.createStatement();
            ResultSet rs = st.executeQuery(Query);
            while(rs.next()){
                combo.addItem(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, ex);
            System.err.println(ex.toString());
        }
    }
    
}
