/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Settlement;

import Client.LineReader;
import Client.Msg;
import Client.Type;
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
 * Nesta classe serão tratados todos os pedidos enviados pelo Exchange, aqui são tratadas as trocas de acçoes
 * e posteriormente 
 */
public class RequestsHandler extends BasicActor<Msg, Void>  {
    
    
    final FiberSocketChannel socket;
    final ActorRef rqsHandler;
    Bank bank;
    
    public RequestsHandler(FiberSocketChannel socket,ActorRef rqs){
        this.socket=socket;
        this.rqsHandler=rqs;
        
    
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
