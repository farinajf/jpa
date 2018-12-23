/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.list.Bid;
import es.my.model.entities.asociaciones.onetomany.list.Item;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToManyList extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Override
    public void configurePU() throws Exception {super.configurePU("myOneToManyListPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item("IT-1");

                em.persist(x);

                final Bid b1 = new Bid(new BigDecimal("1.50"), x);
                x.getBids().add(b1);

                final Bid b2 = new Bid(new BigDecimal("2.00"), x);
                x.getBids().add(0, b2);

                em.persist(b2);

                em.persist(b1);

                System.out.println("--------------------------- END --------------------");

                //1.- INSERT INTO Item (nombre, id) VALUES ...
                //2.- INSERT INTO Bid (cantidad, ID_ITEM, BID_INDICE, id) VALUES ...
                //3.- INSERT INTO Bid (cantidad, ID_ITEM, BID_INDICE, id) VALUES ...
                //4.- UPDATE Bid SET ID_ITEM=?, BID_INDICE=? WHERE id=?
                //5.- UPDATE Bid SET ID_ITEM=?, BID_INDICE=? WHERE id=?

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                //6.- SELECT id, nombre FROM Item WHERE id=?
                final Item x = em.find(Item.class, id);

                System.out.println("----------------------- PRINT -----------------------");

                //7.- SELECT ID_ITEM, id, BID_INDICE, cantidad FROM Bid WHERE ID_ITEM=?
                System.out.println(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
