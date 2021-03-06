/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.onetomany.bidirectional.Bid;
import es.my.model.entities.asociaciones.onetomany.bidirectional.Item;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class OneToManyBidirectional extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyBidirectionalPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item("Item-1");
                em.persist(x);

                final Bid b1 = new Bid(new BigDecimal("100.0"), x);
                x.getBids().add(b1);
                em.persist(b1);

                final Bid b2 = new Bid(new BigDecimal("200.0"), x);
                x.getBids().add(b2);
                em.persist(b2);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, id);

                //Hibernate ejecuta: SELECT * FROM BID WHERE ITEM_ID=?
                Constants.print("Size: " + x.getBids().size());

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                System.out.println("============================================================");
                Collection<Bid> bids = em.createQuery("select b from Bid b where b.item.id = :id").setParameter("id", id).getResultList();
                Constants.print("Size: " + bids.size());

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
