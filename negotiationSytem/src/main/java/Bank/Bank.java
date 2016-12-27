/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author toanjo
 */
public class Bank {
    
    Connection c;
    Statement s;

    public Bank() throws SQLException {
        this.c = DriverManager.getConnection("jdbc:mysql://localhost/psd16");
        this.s = c.createStatement();
    }
    
    public void execute(String contaA, String contaB, float quantidade){
        try {
            s.executeUpdate("update Bank set Balance = Balance - "+quantidade+" "
                    + "where AccountNumber = "+contaA+";"); 
            s.executeUpdate("update Bank set Balance = Balance + "+quantidade+" "
                    + "where AccountNumber = "+contaB+";");
            
        } catch (SQLException e) {}
    }
       
    
}
