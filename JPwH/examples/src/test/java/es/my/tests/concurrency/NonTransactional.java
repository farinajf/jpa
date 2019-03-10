/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.concurrency.version.Item;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class NonTransactional extends JPATest {

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
    public void configurePU() throws Exception {this.configurePU("myConcurrencyVersioningPUnit");}

    /**
     * Autocommit.
     *
     * @throws Throwable
     */
    @Test(groups = {"H2", "ORACLE", "POSTGRESQL"})
    public void demo1() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            ITEM_ID;

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();
            final Item          i  = new Item("I-1");

            em.persist(i);

            tx.commit();
            em.close();

            ITEM_ID = i.getId();
        }
        finally {_TM.rollback();}

        Constants.print("ITEM: " + ITEM_ID);
    }
}
