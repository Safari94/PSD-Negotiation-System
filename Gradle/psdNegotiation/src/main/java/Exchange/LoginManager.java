package Exchange;

import co.paralleluniverse.actors.ActorRef;
import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import Exchange.Message.Type;
import java.util.HashMap;
import java.util.HashSet;



/**
 * Created by xavier on 29/12/16.
 */
public class LoginManager extends BasicActor<Message,Void> {

    private HashMap<String, ClientInfo> users;


    public LoginManager() {

        this.users = new HashMap<>();
    }

    private void populate() {

        ClientInfo jjj = new ClientInfo("jjj", "jjj");
        ClientInfo xavier = new ClientInfo("xavier", "xavier");
        ClientInfo test = new ClientInfo("test", "test");
        ClientInfo to = new ClientInfo("to", "to");

        users.put("xavier", xavier);
        users.put("jjj", jjj);
        users.put("test", test);
        users.put("to", to);


    }

    @Override
    @SuppressWarnings("empty-statement")
    protected Void doRun() throws InterruptedException, SuspendExecution {
        populate();

        while (receive(message -> {
            final ClientInfo usrinfo = (ClientInfo) message.o;
            final String usrname = usrinfo.getUsername();
            switch (message.type) {
                case LOGIN:
                    if (users.containsKey(usrname)) {
                        if (users.get(usrname).authenticate(usrinfo.getPassword())) {

                            usrinfo.getActor().send(new Message(Type.LOGIN_OK, users.get(usrname)));
                        } else
                            usrinfo.getActor().send(new Message(Type.LOGIN_FAILED, usrname));


            } else
            usrinfo.getActor().send(new Message(Type.USER_N_EXISTS, usrname));
            return true;


        }
        return null;
    }));

        return null;
}
    


}