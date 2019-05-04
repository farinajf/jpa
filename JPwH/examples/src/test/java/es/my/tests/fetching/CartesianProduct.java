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
import es.my.model.entities.fetching.cartesianproduct.Bid;
import es.my.model.entities.fetching.cartesianproduct.Item;
import es.my.model.entities.fetching.cartesianproduct.Usuario;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class CartesianProduct extends JPATest {

    private FetchTestLoadEventListener _loadEventListener;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    /**
     *
     * @return
     * @throws Exception
     */
    private FetchTestData _storeTestData() throws Exception {
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
        item.getImagenes().add("1.jpg");
        item.getImagenes().add("2.jpg");
        item.getImagenes().add("3.jpg");
        em.persist(item);
        itemIds[0] = item.getId();
        for (int i = 1; i<=3; i++)
        {
            final Bid bid = new Bid(item, new BigDecimal(27+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("ITEM-2", CalendarUtil.MANHANA.getTime(), u1);
        item.getImagenes().add("x.jpg");
        item.getImagenes().add("y.jpg");
        em.persist(item);
        itemIds[1] = item.getId();
        for (int i = 1; i<=1; i++)
        {
            final Bid bid = new Bid(item,  new BigDecimal(7+i));
            item.getBids().add(bid);
            em.persist(bid);
        }

        item = new Item("ITEM-3", CalendarUtil.PASADO_MANHANA.getTime(), u2);
        em.persist(item);
        itemIds[2] = item.getId();

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
    public void configurePU() throws Exception {super.configurePU("myFetchingCartesianProductPUnit");}

    @Override
    public void afterJPABootstrap() throws Exception {
        _loadEventListener = new FetchTestLoadEventListener(_JPA.getEntityManagerFactory());
    }

    @Test
    public void fetchCollections() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final FetchTestData   td = _storeTestData();

        _loadEventListener.reset();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final long ITEM_ID = td.items.getPrimerId();

            // SELECT i.*, b.*, img.*, u.* FROM ITEM i
            //        LEFT OUTER JOIN BID b        ON i.ID = b.ITEM_ID
            //        LEFT OUTER JOIN IMAGENES img ON i.ID = img.ITEM_ID
            //        INNER JOIN USUARIO u         ON i.VENDEDOR = u.ID
            // WHERE i.ID = ?
            final Item item = em.find(Item.class, ITEM_ID);

            Constants.print("Item.class count: " + _loadEventListener.getLoadCount(Item.class));
            Constants.print("Bid.class count:  " + _loadEventListener.getLoadCount(Bid.class));

            em.detach(item);

            Constants.print("Item.imagenes.size: " + item.getImagenes().size());
            Constants.print("Item.bids.size:     " + item.getBids().size());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
