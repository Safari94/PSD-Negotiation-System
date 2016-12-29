package Exchange;
import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import java.util.*;


/**
 * Created by xavier on 29/12/16.
 */
public class RequestManager extends BasicActor<Message, Void> {


    private ArrayList<Sell> sells;
    private ArrayList<Buy> buys;
    private LinkedList<Pedidos> pedidos;

    public RequestManager() {

        this.sells = new ArrayList<>();
        this.buys = new ArrayList<>();
        this.pedidos = new LinkedList<Pedidos>();
    }


    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {


        while (receive(message -> {

            switch (message.type) {
                case SELL:
                    Sell s=(Sell) message.o;

                    return true;
                case BUY:
                    Buy b=(Buy) message.o;

                    return true;

            }
            return null;
        })) ;

        return null;
    }


}
























}
