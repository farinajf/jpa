/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.concurrency.versiontimestamp.Item;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

import static es.my.jph.env.TransactionManagerTest._TM;

/**
 *
 * @author fran
 */
public class VersioningTimestamp extends JPATest {

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
    public void configurePU() throws Exception {this.configurePU("myConcurrencyVersioningTimestampPUnit");}

    /**
     * First commit wins.
     */
    @Test(expectedExceptions = OptimisticLockException.class)
    public void demo1() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            ITEM_ID;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = new Item("x-1");

                em.persist(x);

                tx.commit();
                em.close();

                ITEM_ID = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item x = em.find(Item.class, ITEM_ID);

                x.setNombre("x-2");

                Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        final UserTransaction tx = _TM.getUserTransaction();

                        try
                        {
                            tx.begin();

                            final EntityManager em = _JPA.createEntityManager();

                            final Item i = em.find(Item.class, ITEM_ID);

                            i.setNombre("TH-1");

                            tx.commit();
                            em.close();
                        }
                        catch (Exception e)
                        {
                            _TM.rollback();
                            throw new RuntimeException("ERROR: thrad transaction: " + e, e);
                        }

                        return null;
                    }
                }).get();

                tx.commit();
                em.close();
            }
        }
        catch (Exception e)
        {
            Constants.print(e);
            throw unwrapCauseOfType(e, OptimisticLockException.class);
        }
        finally {_TM.rollback();}
    }
}
