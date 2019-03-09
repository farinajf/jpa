/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.JPATest;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.concurrency.version.Categoria;
import es.my.model.entities.concurrency.version.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
    private ConcurrencyTestData _guardaCategoriasItems() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();

        tx.begin();

        final EntityManager       em     = _JPA.createEntityManager();
        final ConcurrencyTestData result = new ConcurrencyTestData();

        result.categorias = new TestData(new Long[3]);
        result.items      = new TestData(new Long[5]);

        for (int i = 1; i <= result.categorias.getIds().length; i++)
        {
            final Categoria c = new Categoria();

            c.setNombre("c-" + i);
            em.persist(c);

            result.categorias.getIds()[i-1] = c.getId();

            for (int j = 1; j <= result.categorias.getIds().length; j++)
            {
                final Item item = new Item("i-" + j);

                item.setCategoria(c);
                item.setPrecio   (new BigDecimal(45+j));

                em.persist(item);

                result.items.getIds()[(i-1) + (j-1)] = item.getId();
            }
        }

        tx.commit();
        em.close();

        return result;
    }

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

    /**
     * Comprueba la version de forma manual para conseguir REPEATABLE-READ
     *
     * @throws Throwable
     */
    @Test(expectedExceptions = org.hibernate.OptimisticLockException.class)
    public void demo2() throws Throwable {
        final ConcurrencyTestData testData = _guardaCategoriasItems();
        final Long[]              CAT      = testData.categorias.getIds();
        final UserTransaction     tx       = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            BigDecimal precioTotal = new BigDecimal(0);

            for (Long catId : CAT)
            {
                final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.categoria.id = :catId").setLockMode(LockModeType.OPTIMISTIC).setParameter("catId", catId).getResultList();

                for (Item item : items) precioTotal = precioTotal.add(item.getPrecio());

                if (catId.equals(testData.categorias.getPrimerId()))
                {
                    Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            final UserTransaction tx = _TM.getUserTransaction();

                            try
                            {
                                tx.begin();

                                final EntityManager em = _JPA.createEntityManager();

                                final List<Item> lista           = em.createQuery("SELECT i FROM Item i WHERE i.categoria.id = :catId").setParameter("catId", testData.categorias.getPrimerId()).getResultList();
                                final Categoria  ultimaCategoria = em.getReference(Categoria.class, testData.categorias.getUltimoId());

                                lista.iterator().next().setCategoria(ultimaCategoria);

                                tx.commit();
                                em.close();
                            }
                            catch (Exception e)
                            {
                                _TM.rollback();
                                throw new RuntimeException("ERROR: operacion concurrente: " + e, e);
                            }

                            return null;
                        }
                    }).get();
                }
            }
            /**
             * Para cada ITEM cargado con la consulta que establece el LOCK,
             * Hibernate ejecutara una SELECT durante el FLUSH, para comprobar si la Version del elemento
             * es el mismo.
             * Si las Versiones no coinciden, o se elimino el elemento de la tabla, se lanza una EXCEPTION (OptimisticLockException).
             */
            tx.commit();
            em.close();

            Constants.print("PRECIO TOTAL: " + precioTotal.toString());
        }
        catch (Exception e) {throw unwrapCauseOfType(e, org.hibernate.OptimisticLockException.class);}
        finally             {_TM.rollback();}
    }


    /**************************************************************************/
    /*                       Clases Internas                                  */
    /**************************************************************************/
    class ConcurrencyTestData {
        TestData categorias;
        TestData items;
    }
}
