/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.jpwh.model.simple.Bid;
import org.jpwh.model.simple.Item;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MappingManyToOne extends JPATest {

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("SimplePU");
    }

    @Test
    public void storeAndLoadBids() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();
            EntityManager em = JPA.createEntityManager();

            // Store in one persistence context (transaction)
            Item item = new Item();
            item.setName("Nuevo item");

            Bid firstBid  = new Bid(new BigDecimal("123.00"), item);
            Bid secondBid = new Bid(new BigDecimal("456.00"), item);

            // Order is important here, Hibernate isn't smart enough anymore!
            em.persist(item);
            em.persist(secondBid);
            em.persist(firstBid);

            tx.commit();
            em.close();

            tx.begin();
            em = JPA.createEntityManager();

            Long BID_ID = firstBid.getId();

            // Load in another persistence context
            Bid someBid = em.find(Bid.class, BID_ID);

            // Initializes the Item proxy because we call getId(), which is
            // not mapped as an identifier property (the field is!)
            Assert.assertEquals(someBid.getItem().getId(), item.getId());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
