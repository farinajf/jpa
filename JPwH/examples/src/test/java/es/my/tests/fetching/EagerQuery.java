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
import es.my.model.entities.fetching.nplusoneselects.Bid;
import es.my.model.entities.fetching.nplusoneselects.Item;
import es.my.model.entities.fetching.nplusoneselects.Usuario;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 * Dynamic EAGER fetching
 *
 * Estrategia consistente en establecer el modo EAGER en una consulta cuando la
 * estrategia global es LAZY.
 *
 * @author fran
 */
public class EagerQuery extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myFetchingNPlusOneSelectsPUnit");}

    /**
     *
     * @throws Exception
     */
    //@Test
    public void fetchUsers() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final FetchTestData   td = _store();

        try
        {
            tx.begin();

            {
                final EntityManager em = _JPA.createEntityManager();

                // SELECT i.*, u.* FROM Item i
                //    INNER JOIN USUARIOS u
                //          ON i.VENDEDOR_ID = u.ID
                final List<Item> items = em.createQuery("SELECT i FROM Item i JOIN FETCH i.vendedor").getResultList();

                em.close();

                for (Item i : items) Constants.print("Item.vendedor.nombre: " + i.getVendedor().getNombre());
            }
            {
                Constants.print("CRITERIA");

                final EntityManager   em = _JPA.createEntityManager();
                final CriteriaBuilder cb = em.getCriteriaBuilder();

                final CriteriaQuery criteria = cb.createQuery();

                final Root<Item> it = criteria.from(Item.class);
                it.fetch("vendedor");
                criteria.select(it);

                // SELECT i.*, u.* FROM Item i
                //    INNER JOIN USUARIOS u
                //          ON i.VENDEDOR_ID = u.ID
                final List<Item> items = em.createQuery(criteria).getResultList();

                em.close();

                for (Item i : items) Constants.print("Item.vendedor.nombre: " + i.getVendedor().getNombre());
            }

            tx.commit();
        }
        finally {_TM.rollback();}
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void fetchBids() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final FetchTestData   td = _store();

        try
        {
            tx.begin();

            {
                final EntityManager em = _JPA.createEntityManager();

                // SELECT i.*, b.* FROM Item i
                //   LEFT OUTER JOIN Bid b
                //        ON i.ID = b.ITEM_ID
                final List<Item> items = em.createQuery("SELECT i FROM Item i LEFT JOIN FETCH i.bids").getResultList();
                //final List<Item> items = em.createQuery("SELECT i FROM Item i").getResultList();

                em.close();

                for (Item i : items) Constants.print("Item.bids.size: " + i.getNombre() + " - " + i.getBids().size());
            }
            {
                Constants.print("CRITERIA");

                final EntityManager   em = _JPA.createEntityManager();
                final CriteriaBuilder cb = em.getCriteriaBuilder();

                final CriteriaQuery criteria = cb.createQuery();

                final Root<Item> it = criteria.from(Item.class);
                it.fetch("bids", JoinType.LEFT);
                criteria.select(it);

                // SELECT i.*, b.* FROM Item i
                //   LEFT OUTER JOIN Bid b
                //        ON i.ID = b.ITEM_ID
                final List<Item> items = em.createQuery(criteria).getResultList();

                em.close();

                for (Item i : items) Constants.print("Item.bids.size: " + i.getNombre() + " - " + i.getBids().size());
            }

            tx.commit();
        }
        finally {_TM.rollback();}
    }
}
