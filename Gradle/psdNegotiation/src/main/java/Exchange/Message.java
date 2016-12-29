package Exchange;

/**
 * Created by xavier on 29/12/16.
 */
public class Message {

    final Type type;
    final Object o;  // careful with mutable objects, such as the byte array


    static int MAXLEN = 1024;

    static enum Type { DATA, EOF, IOE,  ENTER, LEAVE, LINE, LOGIN, LOGOUT, LOGIN_OK, LOGIN_FAILED,BUY,SELL,REQUEST,
        ERROR_ILD, USER_N_EXISTS, ERROR_UAE}


    Message(Type type, Object o) {
        this.type = type;
        this.o = o;
    }

}
