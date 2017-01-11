package Bank;

import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import java.sql.*;

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

            String contaA = pedido[0]; //A Perde Dinheiro e Ganha Ações
            String contaB = pedido[1]; //B Ganha Dinheiro e Perde Ações
            String company = pedido[2];
            int amount = Integer.parseInt(pedido[3]);
            float price = Float.parseFloat(pedido[4]);

            txn.begin();


            DataSource ds = (DataSource) ctx.lookup("jdbc/bankaccounts");
            Connection c = ds.getConnection();
            Statement s = c.createStatement();
            ResultSet rs;

            float aBalance = 0; //Balance que A tem antes da transação.
            int bAmount = 0; //Ações que B tem antes da transação.

            rs = s.executeQuery("select Balance from Users where Username = " + contaA);
            while (rs.next()) aBalance = rs.getFloat("Balance");

            rs = s.executeQuery("select Amount from Actions where Users_Username = " +contaB+
                            " and Company = "+company);
            while (rs.next()) bAmount += rs.getInt("Amount");

            //Alterar Balance dos Utilizadores

            if (aBalance < amount * price) {
                //Print para debugging
                System.out.println("Transação no banco falhou por fundos insuficientes." + contaA + " " + contaB + " " + company);
                txn.rollback();
                System.out.println("TRANSACT_FAILED");
            } else {
                try {
                    s.executeUpdate("update Users set Balance = Balance - "
                            + amount * price + " where Username = " + contaA);

                    s.executeUpdate("update Users set Balance = Balance + "
                            + amount * price + " where Username = " + contaB);
                    //Print para debugging
                    System.out.println("Transação de dinheiro ok");
                } catch (SQLException se){
                    txn.rollback();
                    System.out.println("TRANSACT_FAILED");
                }
            }

            //Alterar tabela Actions
            //Em caso de sucesso, enviar TRANSACT_OK

            if (bAmount < amount) {
                txn.rollback();
                System.out.print("TRANSACT_FAILED");
            } else {

                try {
                    if (bAmount == amount) {
                        s.executeUpdate("delete from Actions where Users_Username = " + contaB +
                                " and Company = " + company);
                    } else {
                        s.executeUpdate("update Actions set Amount = Amount - " + amount + " where " +
                                "Users_Username = " + contaB + " and Company = " + company);
                    }

                    //Adicionar ações a A : Cria registo ou atualiza se o user já tiver ações na empresa
                    s.executeUpdate("insert into Actions values ("+company+","+amount+","+contaA+") " +
                            "on duplicate key update amount = amount + "+amount);

                    System.out.println("Transação de ações ok");

                } catch (SQLException se){
                    txn.rollback();
                    System.out.println("TRANSACT_FAILED");
                }

            }

            txn.commit();
            System.out.println("TRANSACT_OK");

        } catch (Exception e) {
            System.out.println("TRANSACT_FAILED");
        }

        return null;
    }

}
