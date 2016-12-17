/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exchange;


import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberServerSocketChannel;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author xavier
 */
public class AcceptorSettlement {
    
    final int port;
    final ActorRef rqsHandler;

    public AcceptorSettlement(int port, ActorRef rqsHandler) {
        this.port = port;
        this.rqsHandler = rqsHandler;
    }
    
    protected Void doRun() throws InterruptedException, SuspendExecution {
      try {
        FiberServerSocketChannel ss = FiberServerSocketChannel.open();
        ss.bind(new InetSocketAddress(port));

        while (true) {
          FiberSocketChannel socket = ss.accept();
         new RequestsHandler(socket,rqsHandler).spawn();
        }

      }catch (IOException e) { }
      
        return null;
    
}
}
