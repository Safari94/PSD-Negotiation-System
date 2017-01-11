package Bank;

import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;
import bitronix.tm.TransactionManagerServices;

import javax.jws.soap.SOAPBinding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import java.sql.*;
import java.util.HashMap;

/**
 *
 * @author toanjo
 */

public final class Bank extends BasicActor<Void,Void> {

    String mess;

    public Bank(String mess) throws SQLException {
        this.mess = mess;
    }

    @Override
    protected Void doRun() throws InterruptedException, SuspendExecution {

        try {
            Context ctx = new InitialContext();
            UserTransaction txn = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

            String pedido[] = mess.split(" ");

            String contaA = pedido[0];
            String contaB = pedido[1];
            String company = pedido[2];
            int amount = Integer.parseInt(pedido[3]);
            float price = Float.parseFloat(pedido[4]);

            txn.begin();


            DataSource ds = (DataSource) ctx.lookup("jdbc/bankaccounts");
            Connection c = ds.getConnection();
            Statement s = c.createStatement();
            ResultSet rs;

            float aBalance = -1;
            float bBalance = -1;

            rs = s.executeQuery("SELECT balance from Users where Username = " + contaA);
            while (rs.next()) aBalance = rs.getFloat("Balance");

            rs = s.executeQuery("SELECT balance from Users where Username = " + contaB);
            while (rs.next()) bBalance = rs.getFloat("Balance");


            if (aBalance < amount * price) {
                //Print para debugging
                System.out.println("Transação no banco falhou por fundos insuficientes." + contaA + " " + contaB + " " + company);
                txn.rollback();
                return null; //Enviar TRANSACT_FAILED
            } else {
                try {
                    s.executeUpdate("update Users set Balance = Balance - "
                            + amount * price + " where Username = " + contaA);

                    s.executeUpdate("update Users set Balance = Balance + "
                            + amount * price + " where Username = " + contaB);
                    //Print para debugging
                    System.out.println("Transação no banco efetuada." + contaA + " " + contaB + " " + company);
                } catch (SQLException se){
                    txn.rollback();
                    //Enviar TRANSACT_FAILED
                }
            }

            //Alterar tabela Actions
            //Em caso de sucesso, enviar TRANSACT_OK

            txn.commit();

        } catch (Exception e) {
            //Enviar TRANSACT_FAILED
        }

        return null;
    }

}
