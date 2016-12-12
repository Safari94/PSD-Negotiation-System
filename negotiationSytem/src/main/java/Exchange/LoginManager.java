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
        
        
        
        
        
        return null;
        } catch (Exception e){
        return null;
        }
    }
    
}
