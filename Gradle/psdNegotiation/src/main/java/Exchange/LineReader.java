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



public class LineReader extends BasicActor<Message, Void> {
    static int MAXLEN = 1024;
    private ActorRef<Message> dest;
    final FiberSocketChannel socket;
    ByteBuffer in = ByteBuffer.allocate(MAXLEN);
    ByteBuffer out = ByteBuffer.allocate(MAXLEN);

    LineReader(ActorRef<Message> dest, FiberSocketChannel socket) {
        this.dest = dest;
        this.socket = socket;
    }

    protected void setDestination(ActorRef dest){
        this.dest = dest;
    }

    protected Void doRun() throws InterruptedException, SuspendExecution {
        boolean eof = false;
        byte b = 0;
        try {
            for(;;) {
                if (socket.read(in) <= 0) {
                    eof = true;

                }
                in.flip();
                while(in.hasRemaining()) {
                    b = in.get();
                    out.put(b);
                    if (b == '\n')
                        break;
                }
                if (eof || b == '\n') { // send line
                    out.flip();
                    if (out.remaining() > 0) {
                        byte[] ba = new byte[out.remaining()];
                        out.get(ba);
                        out.clear();
                        String msgContent = new String((byte[]) ba);
                        System.out.println(msgContent);
                        dest.send(new Message(Message.Type.DATA, ba));
                    }
                }
                if (eof && !in.hasRemaining())
                    break;
                in.compact();
            }

            System.out.println("[EOF] ");
            dest.send(new Message(Message.Type.EOF, null));
            return null;
        } catch (IOException e) {

            System.out.println("[IOE] ");
            dest.send(new Message(Message.Type.IOE, null));
            return null;
        }
    }
}