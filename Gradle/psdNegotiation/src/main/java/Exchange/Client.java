package Exchange;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.io.FiberSocketChannel;
import Exchange.Message.Type;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xavier on 29/12/16.
 */
public class Client extends BasicActor<Message,Void> {

    static int MAXLEN = 1024;
    final FiberSocketChannel socket;
    final ActorRef loginManager;
    final ActorRef requestManager;

    private boolean logged;
    private String usrname;

    private boolean exitflag = false;

    public Client(FiberSocketChannel socket, ActorRef loginManager,ActorRef requestManager) {

        this.loginManager = loginManager;
        this.socket = socket;
        this.logged = false;
        this.requestManager=requestManager;
    }

    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {
        LineReader linereader = new LineReader(self(), socket);
        linereader.spawn();

        while(receive((Message msg) -> {

            try {
                switch (msg.type) {

                    case DATA:
                        String msgContent = new String((byte[]) msg.o);
                        if (msgContent.length() > 0) {
                            String[] splitMsg = msgContent.split(" ");
                            handler(splitMsg);

                        }
                        return true;

                    case LINE:
                        socket.write(ByteBuffer.wrap((byte[]) msg.o));
                        return true;




                    case LOGIN_OK:

                        ClientInfo ci = (ClientInfo) msg.o;
                        this.usrname = ci.getUsername();
                        logged = true;
                        socket.write(ByteBuffer.wrap(("WELCOME " + usrname + "\n").getBytes()));

                    case LOGIN_FAILED:
                        socket.write(ByteBuffer.wrap(("LOGIN_FAILED: " + (String) msg.o + " doesn't exist\n").getBytes()));
                        return true;
                }


            } catch (IOException e) {
            }

            return false;

        }) && !exitflag);

        return null;

    }

    public String concatArgs(String[] args, int nargs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < nargs; i++) {
            sb.append(args[i]).append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }



        private void handler(String[] args) throws SuspendExecution, IOException{
            switch(args[0].trim()){
                case "LOGIN":
                    if(args.length >= 3){
                        ClientInfo usr = new ClientInfo(args[1], args[2].trim(), self());
                        loginManager.send(new Message(Type.LOGIN, usr));
                    }
                    else
                        //error: not enough arguments
                        socket.write(ByteBuffer.wrap("LOGIN: not enough arguments...\n".getBytes()));
                    break;

                case "SELL":

                    if(args.length >= 4){
                         Sell s = new Sell(args[1],Integer.parseInt(args[2]),Float.parseFloat(args[3]),args[4]);
                        requestManager.send(new Message(Type.SELL, s));
                    }
                    else
                        //error: not enough arguments
                        socket.write(ByteBuffer.wrap("SELL: not enough arguments...\n".getBytes()));


                case "BUY":


                     if(args.length >= 4){
                          Buy b = new Buy(args[1],Integer.parseInt(args[2]),Float.parseFloat(args[3]),args[4]);
                         requestManager.send(new Message(Type.BUY, b));
                     }
                     else
                         //error: not enough arguments
                         socket.write(ByteBuffer.wrap("BUY: not enough arguments...\n".getBytes()));

                case "EXIT":
                    exitflag = true;
                    socket.close();
                    break;
                default:
                    socket.write(ByteBuffer.wrap("please login frist\n".getBytes()));
                    break;
            }
        }




}