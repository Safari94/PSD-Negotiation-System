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
import Bank.Bank;
import Exchange.Pedido;

/**
 *
 * @author xavier
 * Nesta classe serão tratados todos os pedidos enviados pelo Exchange, aqui são tratadas as trocas de acçoes
 * e posteriormente 
 */
public class RequestsHandler extends BasicActor<Msg, Void>  {
    
    
    final FiberSocketChannel socket;
    final HashMap<String,ArrayList<Accao>> accoes;
    final ActorRef rqsHandler;
    Bank bank;
    
    public RequestsHandler(FiberSocketChannel socket,ActorRef rqs){
        this.socket=socket;
        this.rqsHandler=rqs;
        this.accoes= new HashMap<>();
        
        
    
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {
        
     while(receive(msg -> { 

            switch(msg.type){

		case PEDIDO:
                    
                    Pedido pd =(Pedido)msg.o;
                    
                    //Retirar accoes do usr1
                    ArrayList<Accao> aux = this.accoes.get(pd.usr1);
                    for(Accao a:aux){
                    
                        if(a.nomeEmpresa.equals(pd.company)){
                             a.totalAccoes-=pd.amount;   
                        }
                    }
                    
                    //Adicionar accoes do usr2
                    ArrayList<Accao> aux1 = this.accoes.get(pd.usr2);
                    for(Accao a:aux1){
                    
                        if(a.nomeEmpresa.equals(pd.company)){
                             a.totalAccoes+=pd.amount;   
                        }
                    }
                    
                    new Bank(pd).start();
            }

            return false;
	}));
        return null;
    }
}
