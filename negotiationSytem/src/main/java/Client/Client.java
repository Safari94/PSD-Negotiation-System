/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

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



public class Client extends BasicActor<Msg,Void> {
    
    final FiberSocketChannel socket;
    final ActorRef userHandler;
    private boolean ativo;
    private String nome;
    private String password;

    public Client() {
        this.socket = null;
        this.userHandler = null;
    }
    
    
    
    public Client(FiberSocketChannel socket, ActorRef userHandler){
    this.userHandler=userHandler;
    this.socket=socket;
    this.ativo=false;
    this.nome=new String();
    this.password=new String();
    }
    
    private final String inicio = "Commands:\n 1)Login <user> <pass>\n 2)Exit\n\n";
    private final String inicioErro = "\nERROR! Choose one of the following commands:\n 1)Login <user> <pass>\n 2)Exit\n\n";
    private final String menu1 = "Commands:\n 1)Sell <Companyname> <Ammount> <price>\n 2)Buy <> <Ammount> <price>\n 3) Logout\n";
    
    private final String menu1Erro = "\nERROR! Choose one of the following commands: \n 1)Sell <Companyname> <Ammount> <price>\n 2)Buy <> <Ammount> <price>\n 3) Logout\n";

    public static void main(String[] args) throws InterruptedException, SuspendExecution{
       Client cli = new Client();
       cli.doRun();
    }    
    protected Void doRun() throws InterruptedException, SuspendExecution {

        new LineReader(self(), socket).spawn();

        self().send(new Msg(Type.LINE, inicio.getBytes()));

        while (receive((Msg msg) -> {

            try {

                switch (msg.type) {

                case DATA:
                String s= new String((byte[])msg.o);
                String[] aux= s.split(" ");

                if(!ativo){

                    switch(aux[0]){

                        case "Login\n":
                            if(aux.length == 3) {
                                nome=aux[1];
                                userHandler.send(new Msg(Type.LOGIN,new Usr (self(),aux[1],aux[2])));
                            }
                            else{
                                self().send(new Msg(Type.LINE,inicioErro.getBytes()));
                            }
                            break;

                        case "Exit\n":
                            if(aux.length == 1) {
                                this.socket.close();
                                return false;
                            }
                            else{
                                self().send(new Msg(Type.LINE,inicioErro.getBytes()));
                                break;
                            }
                        default: self().send(new Msg(Type.LINE,inicioErro.getBytes()));
                    }

                }
                else{
                    switch(aux[0]){

                        case "Sell":
                            if(aux.length == 3) {
                                userHandler.send(new Msg(Type.SELL, new Sell(nome,aux[1],Integer.parseInt(aux[2]),Float.parseFloat(aux[3]))));
                            }
                            else{
                                self().send(new Msg(Type.LINE,menu1Erro.getBytes()));
                            }
                            break;
                        case "Buy":
                            if(aux.length ==3) {
                                userHandler.send(new Msg(Type.BUY, new Buy(nome,aux[1],Integer.parseInt(aux[2]),Float.parseFloat(aux[3]))));
                            }
                            else{       
                                self().send(new Msg(Type.LINE,menu1Erro.getBytes()));
                            }
                            break;
                        case "Logout\n":
                            if(aux.length == 1) {       
                                userHandler.send(new Msg(Type.LOGOUT,null));
                                self().send(new Msg(Type.LOGOUT_OK,inicio.getBytes()));    
                            }
                            else self().send(new Msg(Type.LINE,menu1Erro.getBytes()));    
                            break;
                        default: 
                            if(ativo){
                                StringBuilder nick= new StringBuilder();
                                String mess= new String((byte[])msg.o);
                                nick.append("@"+ this.nome+ ": " + mess);       
                            }
                            else
                                if(ativo) self().send(new Msg(Type.LINE,menu1Erro.getBytes()));
                    }

                }
                
                return true;

                case LOGIN_OK:
                    this.ativo=true;
                    self().send(new Msg(Type.LINE,menu1.getBytes()));
                    return true;
                          
                case LOGIN_FAILED:
                    this.ativo=true;
                    self().send(new Msg(Type.LINE,null));

                case LOGOUT_OK:
                    this.ativo=false;
                    self().send(new Msg(Type.LINE,inicio.getBytes()));
                    return true;

                case EOF:

                case IOE:
                    //userHandler.send(new Msg(Type.LOGOUT, new MRoom2(this.nome, self(), null)));
                    this.socket.close();
                    return false;

                case LINE:
                    this.socket.write(ByteBuffer.wrap((byte[])msg.o));
                    return true;
                    
                }
            } catch (IOException e) {
                self().send(new Msg(Type.LEAVE, self()));
            }

            return false;

            }));

    return null;
    
    }


  

}


