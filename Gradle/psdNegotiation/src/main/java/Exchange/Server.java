package Exchange;

import co.paralleluniverse.actors.*;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.*;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by xavier on 29/12/16.
 */
public class Server {


    /*Aceitar Clientes*/
    static class AcceptorClient extends BasicActor {
        final int port; //Client
        final ActorRef subscriptionManager;
        final ActorRef loginManager;


        public AcceptorClient(int port,  ActorRef loginManager, ActorRef subscriptionManager) {
            this.port = port;
            this.loginManager = loginManager;
            this.subscriptionManager = subscriptionManager;
        }

        @Override
        protected Object doRun() throws InterruptedException, SuspendExecution {

            System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers","false");
            System.out.println("Exchange Iniciado!");

            try {
                //Socket Channel para fibers
                FiberServerSocketChannel ss = FiberServerSocketChannel.open();
                ss.bind(new InetSocketAddress(port));
                while (true) {
                    FiberSocketChannel socket = ss.accept();
                    /*
                    Accept bloqueante que devolve um socket channel para fiber.
                    Quando alguém se conecta, é criado um actor por socket conectado.
                    Actores privilegiados : room, acceptor.
                    Ao criar o user, passa-se o id de room e o socket.
                    */
                    new Client(socket, loginManager, subscriptionManager).spawn();
                }
            } catch (IOException e) {
            }
            return null;
        }
    }


    public static void main(String[] args) throws Exception {

        int port = 12345;//Integer.parseInt(args[0]);

        
        ActorRef publisher = new Publisher().spawn();
        ActorRef subscriptionManager = new SubscriptionManager(publisher).spawn();
        ActorRef loginManager = new LoginManager().spawn();
        AcceptorClient acceptorC = new AcceptorClient(port,loginManager, subscriptionManager);
        acceptorC.spawn();
        acceptorC.join();

        //ActorRef requestManager = new RequestManager().spawn();
    }


}


































