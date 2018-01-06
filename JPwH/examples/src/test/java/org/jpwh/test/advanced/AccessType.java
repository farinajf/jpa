/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jpwh.test.advanced;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.jpwh.model.advanced.Item2;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class AccessType extends JPATest {

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
    public void configurePersistenceUnit() throws Exception {configurePersistenceUnit("AdvancedPU");}

    @Test
    public void storeLoadAccessType() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            EntityManager em = JPA.createEntityManager();

            Item2 someItem = new Item2();
            someItem.setName("Some item");
            someItem.setDescription("This is some description.");

            em.persist(someItem);

            tx.commit();
            em.close();

            Long ITEM_ID = someItem.getId();

            /******************************************************************/
            tx.begin();
            em = JPA.createEntityManager();

            Item2 item = em.find(Item2.class, ITEM_ID);

            Assert.assertEquals(item.getName(), "AUCTION: Some item");

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
