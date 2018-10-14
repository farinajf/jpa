/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.model.Constants;
import es.my.model.entities.Categoria;
import es.my.model.entities.Item;
import javax.persistence.EntityManager;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jpwh.env.JPATest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Java Persistence with Hibernate 2 Ed.
 * Ed. Manning
 *
 * http://jpwh.org/examples/jpwh2/jpwh-2e-examples-20151103/examples/src/test/java/org/jpwh/test/simple/SimpleTransitions.java
 */
public class SimpleTransactions extends JPATest {

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
    public void configurePersistenceUnit() throws Exception {this.configurePersistenceUnit("mySimplePUnit");}

    /**
     * Persistencia basica de una entidad.
     */
    @Test
    public void demo1() {
        UserTransaction tx = _TM.getUserTransaction();
        EntityManager   em = null;

        try
        {
            tx.begin();

            em = JPA.createEntityManager();

            Item x = new Item();
            x.setNombre("First item!!");

            em.persist(x);

            tx.commit();

            System.out.println("item: " + x);
        }
        catch (Exception e)
        {
            try
            {
                if (tx.getStatus() == Status.STATUS_ACTIVE || tx.getStatus() == Status.STATUS_MARKED_ROLLBACK) tx.rollback();
            }
            catch (IllegalStateException | SecurityException | SystemException ex)
            {
                System.err.println("Error en Rollback!!");
                ex.printStackTrace(System.err);
            }
            throw new RuntimeException(e);
        }
        finally {if (em != null && em.isOpen()) em.close();}
    }

    /**
     * Demo de insert y select.
     */
    @Test
    public void demo2() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();
        Long            ITEM_ID;

        try
        {
            {
                final EntityManager em = JPA.createEntityManager();

                tx.begin();

                final Item x = new Item();
                x.setNombre("item-01");

                em.persist(x);

                ITEM_ID = x.getId();

                final Categoria c = new Categoria();
                c.setNombre("cat-01");

                x.setCategoria(c);

                em.persist(c);

                Constants.print(x.toString());
                Constants.print(c.toString());

                tx.commit();
                em.close();

                System.out.println("------------------------------------------------------------------------------------");
            }
            {
                final EntityManager em = JPA.createEntityManager();

                tx.begin();

                final Item x = em.find(Item.class, ITEM_ID);

                Constants.print(x.toString());

                Assert.assertEquals(x.getNombre(),                "item-01");
                Assert.assertEquals(x.getCategoria().getNombre(), "cat-01");

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void demo3() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();
        Long            ITEM_ID;

        try
        {
            {
                final EntityManager em = JPA.createEntityManager();

                tx.begin();

                final Item x = new Item();
                x.setNombre("i-01");

                em.persist(x);

                ITEM_ID = x.getId();

                tx.commit();
                em.close();

                System.out.println("----------------------------------------------------------------------------------");
            }
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item x = em.find(Item.class, ITEM_ID);

                if (x != null) x.setNombre("xxx");

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item x1 = em.find(Item.class, ITEM_ID);
                final Item x2 = em.find(Item.class, ITEM_ID);

                System.out.println("x1: " + ((Object) x1).hashCode());
                System.out.println("x2: " + ((Object) x2).hashCode());

                Assert.assertTrue(x1 == x2);
                Assert.assertTrue(x1.equals(x2));
                Assert.assertTrue(x1.getId().equals(x2.getId()));

                tx.commit();
                em.close();
            }

            tx.begin();

            final EntityManager em = JPA.createEntityManager();

            Assert.assertEquals(em.find(Item.class, ITEM_ID).getNombre(), "xxx");

            tx.commit();

            em.close();
        }
        finally {_TM.rollback();}
    }
}
