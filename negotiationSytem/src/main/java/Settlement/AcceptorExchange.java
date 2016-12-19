/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Settlement;

import Client.Client;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberServerSocketChannel;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import Exchange.SettlementHandler;

/**
 *
 * @author xavier
 */
class AcceptorExchange extends BasicActor{

    final int port;
    final ActorRef exchangeHandler;

    public AcceptorExchange(int port, ActorRef exchangeHandler) {
        this.port = port;
        this.exchangeHandler = exchangeHandler;
    }
    
    protected Void doRun() throws InterruptedException, SuspendExecution {
      try {
        FiberServerSocketChannel ss = FiberServerSocketChannel.open();
        ss.bind(new InetSocketAddress(port));

        while (true) {
          FiberSocketChannel socket = ss.accept();
         new SettlementHandler(socket,exchangeHandler).spawn();
        }

      }catch (IOException e) { }
      
        return null;
    }
    
}
