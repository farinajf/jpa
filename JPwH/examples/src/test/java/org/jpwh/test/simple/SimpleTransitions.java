/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import es.my.model.entities.Categoria;
import es.my.model.entities.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jpwh.env.JPATest;
import org.jpwh.model.simple.Address;
import org.jpwh.model.simple.User;
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
            item.setNombre("Some item");

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

            item.setNombre("Some Item");

            // Make persistent
            em.persist(item);

            Categoria category = new Categoria();
            category.setNombre("Mi categoria");
            //item.setCategory(category);

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

            Assert.assertEquals(o.getNombre(),               "Some Item");
            //Assert.assertEquals(o.getCategory().getName(), "Mi categoria");

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
            someItem.setNombre("Some item");

            em.persist(someItem);

            tx.commit();
            em.close();

            long ITEM_ID = someItem.getId();

            {
                tx.begin();
                em = JPA.createEntityManager();

                //Hit the database if not already in persistence context
                Item item = em.find(Item.class, ITEM_ID);

                if (item != null) item.setNombre("New name");

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

            Assert.assertEquals(em.find(Item.class, ITEM_ID).getNombre(), "New name");

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
            someItem.setNombre("Some item");

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
            Assert.assertEquals(item.getNombre(), "Some item");
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

            someItem.setNombre("Some item");

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
            //Item item = em.getReference(Item.class, ITEM_ID);

            System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111");
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
            System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222");

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
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();
            someItem.setNombre("Some item");
            em.persist(someItem);

            tx.commit();
            em.close();

            final long ITEM_ID = someItem.getId();

            tx.begin();
            em = JPA.createEntityManager();

            Item item = em.find(Item.class, ITEM_ID);
            item.setNombre("Some name");
            System.out.println("...............................111111111............................");

            // Someone updates this row in the database!
            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    UserTransaction tx = _TM.getUserTransaction();
                    try
                    {
                        tx.begin();

                        EntityManager em = JPA.createEntityManager();

                        Session session = em.unwrap(Session.class);

                        session.doWork(new Work() {
                            @Override
                            public void execute(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement("update ITEM set name = ? where ID = ?");
                                ps.setString(1, "Concurrent Update Name");
                                ps.setLong(2, ITEM_ID);

                                /*
                                Alternative: you get a EntityNotFoundException on refresh
                                PreparedStatement ps = con.prepareStatement("delete from ITEM where ID = ?");
                                ps.setLong(1, ITEM_ID);
                                */

                                if (ps.executeUpdate() != 1) throw new SQLException("ITEM row was not updated");
                                System.out.println("...............................222222222............................");
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        _TM.rollback();
                        throw new RuntimeException("Concurrent operation failure: " + e, e);
                    }

                    return null;
                }
            }).get();

            String oldName = item.getNombre();

            System.out.println("...............................333333333............................");
            em.refresh(item);

            Assert.assertNotEquals(item.getNombre(), oldName);
            Assert.assertEquals(item.getNombre(), "Concurrent Update Name");

            System.out.println("...............................444444444............................");
            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    /**
     * Replica el objeto ITEM en otra base de datos.
     * @throws Exception
     */
    @Test(groups = {"H2", "POSTGRESQL", "ORACLE"})
    public void replicate() throws Exception {
        long ITEM_ID;

        try
        {
            UserTransaction tx = _TM.getUserTransaction();

            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();
            someItem.setNombre("Some item");

            em.persist(someItem);

            tx.commit();
            em.close();

            ITEM_ID = someItem.getId();
        }
        finally {_TM.rollback();}

        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();
            EntityManager emA = _getDatabaseA().createEntityManager();

            Item item = emA.find(Item.class, ITEM_ID);

            EntityManager emB = _getDatabaseB().createEntityManager();

            emB.unwrap(Session.class).replicate(item, org.hibernate.ReplicationMode.LATEST_VERSION);

            tx.commit();
            emA.close();
            emB.close();
        }
        finally {_TM.rollback();}
    }

    protected EntityManagerFactory _getDatabaseA() {return JPA.getEntityManagerFactory();}
    protected EntityManagerFactory _getDatabaseB() {return JPA.getEntityManagerFactory();}

    /**
     * Deshabilita FLUSH antes de ejecutar una QUERY
     * @throws Exception
     */
    @Test
    public void flushModeType() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        Long ITEM_ID;

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();
            someItem.setNombre("Original name");

            em.persist(someItem);

            tx.commit();
            em.close();

            ITEM_ID = someItem.getId();
        }
        finally {_TM.rollback();}

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item item = em.find(Item.class, ITEM_ID);
            item.setNombre("New name");

            //Disable flushing before queries
            em.setFlushMode(FlushModeType.COMMIT);

            System.out.println("...................................................");
            System.out.println(em.createQuery("select i.name from Item i where i.id = :id").setParameter("id", ITEM_ID).getSingleResult());
            System.out.println("...................................................");

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    @Test
    public void scopeOfIdentity() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item someItem = new Item();
            someItem.setNombre("Some item");

            em.persist(someItem);

            tx.commit();
            em.close();
            long ITEM_ID = someItem.getId();

            tx.begin();
            em = JPA.createEntityManager();

            Item a = em.find(Item.class, ITEM_ID);
            Item b = em.find(Item.class, ITEM_ID);

            Assert.assertTrue(a == b);
            Assert.assertTrue(a.equals(b));
            Assert.assertEquals(a.getId(), b.getId());

            tx.commit();
            em.close();
            //PC is gone, 'a' and 'b' are now references to instances in detached state!

            tx.begin();
            em = JPA.createEntityManager();

            Item c = em.find(Item.class, ITEM_ID);

            Assert.assertTrue(a != c);
            Assert.assertFalse(a.equals(c));
            Assert.assertEquals(a.getId(), c.getId());

            tx.commit();
            em.close();

            Set<Item> allItems = new HashSet<>();

            allItems.add(a);
            allItems.add(b);
            allItems.add(c);

            Assert.assertEquals(allItems.size(), 2);
        }
        finally {_TM.rollback();}
    }

    @Test
    public void detach() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            User someUSer = new User();

            someUSer.setUsername("juan");
            someUSer.setHomeAddress(new Address("Some street", "1234", "Some city"));

            em.persist(someUSer);

            tx.commit();
            em.close();

            long USER_ID = someUSer.getId();

            tx.begin();
            em = JPA.createEntityManager();

            User user = em.find(User.class, USER_ID);

            em.detach(user);

            Assert.assertFalse(em.contains(user));

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    @Test
    public void mergeDetached() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            User detachedUser = new User();

            detachedUser.setUsername("juan");
            detachedUser.setHomeAddress(new Address("Some street", "1234", "Some city"));

            em.persist(detachedUser);

            tx.commit();
            em.close();

            long USER_ID = detachedUser.getId();

            detachedUser.setUsername("fran");

            tx.begin();
            em = JPA.createEntityManager();

            User mergedUser = em.merge(detachedUser);
            // Discard 'detachedUser' reference after merging!

            // The 'mergedUser' is in persistent state
            mergedUser.setUsername("alejandro");

            tx.commit(); // UPDATE in database
            em.close();

            tx.begin();
            em = JPA.createEntityManager();

            User user = em.find(User.class, USER_ID);

            Assert.assertEquals(user.getUsername(), "alejandro");

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
