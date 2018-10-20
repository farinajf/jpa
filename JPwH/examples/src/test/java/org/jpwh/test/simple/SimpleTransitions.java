/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import es.my.model.entities.Item;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Java Persistence with Hibernate 2 Ed.
 *
 * http://jpwh.org/examples/jpwh2/jpwh-2e-examples-20151103/examples/src/test/java/org/jpwh/test/simple/SimpleTransitions.java
 */
public class SimpleTransitions extends JPATest {
    @Override
    public void configurePersistenceUnit() throws Exception {configurePersistenceUnit("SimplePU");}

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

//    @Test
//    public void detach() throws Exception {
//        UserTransaction tx = _TM.getUserTransaction();
//
//        try
//        {
//            tx.begin();
//
//            EntityManager em = JPA.createEntityManager();
//
//            User someUSer = new User();
//
//            someUSer.setUsername("juan");
//            someUSer.setHomeAddress(new Address("Some street", "1234", "Some city"));
//
//            em.persist(someUSer);
//
//            tx.commit();
//            em.close();
//
//            long USER_ID = someUSer.getId();
//
//            tx.begin();
//            em = JPA.createEntityManager();
//
//            User user = em.find(User.class, USER_ID);
//
//            em.detach(user);
//
//            Assert.assertFalse(em.contains(user));
//
//            tx.commit();
//            em.close();
//        }
//        finally {_TM.rollback();}
//    }
//
//    @Test
//    public void mergeDetached() throws Exception {
//        UserTransaction tx = _TM.getUserTransaction();
//
//        try
//        {
//            tx.begin();
//
//            EntityManager em = JPA.createEntityManager();
//
//            User detachedUser = new User();
//
//            detachedUser.setUsername("juan");
//            detachedUser.setHomeAddress(new Address("Some street", "1234", "Some city"));
//
//            em.persist(detachedUser);
//
//            tx.commit();
//            em.close();
//
//            long USER_ID = detachedUser.getId();
//
//            detachedUser.setUsername("fran");
//
//            tx.begin();
//            em = JPA.createEntityManager();
//
//            User mergedUser = em.merge(detachedUser);
//            // Discard 'detachedUser' reference after merging!
//
//            // The 'mergedUser' is in persistent state
//            mergedUser.setUsername("alejandro");
//
//            tx.commit(); // UPDATE in database
//            em.close();
//
//            tx.begin();
//            em = JPA.createEntityManager();
//
//            User user = em.find(User.class, USER_ID);
//
//            Assert.assertEquals(user.getUsername(), "alejandro");
//
//            tx.commit();
//            em.close();
//        }
//        finally {_TM.rollback();}
//    }
}
