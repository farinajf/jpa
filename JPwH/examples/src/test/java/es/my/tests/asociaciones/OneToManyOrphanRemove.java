/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.entities.asociaciones.onetomany.orphanremove.Bid;
import es.my.model.entities.asociaciones.onetomany.orphanremove.Item;
import es.my.model.entities.asociaciones.onetomany.orphanremove.Usuario;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class OneToManyOrphanRemove extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToManyOrphanRemovePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            itemId;
        final Long            userId;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario user = new Usuario();
                em.persist(user);

                final Item x = new Item("Item-1");
                em.persist(x); // Bids son persistidos automaticamente (CascadeType.PERSIST)

                final Bid b1 = new Bid(new BigDecimal("100.0"), x);
                b1.setBidder(user);
                x.getBids().add(b1);

                final Bid b2 = new Bid(new BigDecimal("200.0"), x);
                b2.setBidder(user);
                x.getBids().add(b2);

                tx.commit();
                em.close();

                itemId = x.getId();
                userId = user.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario user = em.find(Usuario.class, userId);
                System.out.println("============================================================");
                System.out.println("user.getBids().size(): " + user.getBids().size());

                final Item x = em.find(Item.class, itemId);
                System.out.println("============================================================");
                System.out.println("item.getBids().size(): " + x.getBids().size());

                System.out.println("===================== REMOVE BID FROM ITEM ==========================");
                final Bid b = x.getBids().iterator().next();

                //Bid es borrado de la base de datos (orphanRemoval = true)
                x.getBids().remove(b);

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario user = em.find(Usuario.class, userId);
                System.out.println("============================================================");
                System.out.println("user.getBids().size(): " + user.getBids().size());

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
