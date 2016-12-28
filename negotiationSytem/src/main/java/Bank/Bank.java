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
import java.util.HashMap;
import Exchange.Pedido;

/**
 *
 * @author toanjo
 */
public final class Bank {
    

    HashMap<String,Float> accounts;

    public Bank() throws SQLException {
        this.accounts=new HashMap<>();
        populate(accounts);
    }
    
    public Bank(Pedido pd){       
        String contaA = pd.usr1;
        String contaB = pd.usr2;
        float quantidade = pd.price * pd.amount;
        execute(contaA,contaB,quantidade);
    }
    
    void populate(HashMap<String,Float> a){
        a.put("001", Float.parseFloat("1500"));
        a.put("002", Float.parseFloat("50"));
        a.put("003", Float.parseFloat("250.50"));
        a.put("004", Float.parseFloat("1200"));
    }
    
    void execute(String contaA, String contaB, float quantidade){
        float aBefore = accounts.get(contaA);
        float bBefore = accounts.get(contaB);
        
        if(bBefore >= quantidade){        
            accounts.put(contaA, aBefore + quantidade);
            accounts.put(contaB, bBefore - quantidade);
        } else System.out.println("Fundos insuficientes!");
    }
       
    /*
    Connection c;
    Statement s;

    public Bank() throws SQLException {
        this.c = DriverManager.getConnection("jdbc:mysql://localhost/psd16");
        this.s = c.createStatement();
    }
    
    public void execute(String contaA, String contaB, float quantidade){
        try {
            s.executeUpdate("update Bank set Balance = Balance + "+quantidade+" "
                    + "where AccountNumber = "+contaA+";"); 
            s.executeUpdate("update Bank set Balance = Balance - "+quantidade+" "
                    + "where AccountNumber = "+contaB+";");
            
        } catch (SQLException e) {}
    }
    
    */
    
}
