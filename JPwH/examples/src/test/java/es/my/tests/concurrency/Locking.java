/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.concurrency;

import es.my.jph.env.DatabaseProduct;
import es.my.model.Constants;
import es.my.model.entities.concurrency.version.Categoria;
import es.my.model.entities.concurrency.version.Item;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Locking extends Versioning {

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
    @Test
    public void pessimisticReadWrite() throws Exception {
        final ConcurrencyTestData testData = _guardaCategoriasItems();
        final Long[]              CAT      = testData.categorias.getIds();
        final UserTransaction     tx       = _TM.getUserTransaction();

        Constants.print("DEMO P-1 => ");

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            BigDecimal precioTotal = new BigDecimal(0);

            for (Long catId : CAT)
            {
                // Se establece un bloqueo sobre las filas de base de datos: SELECT ... FOR UPDATE
                // Si ya existe un bloqueo, espera 5 segundos.
                final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.categoria.id = :catId")
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .setHint("javax.persistence.lock.timeout", 5000)
                        .setParameter("catId", catId)
                        .getResultList();

                for (Item item : items) precioTotal = precioTotal.add(item.getPrecio());

                Constants.print("DEMO P-1 => catId: " + catId + " precio total: " + precioTotal);

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

                                if (_TM.getDatabaseProduct().equals(DatabaseProduct.POSTGRESQL))
                                {
                                    em.unwrap(Session.class).doWork(new Work() {
                                        @Override
                                        public void execute(Connection connection) throws SQLException {
                                            connection.createStatement().execute("SET STATEMENT_TIMEOUT = 5000");
                                        }
                                    });
                                }
                                if (_TM.getDatabaseProduct().equals(DatabaseProduct.MYSQL))
                                {
                                    em.unwrap(Session.class).doWork(new Work() {
                                        @Override
                                        public void execute(Connection connection) throws SQLException {
                                            connection.createStatement().execute("set innodb_lock_wait_timeout = 5;");
                                        }
                                    });
                                }

                                // OJO: Hay un bloqueo establecido.
                                Constants.print("DEMO P-1 => before: " + new Date());

                                final List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.categoria.id = :catId")
                                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                        .setHint("javax.persistence.lock.timeout", 5000)
                                        .setParameter("catId", testData.categorias.getPrimerId())
                                        .getResultList();

                                Constants.print("DEMO P-1 => after: " + new Date());

                                // No se llega a ejecutar
                                items.iterator().next().setCategoria(em.getReference(Categoria.class, testData.categorias.getUltimoId()));

                                tx.commit();
                                em.close();
                            }
                            catch (Exception e)
                            {
                                _TM.rollback();

                                Constants.print(e);

                                if      (_TM.getDatabaseProduct().equals(DatabaseProduct.POSTGRESQL)) Assert.assertTrue(e instanceof javax.persistence.PersistenceException);
                                else if (_TM.getDatabaseProduct().equals(DatabaseProduct.MYSQL))      Assert.assertTrue(e instanceof javax.persistence.LockTimeoutException);
                                else if (_TM.getDatabaseProduct().equals(DatabaseProduct.ORACLE))     Assert.assertTrue(e instanceof javax.persistence.PessimisticLockException);
                                else if (_TM.getDatabaseProduct().equals(DatabaseProduct.H2))         Assert.assertTrue(e instanceof javax.persistence.PessimisticLockException);
                            }
                            return null;
                        }
                    }).get();
                }
            }

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }
}
