/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jpwh.test.simple;

import java.util.logging.Logger;
import org.jpwh.env.JPATest;

/**
 * 
 */
public class MappingEmbeddables extends JPATest {

    private static final Logger LOG = Logger.getLogger(MappingEmbeddables.class.getName());

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("SimplePU");
    }

//    @Test
//    public void storeAndLoadUsers() throws Exception {
//        UserTransaction tx = _TM.getUserTransaction();
//
//        try
//        {
//            tx.begin();
//            EntityManager em = JPA.createEntityManager();
//
//            User user = new User();
//            user.setUsername("Juan");
//
//            Address homeAddress = new Address("Miguel de Cervantes", "12345", "A Coruña");
//            user.setHomeAddress(homeAddress);
//
//            em.persist(user);
//            tx.commit();
//            em.close();
//
//            tx.begin();
//            em = JPA.createEntityManager();
//
//            User u = em.find(User.class, user.getId());
//
//            Assert.assertEquals(u.getUsername(), "Juan");
//
//            System.out.println(u.getHomeAddress().getCity());
//            System.out.println(u.getHomeAddress().getStreet());
//            System.out.println(u.getHomeAddress().getZipcode());
//
//            tx.commit();
//            em.close();
//        }
//        finally {_TM.rollback();}
//    }
//
//    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
//    public void storeAndLoadInvalidUsers() throws Exception, Throwable {
//        UserTransaction tx = _TM.getUserTransaction();
//
//        try
//        {
//            tx.begin();
//            EntityManager em = JPA.createEntityManager();
//
//            User user = new User();
//            user.setUsername("Juan");
//            user.setHomeAddress(new Address("Lope de Vega", "33123", null));
//
//            em.persist(user);
//
//            try
//            {
//                // Hibernate tries the INSERT but fails!!
//                em.flush();
//
//                // Note: If you try instead with tx.commit() and a flush side-effect,
//                // you won't get the ConstraintViolationException.
//                // Hibernate will catch it internally and simply mark the TX for rollback.
//            }
//            catch (Exception ex)
//            {
//                throw unwrapCauseOfType(ex, org.hibernate.exception.ConstraintViolationException.class);
//            }
//        }
//        finally {_TM.rollback();}
//    }
}
