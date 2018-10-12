/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.test.simple;

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
        configurePersistenceUnit("SimplePU");
    }

    @Test
    public void storeAndQueryItems() throws Exception {
        storeAndQueryItems("findItems");
    }

    public void storeAndQueryItems(String queryName) throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item itemOne = new Item();
            itemOne.setNombre("Item One");
            itemOne.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));
            em.persist(itemOne);

            Item itemTwo = new Item();
            itemTwo.setNombre("Item Two");
            itemTwo.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));
            em.persist(itemTwo);

            tx.commit();
            em.close();

            tx.begin();
            em = JPA.createEntityManager();

            Query q = em.createNamedQuery(queryName);

            List<Item> items = q.getResultList();

            Assert.assertEquals(items.size(), 2);

            tx.commit();
            em.close();

        }
        finally {_TM.rollback();}
    }
}
