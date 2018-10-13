/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.model.Constants;
import es.my.model.entities.Item;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class CRUD extends JPATest {

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    private void _findItems(final String query) throws Exception {
        Constants.print("_findItems(" + query + ")");

        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            EntityManager em = JPA.createEntityManager();


            tx.begin();

            Query q = em.createNamedQuery(query);

            List<Item> result = q.getResultList();

            for (Item aux : result) System.out.println("result: " + aux);

            Assert.assertEquals(result.size(), 2);

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
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
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("mySimplePUnit");
    }

    @Test
    public void saveAndQuery() throws Exception {
        this.saveAndQuery("findItems");
    }

    public void saveAndQuery(final String query) throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item      x1 = new Item();
            x1.setNombre    ("Articulo-1");
            x1.setAuctionEnd(new Date(System.currentTimeMillis() + Constants.TIME_OFFSET_MS));

            Item      x2 = new Item();
            x2.setNombre    ("Articulo-2");
            x2.setAuctionEnd(new Date(System.currentTimeMillis() + Constants.TIME_OFFSET_MS));

            Constants.print("Persist X1");
            em.persist(x1);

            Constants.print("Persist X2");
            em.persist(x2);

            tx.commit();
            em.close();

            System.out.println("x1: " + x1);
            System.out.println("x2: " + x2);

            _findItems(query);
        }
        finally {_TM.rollback();}
    }
}
