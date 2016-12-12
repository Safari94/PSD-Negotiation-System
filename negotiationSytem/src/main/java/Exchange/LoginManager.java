/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import proto_client.Client.User;

/**
 *
 * @author toanjo
 */
public class LoginManager extends Actor<Message,Void>{
    
    FiberSocketChannel s;
    User u;
    
    public LoginManager(FiberSocketChannel s,User u){
        this.s=s;
        this.u=u;
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        
        try{
        //Conexão SQL
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost/PSDDB");
        Statement s = c.createStatement();
                
        
        String username = u.getUser();
        String password = u.getPass();
        
        ResultSet rs = s.executeQuery("select * from users where userid = "+username
        +"and password = "+password);
        if(!rs.next()){
            Auth client = new Auth(null,null,null,self());
            client.ref.send(null);
        } else {
            while(rs.next()){
                String accountnumber=rs.getString("accountnumber");
                
                Auth client = new Auth(username,password,accountnumber,self());
                client.ref.send(client);                
            }
        }
        
                
        return null;
        } catch (Exception e){
        return null;
        }
    }
    
}
