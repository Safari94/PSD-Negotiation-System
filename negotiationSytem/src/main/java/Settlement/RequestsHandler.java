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
    
    final ActorRef exchangeH;
    final FiberSocketChannel socket;
    
    

  public RequestsHandler(FiberSocketChannel socket,ActorRef exchangeH ) {
        this.socket=socket;
        this.exchangeH=exchangeH;
    }
  
  
  @Override
  protected Void doRun() throws InterruptedException, SuspendExecution {

            new LineReader(self(), socket).spawn();

            self().send(new Msg(Type.LINE, null));
            
            while (receive(msg -> {

                  try {

                  switch (msg.type) {
                      
                      
                      //Fazer cenas
                  }
            
            
            
            return null;
            
  }     
    
}
