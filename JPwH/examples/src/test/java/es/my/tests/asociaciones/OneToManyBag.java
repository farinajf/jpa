/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.bag.Bid;
import es.my.model.entities.asociaciones.onetomany.bag.Item;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToManyBag extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyBagPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item("X1");

                //1.- INSERT INTO ITEM (nombre, id) VALUES ...
                em.persist(x);

                final Bid b = new Bid(new BigDecimal("1144.10"), x);

                x.getBids().add(b);
                x.getBids().add(b);

                //2.- INSERT INTO BID (cantidad, item_id, id) VALUES ...
                em.persist(b);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                //3.- SELECT id, nombre FROM ITEM WHERE id=?
                final Item x = em.find(Item.class, id);

                System.out.println("----------------------- ADDING ----------------------");
                //4.- NO SELECT with bags.
                final Bid b = new Bid(new BigDecimal("1000.50"), x);

                x.getBids().add(b);

                System.out.println("----------------------- PRINT ----------------------");
                //5.- SELECT item_id, id, cantidad FROM BID WHERE item_id=?
                System.out.println(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
