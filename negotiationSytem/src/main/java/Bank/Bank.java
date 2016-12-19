/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank;

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

    public Bank(){
        //Connection c = ;
        //Statement s = c.createStatement();
    }
    
    public void execute(String a, String b, float quantidade){
        try {
            s.executeUpdate("update "); //Tirar dinheiro a a
            s.executeUpdate("update "); //Por dinheiro em b
        } catch (SQLException e) {}
    }
    
    
}
