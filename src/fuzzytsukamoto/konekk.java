/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzytsukamoto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author aceqr
 */
public class konekk {
    private String url = "jdbc:h2:FuzzyTsukamotoJeffi";
 
    private String username = "root";
 
    private String password = "root";
 
    public Connection KoneksiDB(){
        try {
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            return con;           
        }catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "ERROR : " + e.getMessage());
            return null;
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR : " + e.getMessage());
            return null;
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR : " + e.getMessage());
            return null;
        }
    }

    
}
