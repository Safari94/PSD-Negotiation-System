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
    final ActorRef subscriptionManager;
    private boolean logged;
    private String usrname;
    private boolean exitflag = false;

    public Client(FiberSocketChannel socket, ActorRef loginManager, ActorRef subscriptionManager) {
        this.loginManager = loginManager;
        this.subscriptionManager = subscriptionManager;
        this.socket = socket;
        this.logged = false;
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
                            System.out.println(msgContent);
                            handler(splitMsg);
                        }
                        return true;

                    case LOGIN_OK:
                        System.out.println("Login"); //D
                        this.usrname = (String) msg.o;
                        System.out.println(this.usrname);
                        logged = true;
                        System.out.println("A informar cliente");//D
                        socket.write(ByteBuffer.wrap(("welcome "+this.usrname+"\n").getBytes()));
                        System.out.println("Informei cliente");//D
                        return true;

                    case LOGIN_FAILED:
                        this.usrname=(String) msg.o;
                        System.out.println("A informar cliente");//D
                        socket.write(ByteBuffer.wrap(("login_failed "+this.usrname+"\n ").getBytes()));
                        System.out.println("Informei cliente");//D
                        return true;

                    case USER_N_EXISTS:
                        this.usrname=(String) msg.o;
                        System.out.println("A informar cliente");//D
                        socket.write(ByteBuffer.wrap(("login_failed").getBytes()));
                        System.out.println("Informei cliente");//D
                        return true;


                    default:
                        break;
                }

            } catch (IOException e) {}
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
            case "login":
                System.out.println(args.length);
                if(args.length == 3){
                    ClientInfo usr = new ClientInfo(args[1], args[2].trim(), self());
                    loginManager.send(new Message(Type.LOGIN, usr));
                    break;
                }
                else {
                    //error: not enough arguments
                    socket.write(ByteBuffer.wrap("login: not enough arguments...\n".getBytes()));
                }
                break;

            case "sell":
                if(args.length >= 4) {
                    Sell s = new Sell(args[1], Integer.parseInt(args[2]), Float.parseFloat(args[3]), args[4]);
                    loginManager.send(new Message(Type.SELL, s));
                    subscriptionManager.send(new Message(Type.SELL, s));

                }
                break;

            case "buy":
                System.out.println(args.length);
                if(args.length >= 4){
                    Buy b = new Buy(args[1],Integer.parseInt(args[2]),Float.parseFloat(args[3]),args[4]);
                    loginManager.send(new Message(Type.BUY, b));
                    System.out.println(args.length); //D
                    subscriptionManager.send(new Message(Type.BUY, b));

                }
                break;

            case "out":
                exitflag = true;
                socket.close();
                break;

            default:
                socket.write(ByteBuffer.wrap("please login frist\n".getBytes()));
                break;

        }
    }

}
