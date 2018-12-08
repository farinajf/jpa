/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.ondeletecascade.Bid;
import es.my.model.entities.asociaciones.onetomany.ondeletecascade.Item;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class OneToManyOnDeleteCascade extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyOnDeleteCascadePUnit");}

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

                final List<Bid> result = em.createQuery("select b from Bid b where b.item.id = :x").setParameter("x", id).getResultList();

                System.out.println("==================== 1 ========================================");
                System.out.println("item.getBids().size(): " + result.size());

                em.close();
                tx.commit();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, id);

                System.out.println("======================REMOVE ON CASCADE======================================");
                //Bids son borradas de la base de datos en cascada
                em.remove(x);

                tx.commit();
                em.close();
            }
            {
                tx.begin();
                final EntityManager em = _JPA.createEntityManager();

                final List<Bid> result = em.createQuery("select b from Bid b where b.item.id = :x").setParameter("x", id).getResultList();

                System.out.println("==================== 2 ========================================");
                System.out.println("item.getBids().size(): " + result.size());

                em.close();
                tx.commit();
            }
        }
        finally {_TM.rollback();}
    }
}
