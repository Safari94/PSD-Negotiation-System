/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;

import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.*;
import co.paralleluniverse.fibers.io.*;
import java.net.InetSocketAddress;

/**
 *
 * @author toanjo
 */
public class Acceptor extends Actor{
    
    int port;
    
    public Acceptor(int port){
        this.port=port;
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        
        try{
            FiberServerSocketChannel ss = FiberServerSocketChannel.open();
            ss.bind(new InetSocketAddress(port));
            
            while(true){
                FiberSocketChannel socket = ss.accept();
                //new LoginManager(socket);
            }
            
        } catch (Exception e) {
            return null;
        }
        
    }
    
}
