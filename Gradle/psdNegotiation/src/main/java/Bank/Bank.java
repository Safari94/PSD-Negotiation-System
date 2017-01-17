package Bank;

import co.paralleluniverse.actors.BasicActor;
import co.paralleluniverse.fibers.SuspendExecution;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import java.sql.*;
import java.util.Hashtable;

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

        System.out.println("Olá! Recebi: "+mess);

        try {
            Hashtable contextArgs = new Hashtable();
            contextArgs.put( Context.INITIAL_CONTEXT_FACTORY, "bitronix.tm.jndi.BitronixInitialContextFactory");
            Context ctx = new InitialContext(contextArgs);
            UserTransaction txn = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

            System.out.println("Contexto e Transação criados");

            String pedido[] = mess.split(" ");

            String contaA = pedido[0]; //A Perde Dinheiro e Ganha Ações
            String contaB = pedido[1]; //B Ganha Dinheiro e Perde Ações
            String company = pedido[2];
            int amount = Integer.parseInt(pedido[3]);
            float price = Float.parseFloat(pedido[4]);

            txn.begin();


            DataSource ds = (DataSource) ctx.lookup("jdbc:mysql://localhost:3306/psd16");
            Connection c = ds.getConnection();
            Statement s = c.createStatement();
            ResultSet rs;

            float aBalance = 0; //Balance que A tem antes da transação.
            int bAmount = 0; //Ações que B tem antes da transação.

            rs = s.executeQuery("select AccountBalance from Client where Username = " + contaA);
            while (rs.next()) aBalance = rs.getFloat("AccountBalance");

            rs = s.executeQuery("select Quantidade from Accoes where User = " +contaB+
                            " and NomeEmpresa = "+company);
            while (rs.next()) bAmount += rs.getInt("Quantidade");

            //Alterar Balance dos Utilizadores

            if (aBalance < amount * price) {
                //Print para debugging
                System.out.println("Transação no banco falhou por fundos insuficientes." + contaA + " " + contaB + " " + company);
                txn.rollback();
                System.out.println("TRANSACT_FAILED");
            } else {
                try {
                    s.executeUpdate("update Client set AccountBalance = AccountBalance - "
                            + amount * price + " where Username = " + contaA);

                    s.executeUpdate("update Client set AccountBalance = AccountBalance + "
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
                        s.executeUpdate("delete from Accoes where User = " + contaB +
                                " and NomeEmpresa = " + company);
                    } else {
                        s.executeUpdate("update Accoes set Quantidade = Quantidade - " + amount + " where " +
                                "User = " + contaB + " and NomeEmpresa = " + company);
                    }

                    //Adicionar ações a A : Cria registo ou atualiza se o user já tiver ações na empresa
                    s.executeUpdate("insert into Accoes values ("+company+","+amount+","+contaA+") " +
                            "on duplicate key update Quantidade = Quantidade + "+amount);

                    System.out.println("Transação de ações ok");

                } catch (SQLException se){
                    txn.rollback();
                    System.out.println("TRANSACT_FAILED");
                }

            }

            txn.commit();
            System.out.println("TRANSACT_OK");

        } catch (Exception e) {
            System.out.println("TRANSACT_FAILED : Ligação à base de dados?");
            System.out.println(e.getMessage());
        }

        return null;
    }

}
