package Client;

public enum Type {
	DATA,
	EOF, 
	IOE, 
	ENTER, 
	LEAVE, 
	LINE, 
        BUY,
        SELL,
	CREATE, 
	LOGIN,  
	LOGIN_OK,
        LOGIN_FAILED,
        LOGOUT_OK,
	LOGOUT,
        PEDIDO,
        BANK_OK,
        BANK_FAILED,
        SETTLEMENT_OK,
        SETTLEMENT_FAILED,
        EXCHANGE_OK,
        EXCHANGE_FAILED
}
