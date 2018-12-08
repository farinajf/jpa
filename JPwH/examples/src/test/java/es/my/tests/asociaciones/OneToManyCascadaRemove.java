/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.cascadaremove.Bid;
import es.my.model.entities.asociaciones.onetomany.cascadaremove.Item;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class OneToManyCascadaRemove extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyCascadaRemovePUnit");}

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
                em.persist(x); // Bids son persistidos automaticamente (CascadeType.PERSIST)

                final Bid b1 = new Bid(new BigDecimal("100.0"), x);
                x.getBids().add(b1);

                final Bid b2 = new Bid(new BigDecimal("200.0"), x);
                x.getBids().add(b2);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();
                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, id);

                System.out.println("============================================================");
                // Bids no son borrados de base de datos
                x.getBids().clear();

                em.persist(x);

                em.close();
                tx.commit();
            }
            {
                tx.begin();
                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, id);

                System.out.println("============================================================");

                em.remove(x);

                em.close();
                tx.commit();
            }
        }
        finally {_TM.rollback();}
    }
}
