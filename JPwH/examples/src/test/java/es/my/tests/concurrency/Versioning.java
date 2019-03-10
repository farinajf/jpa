/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.JPATest;
import es.my.jph.shared.util.TestData;
import es.my.model.Constants;
import es.my.model.entities.concurrency.version.Bid;
import es.my.model.entities.concurrency.version.Categoria;
import es.my.model.entities.concurrency.version.InvalidBidException;
import es.my.model.entities.concurrency.version.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
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
    private TestData _guardaItemBids() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();

        tx.begin();

        final EntityManager em = _JPA.createEntityManager();

        final Long[] ids  = new Long[1];
        final Item   item = new Item("I-1");

        em.persist(item);

        ids[0] = item.getId();

        for (int i = 1; i <= 3; i++) em.persist(new Bid(new BigDecimal(24 + i), item));

        tx.commit();
        em.close();

        return new TestData(ids);
    }

    private Bid _getHighestBid(final EntityManager em, final Item item) {
        try
        {
            return (Bid) em.createQuery("SELECT b FROM Bid b WHERE b.item = :item ORDER BY b.cantidad DESC").setParameter("item", item).setMaxResults(1).getSingleResult();
        }
        catch (NoResultException e) {return null;}
    }

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/
    protected ConcurrencyTestData _guardaCategoriasItems() throws Exception {
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

                Constants.print("DEMO 1 => Version: " + i.getVersion());

                i.setNombre("I-2");

                // Este Thread modifica el mismo ITEM_ID y realiza el commit primero
                Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        final UserTransaction tx = _TM.getUserTransaction();

                        try
                        {
                            tx.begin();

                            final EntityManager em = _JPA.createEntityManager();
                            final Item          i  = em.find(Item.class, ITEM_ID);

                            i.setNombre("TH-1");

                            tx.commit();
                            em.close();
                        }
                        catch (Exception e)
                        {
                            _TM.rollback();
                            throw new RuntimeException("ERROR: thrad transaction: " + e, e);
                        }

                        Constants.print("DEMO 1 => Item modificado en el Thread: " + i.toString());
                        return null;
                    }
                }).get();

                Constants.print("DEMO 1 => Saltara una excepcion por el bloqueo optimista.");
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

        Constants.print("DEMO 2 => ");

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            BigDecimal precioTotal = new BigDecimal(0);

            for (Long catId : CAT)
            {
                Constants.print("DEMO 2 => catId: " + catId);

                // LockModeType.OPTIMISTIC: Hibernate ejecutara una SELECT durante el FLUSH, para comprobar si la Version del elemento es el mismo.
                final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.categoria.id = :catId")
                        .setLockMode(LockModeType.OPTIMISTIC)
                        .setParameter("catId", catId)
                        .getResultList();

                for (Item item : items) precioTotal = precioTotal.add(item.getPrecio());

                if (catId.equals(testData.categorias.getPrimerId()))
                {
                    // Mediante este hilo se modifica la categoria de un item.
                    // Por tanto se modifica la version de este ITEM
                    Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            final UserTransaction tx = _TM.getUserTransaction();

                            try
                            {
                                tx.begin();

                                final EntityManager em = _JPA.createEntityManager();

                                final List<Item> lista = em.createQuery("SELECT i FROM Item i WHERE i.categoria.id = :catId")
                                        .setParameter("catId", testData.categorias.getPrimerId())
                                        .getResultList();

                                final Categoria  ultimaCategoria = em.getReference(Categoria.class, testData.categorias.getUltimoId());

                                final Item aux = lista.iterator().next();
                                aux.setCategoria(ultimaCategoria);

                                Constants.print("DEMO 2 => AUX: " + aux);

                                tx.commit();
                                em.clear();

                                {
                                    tx.begin();
                                    Constants.print("DEMO 2 => AUX: " + em.find(Item.class, aux.getId()));
                                    tx.commit();
                                }
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
            Constants.print("DEMO 2 => Saltara una excepcion por el setLockMode(LockModeType.OPTIMISTIC).");
            tx.commit();
            em.close();

            Constants.print("PRECIO TOTAL: " + precioTotal.toString());
        }
        catch (Exception e)
        {
            Constants.print(e);
            throw unwrapCauseOfType(e, org.hibernate.OptimisticLockException.class);
        }
        finally {_TM.rollback();}
    }

    /**
     * Force Increment
     *
     * @throws Throwable
     */
    @Test(expectedExceptions = org.hibernate.StaleObjectStateException.class)
    public void demo3() throws Throwable {
        final TestData        testData = _guardaItemBids();
        final Long            ITEM_ID  = testData.getPrimerId();
        final UserTransaction tx       = _TM.getUserTransaction();

        Constants.print("DEMO 3 => ");

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            // LockModeType.OPTIMISTIC_FORCE_INCREMENT: se incrementa la version del Item incluso aunque no se modifique el Item.
            final Item item = em.find(Item.class, ITEM_ID, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            final Bid  hb   = _getHighestBid(em, item);

            Constants.print("DEMO 3 => item: " + item);

            // Este hilo vuelve a leer el mismo ITEM estebleciendo el modo de bloqueo: OPTIMISTIC_FORCE_INCREMENT
            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    final UserTransaction tx = _TM.getUserTransaction();

                    try
                    {
                        tx.begin();

                        final EntityManager em = _JPA.createEntityManager();

                        final Item item = em.find(Item.class, testData.getPrimerId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                        final Bid  hb   = _getHighestBid(em, item);

                        try
                        {
                            em.persist(new Bid(new BigDecimal("80.0"), item, hb));
                        }
                        catch (InvalidBidException e) {}

                        // Actualiza la VERSION del ITEM cargado
                        tx.commit();
                        em.close();
                    }
                    catch (Exception e)
                    {
                        _TM.rollback();
                        throw new RuntimeException("ERROR: executor operation: " + e, e);
                    }

                    return null;
                }
            }).get();

            Constants.print("DEMO 3 => Se crea un nuevo BID para ese ITEM.");

            try
            {
                em.persist(new Bid(new BigDecimal("90.0"), item, hb));
            }
            catch (InvalidBidException e) {}

            Constants.print("DEMO 3 => Debería saltar na excepcion.");

            // Actualiza la VERSION del ITEM cargado y salta un StaleObjectStateException
            tx.commit();
            em.close();
        }
        catch (Exception e)
        {
            Constants.print(e);

            final Throwable th = unwrapCauseOfType(e, org.hibernate.StaleObjectStateException.class);

            Constants.print(th);

            throw th;
        }
        finally {_TM.rollback();}
    }

    /**************************************************************************/
    /*                       Clases Internas                                  */
    /**************************************************************************/
    class ConcurrencyTestData {
        TestData categorias;
        TestData items;
    }
}
