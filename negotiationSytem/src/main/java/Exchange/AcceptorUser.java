

package Exchange;


import Client.Client;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;

import java.util.*;

/**
 *
 * @author xavier
 */
public class AcceptorUser extends BasicActor {
    
    final int port;
    final ActorRef userHandler;
    final ActorRef settlementHandler;

    public AcceptorUser(int port, ActorRef userHandler, ActorRef settlementHandler) {
        this.port = port;
        this.userHandler = userHandler;
        this.settlementHandler = settlementHandler;
    }
    
    protected Void doRun() throws InterruptedException, SuspendExecution {
      try {
        FiberServerSocketChannel ss = FiberServerSocketChannel.open();
        ss.bind(new InetSocketAddress(port));

        while (true) {
          FiberSocketChannel socket = ss.accept();
         new Client(socket,userHandler,settlementHandler).spawn();
        }

      }catch (IOException e) { }
      
        return null;
    }
    
    
}
