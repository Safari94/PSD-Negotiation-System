/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank;

import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author toanjo
 */
public final class Bank extends BasicActor<Void,Void> {

    HashMap<String,Float> accounts;
    String mess;

    public Bank(String mess) throws SQLException {
        this.accounts=new HashMap<>();
        this.mess=mess;
        populate(accounts);
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
        
        if(aBefore >= quantidade){        
            accounts.put(contaA, aBefore - quantidade);
            accounts.put(contaB, bBefore + quantidade);
        } else System.out.println("Fundos insuficientes!");
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        String pedido[]=mess.split(" ");

        String contaA = pedido[0];
        String contaB = pedido[1];
        String company = pedido[2];
        int amount = Integer.parseInt(pedido[3]);
        float price = Float.parseFloat(pedido[4]);

        System.out.println(mess);
        return null;
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
            s.executeUpdate("update Bank set Balance = Balance - "+quantidade+" "
                    + "where AccountNumber = "+contaA+";"); 
            s.executeUpdate("update Bank set Balance = Balance + "+quantidade+" "
                    + "where AccountNumber = "+contaB+";");
            
        } catch (SQLException e) {}
    }
    
    */
    
}
