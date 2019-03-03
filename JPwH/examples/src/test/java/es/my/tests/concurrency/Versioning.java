/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.concurrency.version.Item;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Versioning extends JPATest {

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

                final Item i = new Item("I-1");
                em.persist(i);

                tx.commit();
                em.close();

                ITEM_ID = i.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item i = em.find(Item.class, ITEM_ID);

                Constants.print("Version: " + i.getVersion());

                i.setNombre("I-2");

                //Threads
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

                em.flush();
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
