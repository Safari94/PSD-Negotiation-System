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
    
    Client(FiberSocketChannel socket, ActorRef userHandler){
    this.userHandler=userHandler;
    this.socket=socket;
    this.ativo=false;
    this.nome=new String();
    this.password=new String();
    }
    
    private final String inicio = "Commands:\n 1)Login <user> <pass>\n 2) Exit\n\n";
    private final String inicioErro = "\nERROR chose one of the following commands:\n 1)Login <user> <pass>\n 2)Exit\n\n";
    private final String menu1 = "Commands:\n 1)Sell <Companyname> <Ammount> <price>\n 2)Buy <> <Ammount> <price>\n 3) Logout\n";
    private final String menu1Erro = "\nERROR chose one of the following commands: \n 1)Sell <Companyname> <Ammount> <price>\n 2)Buy <> <Ammount> <price>\n 3) Logout\n";
}
