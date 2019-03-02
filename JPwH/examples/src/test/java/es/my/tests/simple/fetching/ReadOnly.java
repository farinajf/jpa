/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple.fetching;

import es.my.jph.env.JPATest;
import es.my.jph.shared.util.CalendarUtil;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.fetching.readonly.Bid;
import es.my.model.entities.fetching.readonly.Item;
import es.my.model.entities.fetching.readonly.Usuario;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class ReadOnly extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    private FetchTestData _storeTestData() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();

        tx.begin();

        final EntityManager em = _JPA.createEntityManager();

        final Long[] itemIds = new Long[3];
        final Long[] userIds = new Long[3];

        final Usuario u1 = new Usuario("u-1");
        em.persist(u1);
        userIds[0] = u1.getId();

        final Usuario u2 = new Usuario("u-2");
        em.persist(u2);
        userIds[1] = u2.getId();

        final Usuario u3 = new Usuario("u-3");
        em.persist(u3);
        userIds[2] = u3.getId();

        {
            final Item item = new Item("i-1", CalendarUtil.MANHANA.getTime(), u1);
            em.persist(item);
            itemIds[0] = item.getId();

            for (int i = 1; i <= 3; i++)
            {
                final Bid b = new Bid(item, u3, new BigDecimal(12 + i));
                item.getBids().add(b);
                em.persist(b);
            }
        }

        {
            final Item item = new Item("i-2", CalendarUtil.MANHANA.getTime(), u1);
            em.persist(item);
            itemIds[1] = item.getId();

            for (int i = 1; i <= 1; i++)
            {
                final Bid b = new Bid(item, u2, new BigDecimal(31 + i));
                item.getBids().add(b);
                em.persist(b);
            }
        }

        {
            final Item item = new Item("i-3", CalendarUtil.MANHANA.getTime(), u2);
            em.persist(item);
            itemIds[2] = item.getId();
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
    public void configurePU() throws Exception {this.configurePU("myFetchingReadOnlyPUnit");}

    @Test
    public void entidadInmutable() throws Exception {
        final FetchTestData   testData = _storeTestData();
        final UserTransaction tx       = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID = testData.items.getPrimerId();

            final Item i = em.find(Item.class, ITEM_ID);

            // Bid es inmutable (anotada con @org.hibernate.annotations.Immutable
            // por tanto no se actualiza la base de datos
            for (Bid b : i.getBids()) b.setCantidad(new BigDecimal("1000"));

            em.flush();
            em.clear();

            final Item i2 = em.find(Item.class, ITEM_ID);

            Constants.print(i2);

            tx.commit();
            em.close();

            Constants.print("-----------------------------------------------");
        }
        finally {_TM.rollback();}
    }

    @Test
    public void readOnlySelectivo() throws Exception {
        final FetchTestData   testData = _storeTestData();
        final UserTransaction tx       = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em      = _JPA.createEntityManager();
            final Long          ITEM_ID = testData.items.getPrimerId();

            {
                em.unwrap(Session.class).setDefaultReadOnly(true);

                final Item i = em.find(Item.class, ITEM_ID);
                i.setNombre("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                em.flush(); //No se actualiza la BBDD

                Constants.print(" 1 ");
            }

            em.clear();

            {
                final Item i = em.find(Item.class, ITEM_ID);
                i.setNombre("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                em.unwrap(Session.class).setReadOnly(i, true);

                em.flush(); //No se actualiza la BBDD

                Constants.print(" 2 ");
            }

            em.clear();

            {
                org.hibernate.Query q = em.unwrap(Session.class).createQuery("SELECT i FROM Item i");

                List<Item> result = q.setReadOnly(true).list();

                for (Item x : result) x.setNombre("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx");

                em.flush(); //No se actualiza la BBDD

                Constants.print(" 3 - result.size: " + result.size());
            }

            em.clear();

            {
                final List<Item> result = em.createQuery("SELECT i FROM Item i").setHint(org.hibernate.annotations.QueryHints.READ_ONLY, true).getResultList();

                for (Item x : result) x.setNombre("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx");

                em.flush(); //No se actualiza la BBDD

                Constants.print(" 4 - result.size: " + result.size());
            }

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
