package Exchange;

/**
 * Created by xavier on 29/12/16.
 */
public class Message {

    final Type type;
    final Object o;


    static int MAXLEN = 1024;

    static enum Type { DATA, EOF, IOE,  LOGIN, LOGIN_OK, LOGIN_FAILED,BUY,SELL,SELL_OK,
        BUY_OK, USER_N_EXISTS, TRANSACT_OK, TRANSACT_FAILED}


    Message(Type type, Object o) {
        this.type = type;
        this.o = o;
    }

}
