/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectFiles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ahmet Nuri
 */
public class DB {
    private Connection c;
    
    public DB() throws SQLException {       
        c = DriverManager.getConnection("jdbc:mysql://localhost:3306/demirbas?user=root&password=2021");       
    }
    
    public List<String> userList(){
        List<String> users = new ArrayList<>();
        try {
            String query = "SELECT username FROM users";
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            users.add(rs.getString("username"));
        }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);    
        }
        return users;
    }
    
    public boolean searcUser(String username, String password) throws SQLException {
        ResultSet rs = null;
        String query = String.format("SELECT password FROM users WHERE username = '%s'", username);
        java.sql.Statement st = c.createStatement();
        rs = st.executeQuery(query);
   
        if(rs.next()) {
            String pass = rs.getString("password");
            return pass.equals(password);
        }
        return false;
    }
    
    public int add(ItemClass item){
        String queryString = "INSERT INTO items (name,amount,price,department) VALUES(?,?,?,?)";
        try {
            PreparedStatement pstmt = c.prepareStatement(queryString);
            pstmt.setString(1, item.name);
            pstmt.setInt(2, item.amount);
            pstmt.setDouble(3, item.price);
            pstmt.setInt(4, item.department);
            return pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int delete(int id) {
        String query = "DELETE FROM items WHERE id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(query);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        } catch(SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int update(ItemClass item){
        String query = "UPDATE items SET name = ?, amount = ?, price = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(query);
            pstmt.setInt(4, item.id);
            pstmt.setString(1, item.name);
            pstmt.setInt(2, item.amount);
            pstmt.setDouble(3, item.price);
            return pstmt.executeUpdate();
        } catch(SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    public ItemClass getItem(int id) {
        String query = "SELECT * FROM items WHERE id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                ItemClass item = new ItemClass();
                item.id = rs.getInt("id");
                item.name = rs.getString("name");
                item.amount = rs.getInt("amount");
                item.price = rs.getDouble("price");
                return item;
            }
            else {
                return null; 
            }
            
        } catch(SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    public List<ItemClass> getItems(int department){
        ArrayList<ItemClass> items = new ArrayList<>();
        String queryString = "SELECT * FROM items WHERE department = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(queryString);
            pstmt.setInt(1, department);
            ResultSet set = pstmt.executeQuery();
            
            while(set.next()){
                ItemClass item = new ItemClass();
                item.id = set.getInt("id");
                item.name = set.getString("name");
                item.amount = set.getInt("amount");
                item.price = set.getDouble("price");
                item.department = set.getInt("department");
                
                items.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
    
    public void close() throws SQLException {
        c.close();
    }
    
}
