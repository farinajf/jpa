/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
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

    @Test
    public void retrievePersistent() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();
            someItem.setName("Some item");

            em.persist(someItem);

            tx.commit();
            em.close();

            long ITEM_ID = someItem.getId();

            {
                tx.begin();
                em = JPA.createEntityManager();

                //Hit the database if not already in persistence context
                Item item = em.find(Item.class, ITEM_ID);

                if (item != null) item.setName("New name");

                tx.commit(); //SQL UPDATE
                em.close();
            }
            {
                tx.begin();
                em = JPA.createEntityManager();

                Item itemA = em.find(Item.class, ITEM_ID);
                Item itemB = em.find(Item.class, ITEM_ID);

                Assert.assertTrue(itemA == itemB);
                Assert.assertTrue(itemA.equals(itemB));
                Assert.assertTrue(itemA.getId().equals(itemB.getId()));

                tx.commit();
                em.close();
            }

            tx.begin();
            em = JPA.createEntityManager();

            Assert.assertEquals(em.find(Item.class, ITEM_ID).getName(), "New name");

            tx.commit();

            em.close();
        }
        finally {_TM.rollback();}
    }

    @Test(expectedExceptions = org.hibernate.LazyInitializationException.class)
    public void retrievePersistentReference() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();
            someItem.setName("Some item");

            em.persist(someItem);

            tx.commit();
            em.close();

            long ITEM_ID = someItem.getId();

            tx.begin();
            em = JPA.createEntityManager();

            /*
            If the persistence context already contains an Item with the given identifier,
            that Item instance is returned by getReference() without hitting the database.
            Furthermore, if no persistence instance with that identifier is currently managed,
            a hollow placeholder will be produced by Hibernate, a proxy.
            This menans getReference() will not access the database, and it doesn't return
            null, unlike find()
            */
            Item item = em.getReference(Item.class, ITEM_ID);

            /*
            JPA offers PersistenceUnitUtil helper methods such as isLoaded() to detect
            if you are working with an uninitialized proxy.
            */
            PersistenceUnitUtil persistenceUnitUtil = JPA.getEntityManagerFactory().getPersistenceUnitUtil();

            Assert.assertFalse(persistenceUnitUtil.isLoaded(item));

            /*
            Hibernate has a convenient static initialize() method, loading the proxy's data
            */
            //Hibernate.initialize(item);

            tx.commit();
            em.close();

            /*
            After the persistence context is closed, item is in detached state. If you
            do not initialize the proxy while the persistence context is still open,
            you get a lazyInitializationException if you access the proxy. You can't
            load data on-demand once the persistence context is closed. The solution
            is simple: Load the data before you close the persistence context.
             */
            Assert.assertEquals(item.getName(), "Some item");
        }
        finally {_TM.rollback();}
    }

    @Test
    public void makeTransient() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();

            someItem.setName("Some item");

            em.persist(someItem);

            tx.commit();
            em.close();

            long ITEM_ID = someItem.getId();

            tx.begin();
            em = JPA.createEntityManager();

            /*
            If you call find(), Hibernate will execute a SELECT to load the Item.
            If you call getReference(), Hibernate will attempt to avoid the SELECT
            and return a proxy.
            */
            Item item = em.find(Item.class, ITEM_ID);
            //Item item = em.getReference(Item.class, ITEM_ID):

            /*
            Calling remove() will queue the entity instance for deletion when the unit
            of work completes, it is now in removed state. If remove() is called
            on a proxy, Hibernate will execute a SELECT to load the data.
            An entity instance has to be fully initialized during life cycle transitions.
            You may have life cycle callback methods or an entity listener enabled,
            and the instance must pass through these interceptors to complete its full life cycle.
            */
            em.remove(item);

            /*
            An entity in removed state is no longer in persitent state, this can be checked
            with the contains() operation.
            */
            Assert.assertFalse(em.contains(item));

            /*
            You can make the removed instance persistent again, cancelling the deletion.
            */
            //em.persist(item);

            // hibernate.use_identifier_rollback was enabled, it now looks like a transient instance
            Assert.assertNull(item.getId());

            /*
            When the transaction commits, Hibernate synchronizes the state transitions with the
            database and executes the SQL DELETE. The JVM garbage collector detects that the item
            is no longer referenced by anyone and finally deletes the last trace of the data.
            */

            tx.commit();
            em.close();

            tx.begin();
            em = JPA.createEntityManager();

            item = em.find(Item.class, ITEM_ID);

            Assert.assertNull(item);

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    @Test
    public void refresh() throws Exception {
        
    }
}
