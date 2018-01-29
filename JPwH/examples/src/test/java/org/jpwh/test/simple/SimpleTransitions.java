/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import javax.persistence.EntityManager;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.jpwh.model.simple.Category;
import org.jpwh.model.simple.Item;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Java Persistence with Hibernate 2 Ed.
 * Ed. Manning
 * 
 * http://jpwh.org/examples/jpwh2/jpwh-2e-examples-20151103/examples/src/test/java/org/jpwh/test/simple/SimpleTransitions.java
 */
public class SimpleTransitions extends JPATest {
    @Override
    public void configurePersistenceUnit() throws Exception {configurePersistenceUnit("SimplePU");}

    @Test
    public void basicUOW() {
        EntityManager   em = null;
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();
            em = JPA.createEntityManager();

            Item item = new Item();
            item.setName("Some item");

            em.persist(item);

            tx.commit();
        }
        catch (Exception e)
        {
            try
            {
                if (tx.getStatus() == Status.STATUS_ACTIVE || tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) tx.rollback();
            }
            catch (IllegalStateException | SecurityException | SystemException ex)
            {
                System.err.println("Rollback of transation failed!!");
                ex.printStackTrace(System.err);
            }
            throw new RuntimeException(e);
        }
        finally {if (em != null && em.isOpen()) em.close();}
    }

    @Test
    public void makePersistent() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            EntityManager em = JPA.createEntityManager();

            tx.begin();

            Item item = new Item();

            item.setName("Some Item");

            // Make persistent
            em.persist(item);

            Category category = new Category();
            category.setName("Mi categoria");
            item.setCategory(category);

            em.persist(category);

            Long ITEM_ID = item.getId();
            System.out.println("-------------------------------------> " + ITEM_ID);

            tx.commit();
            em.close();

            /*-------------------------------------------------------------------*/
            tx.begin();

            em = JPA.createEntityManager();
            Item o = em.find(Item.class, ITEM_ID);

            System.out.println("----------------------------------------------------");
            System.out.println(o);
            System.out.println("----------------------------------------------------");

            Assert.assertEquals(o.getName(),               "Some Item");
            Assert.assertEquals(o.getCategory().getName(), "Mi categoria");

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
