/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.fetching;

import es.my.jph.env.JPATest;
import es.my.jph.shared.FetchTestLoadEventListener;
import es.my.jph.shared.util.CalendarUtil;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.fetching.fetchloadgraph.Bid;
import es.my.model.entities.fetching.fetchloadgraph.Bid_;
import es.my.model.entities.fetching.fetchloadgraph.Item;
import es.my.model.entities.fetching.fetchloadgraph.Item_;
import es.my.model.entities.fetching.fetchloadgraph.Usuario;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.persistence.Subgraph;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class FetchLoadGraph extends JPATest {

    private FetchTestLoadEventListener _loadEventListener;

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
        Long[] bidIds  = new Long[3];

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
            bidIds[i-1] = bid.getId();
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
        result.bids     = new TestData(bidIds);

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
    public void configurePU() throws Exception {super.configurePU("myFetchingFetchLoadGraphPUnit");}

    @Override
    public void afterJPABootstrap() throws Exception {_loadEventListener = new FetchTestLoadEventListener(_JPA.getEntityManagerFactory());}

    //@Test
    public void loadItem() throws Exception {
        final FetchTestData   data    = _store();
        final PersistenceUtil pu      = Persistence.getPersistenceUtil();
        final long            ITEM_ID = data.items.getPrimerId();

        _loadEventListener.reset();

        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", em.getEntityGraph(Item.class.getSimpleName()));

                final Item i = em.find(Item.class, ITEM_ID, propiedades);

                Constants.print("Item id       is loaded: " + pu.isLoaded(i));             // true
                Constants.print("Item nombre   is loaded: " + pu.isLoaded(i, "nombre"));   // true
                Constants.print("Item fechaFin is loaded: " + pu.isLoaded(i, "fechaFin")); // true
                Constants.print("Item vendedor is loaded: " + pu.isLoaded(i, "vendedor")); // false
                Constants.print("Item bids     is loaded: " + pu.isLoaded(i, "bids"));     // false

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", itemGraph);

                final Item i = em.find(Item.class, ITEM_ID, propiedades);

                Constants.print("Item id       is loaded: " + pu.isLoaded(i));             // true
                Constants.print("Item nombre   is loaded: " + pu.isLoaded(i, "nombre"));   // true
                Constants.print("Item fechaFin is loaded: " + pu.isLoaded(i, "fechaFin")); // true
                Constants.print("Item vendedor is loaded: " + pu.isLoaded(i, "vendedor")); // false
                Constants.print("Item bids     is loaded: " + pu.isLoaded(i, "bids"));     // false

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
    }

    //@Test
    public void loadItemSeller() throws Exception {
        final FetchTestData   data    = _store();
        final PersistenceUtil pu      = Persistence.getPersistenceUtil();
        final long            ITEM_ID = data.items.getPrimerId();

        _loadEventListener.reset();

        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", em.getEntityGraph("ItemVendedor"));

                final Item i = em.find(Item.class, ITEM_ID, propiedades);

                Constants.print("Item id       is loaded: " + pu.isLoaded(i));             // true
                Constants.print("Item nombre   is loaded: " + pu.isLoaded(i, "nombre"));   // true
                Constants.print("Item fechaFin is loaded: " + pu.isLoaded(i, "fechaFin")); // true
                Constants.print("Item vendedor is loaded: " + pu.isLoaded(i, "vendedor")); // true
                Constants.print("Item bids     is loaded: " + pu.isLoaded(i, "bids"));     // false

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
                itemGraph.addAttributeNodes(Item_.vendedor);

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", itemGraph);

                final Item i = em.find(Item.class, ITEM_ID, propiedades);

                Constants.print("Item id       is loaded: " + pu.isLoaded(i));             // true
                Constants.print("Item nombre   is loaded: " + pu.isLoaded(i, "nombre"));   // true
                Constants.print("Item fechaFin is loaded: " + pu.isLoaded(i, "fechaFin")); // true
                Constants.print("Item vendedor is loaded: " + pu.isLoaded(i, "vendedor")); // true
                Constants.print("Item bids     is loaded: " + pu.isLoaded(i, "bids"));     // false

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
                itemGraph.addAttributeNodes("vendedor");

                final List<Item> items = em.createQuery("SELECT i FROM Item i").setHint("javax.persistence.loadgraph", itemGraph).getResultList();

                for (Item i : items)
                {
                    Constants.print("-----------------------------------------------------------------");
                    Constants.print("Item id       is loaded: " + pu.isLoaded(i));             // true
                    Constants.print("Item nombre   is loaded: " + pu.isLoaded(i, "nombre"));   // true
                    Constants.print("Item fechaFin is loaded: " + pu.isLoaded(i, "fechaFin")); // true
                    Constants.print("Item vendedor is loaded: " + pu.isLoaded(i, "vendedor")); // true
                    Constants.print("Item bids     is loaded: " + pu.isLoaded(i, "bids"));     // false
                }

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
    }

    //@Test
    public void loadBidBidderItem() throws Exception {
        final FetchTestData   data   = _store();
        final PersistenceUtil pu     = Persistence.getPersistenceUtil();
        final long            BID_ID = data.bids.getPrimerId();

        _loadEventListener.reset();

        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", em.createEntityGraph("BidBidderItem"));

                final Bid b = em.find(Bid.class, BID_ID, propiedades);

                Constants.print("Bid id            is loaded: " + pu.isLoaded(b));             // true
                Constants.print("Bid cantidad      is loaded: " + pu.isLoaded(b, "cantidad")); // true
                Constants.print("Bid bidder        is loaded: " + pu.isLoaded(b, "bidder"));   // true
                Constants.print("Bid item          is loaded: " + pu.isLoaded(b, "item"));     // true
                Constants.print("Bid item.nombre   is loaded: " + pu.isLoaded(b.getItem(), "nombre"));   // true
                Constants.print("Bid item.vendedor is loaded: " + pu.isLoaded(b.getItem(), "vendedor")); // false

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final EntityGraph<Bid> bidGraph = em. createEntityGraph(Bid.class);
                bidGraph.addAttributeNodes("bidder", "item");

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", bidGraph);

                final Bid b = em.find(Bid.class, BID_ID, propiedades);

                Constants.print("Bid id            is loaded: " + pu.isLoaded(b));             // true
                Constants.print("Bid cantidad      is loaded: " + pu.isLoaded(b, "cantidad")); // true
                Constants.print("Bid bidder        is loaded: " + pu.isLoaded(b, "bidder"));   // true
                Constants.print("Bid item          is loaded: " + pu.isLoaded(b, "item"));     // true
                Constants.print("Bid item.nombre   is loaded: " + pu.isLoaded(b.getItem(), "nombre"));   // true
                Constants.print("Bid item.vendedor is loaded: " + pu.isLoaded(b.getItem(), "vendedor")); // false

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
    }

    @Test
    public void loadBidBidderItemSellerBids() throws Exception {
        final FetchTestData   data   = _store();
        final PersistenceUtil pu     = Persistence.getPersistenceUtil();
        final long            BID_ID = data.bids.getPrimerId();

        _loadEventListener.reset();

        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", em.createEntityGraph("BidBidderItemSellerBids"));

                final Bid b = em.find(Bid.class, BID_ID, propiedades);

                Constants.print("Bid id            is loaded: " + pu.isLoaded(b));             // true
                Constants.print("Bid cantidad      is loaded: " + pu.isLoaded(b, "cantidad")); // true
                Constants.print("Bid bidder        is loaded: " + pu.isLoaded(b, "bidder"));   // true
                Constants.print("Bid item          is loaded: " + pu.isLoaded(b, "item"));     // true
                Constants.print("Bid item.nombre   is loaded: " + pu.isLoaded(b.getItem(), "nombre"));   // true
                Constants.print("Bid item.vendedor is loaded: " + pu.isLoaded(b.getItem(), "vendedor")); // true
                Constants.print("Bid item.vendedor.nombre is loaded: " + pu.isLoaded(b.getItem().getVendedor(), "nombre")); // true
                Constants.print("Bid item.bids     is loaded: " + pu.isLoaded(b.getItem(), "bids"));     // true

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
        {
            final UserTransaction tx = _TM.getUserTransaction();

            try
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
                bidGraph.addAttributeNodes(Bid_.bidder, Bid_.item);

                final Subgraph<Item> itemGraph = bidGraph.addSubgraph(Bid_.item);
                itemGraph.addAttributeNodes(Item_.vendedor, Item_.bids);

                final Map<String, Object> propiedades = new HashMap<>();

                propiedades.put("javax.persistence.loadgraph", bidGraph);

                final Bid b = em.find(Bid.class, BID_ID, propiedades);

                Constants.print("Bid id            is loaded: " + pu.isLoaded(b));             // true
                Constants.print("Bid cantidad      is loaded: " + pu.isLoaded(b, "cantidad")); // true
                Constants.print("Bid bidder        is loaded: " + pu.isLoaded(b, "bidder"));   // true
                Constants.print("Bid item          is loaded: " + pu.isLoaded(b, "item"));     // true
                Constants.print("Bid item.nombre   is loaded: " + pu.isLoaded(b.getItem(), "nombre"));   // true
                Constants.print("Bid item.vendedor is loaded: " + pu.isLoaded(b.getItem(), "vendedor")); // true
                Constants.print("Bid item.vendedor.nombre is loaded: " + pu.isLoaded(b.getItem().getVendedor(), "nombre")); // true
                Constants.print("Bid item.bids     is loaded: " + pu.isLoaded(b.getItem(), "bids"));     // true

                tx.commit();
                em.close();
            }
            finally {_TM.rollback();}
        }
    }
}
