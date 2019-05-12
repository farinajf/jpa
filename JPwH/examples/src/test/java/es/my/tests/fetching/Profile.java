/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.fetching;

import es.my.jph.env.JPATest;
import es.my.jph.shared.util.CalendarUtil;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.fetching.profile.Bid;
import es.my.model.entities.fetching.profile.Item;
import es.my.model.entities.fetching.profile.Usuario;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 * Mediete el uso de Profiles se puede sobreescribir un fetch plan global.
 * Aunque representa un solucion de optimizacion sencilla es adecuada para
 * aplicaciones de tamanho pequenho o medio.
 *
 * @author fran
 */
public class Profile extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    /**
     *
     * @return
     * @throws Exception
     */
    private FetchTestData _store() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final EntityManager   em = _JPA.createEntityManager();

        tx.begin();

        Long[] itemIds = new Long[3];
        Long[] userIds = new Long[3];

        final Usuario u1 = new Usuario("U-1");
        em.persist(u1);
        userIds[0] = u1.getId();

        final Usuario u2 = new Usuario("U-2");
        em.persist(u2);
        userIds[1] = u2.getId();

        final Usuario u3 = new Usuario("U-3");
        em.persist(u3);
        userIds[2] = u3.getId();

        Item item = new Item("ITEM-1", CalendarUtil.MANHANA.getTime(), u1);
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i<=3; i++)
        {
            final Bid bid = new Bid(item, u3, new BigDecimal(27+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("ITEM-2", CalendarUtil.MANHANA.getTime(), u1);
        em.persist(item);
        itemIds[1] = item.getId();
        for (int i = 1; i<=1; i++)
        {
            final Bid bid = new Bid(item, u2, new BigDecimal(7+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("ITEM-3", CalendarUtil.PASADO_MANHANA.getTime(), u2);
        em.persist(item);
        itemIds[2] = item.getId();
        for (int i = 1; i<=1; i++)
        {
            final Bid bid = new Bid(item, u1, new BigDecimal(3+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        tx.commit();
        em.close();

        final FetchTestData result = new FetchTestData();

        result.items    = new TestData(itemIds);
        result.usuarios = new TestData(userIds);

        return result;
    }

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
    public void configurePU() throws Exception {super.configurePU("myFetchingProfilePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final FetchTestData   td = _store();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID = td.items.getPrimerId();

            // 1.- SELECT * FROM Item WHERE id = ?
            Item item = em.find(Item.class, ITEM_ID);

            Constants.print("Item.vendedor.isInitialized: " + Hibernate.isInitialized(item.getVendedor()));

            em.clear();

            // 2.- SELECT i.*, u.* FROM Item i
            //        INNER JOIN USUARIOS u ON i.VENDEDOR_ID = u.ID
            //    WHERE i.ID = ?
            em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_SELLER);

            item = em.find(Item.class, ITEM_ID);

            em.clear();

            Constants.print("Item.vendedor.nombre: " + item.getVendedor().getNombre());

            // 3.- SELECT i.*, u.*, b.* FROM Item i
            //        LEFT OUTER JOIN Bid b      ON i.ID = b.ITEM_ID
            //        INNER JOIN      USUARIOS u ON i.VENDEDOR_ID = u.ID
            //    WHERE i.ID = ?
            em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_BIDS);

            item = em.find(Item.class, ITEM_ID);

            em.clear();

            Constants.print("Item.vendedor.nombre: " + item.getVendedor().getNombre());
            Constants.print("Item.bids.size:       " + item.getBids().size());

            // 4.-
            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
