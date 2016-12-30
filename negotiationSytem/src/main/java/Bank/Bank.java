/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bank;

import Client.Msg;
import Client.Type;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import Exchange.Pedido;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;

/**
 *
 * @author toanjo
 */
public final class Bank extends BasicActor <Msg,Void>{
    

    HashMap<String,Float> accounts;
    ActorRef setRef;
    String contaA;
    String contaB;
    float quantidade;

    public Bank() throws SQLException {
        this.accounts=new HashMap<>();
        populate(accounts);
    }
    
    public Bank(ActorRef ref, Pedido pd){    
        setRef = ref;
        contaA = pd.usr1;
        contaB = pd.usr2;
        quantidade = pd.price * pd.amount;
    }
    
    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        int v = execute (contaA, contaB, quantidade);
        if (v==0){
            ref.send(new Msg(Type.BANK_OK,"Bank Transaction Completed!"));
        } else {
            ref.send(new Msg(Type.BANK_FAILED, "Bank Transaction Failed!"));
        }
        return null;
    }
    
    int execute(String contaA, String contaB, float quantidade){
        float aBefore = accounts.get(contaA);
        float bBefore = accounts.get(contaB);
        
        if(bBefore >= quantidade){        
            accounts.put(contaA, aBefore + quantidade);
            accounts.put(contaB, bBefore - quantidade);
            return 0;
        } else return -1;
    }
    
    void populate(HashMap<String,Float> a){
        a.put("001", Float.parseFloat("1500"));
        a.put("002", Float.parseFloat("50"));
        a.put("003", Float.parseFloat("250.50"));
        a.put("004", Float.parseFloat("1200"));
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
