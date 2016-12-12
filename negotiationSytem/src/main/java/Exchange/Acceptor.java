/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.*;
import co.paralleluniverse.fibers.io.*;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import proto_client.Client.User;

/**
 *
 * @author toanjo
 */
public class Acceptor extends Actor{
    
    int port;
    ArrayList<User> users;
    
    public Acceptor(int port){
        this.port=port;
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        
        try{
            
            ServerSocket ss = new ServerSocket(6063);            
            FiberServerSocketChannel fss = FiberServerSocketChannel.open();
            fss.bind(new InetSocketAddress(port));
            
            while(true){
                FiberSocketChannel fsocket = fss.accept();
                Socket socket = ss.accept();
                
                CodedInputStream cis = CodedInputStream.newInstance
                                (socket.getInputStream());
                CodedOutputStream cos = CodedOutputStream.newInstance
                                (socket.getOutputStream());
                
                
                
                //new LoginManager(fsocket,user).spawn();
            }
            
        } catch (Exception e) {
            return null;
        }
        
    }
    
}
