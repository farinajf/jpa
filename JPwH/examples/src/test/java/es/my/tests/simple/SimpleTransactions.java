/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.model.Constants;
import es.my.model.entities.Categoria;
import es.my.model.entities.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceUnitUtil;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
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

    /**
     * EntityManager.getReference(): devuelve una entidad si existe en el contexto de persistencia, en caso
     * contrario devuelve un Proxy. No lanza una query contra la base de datos.
     * Para cargar los datos del proxy se puede hacer bajo demanda antes de cerrar el contexto de
     * persistencia, o bien llamando al metodo estatico: Hibernate.initialize().
     *
     * @throws Exception
     */
    @Test(expectedExceptions = org.hibernate.LazyInitializationException.class)
    public void getPersistenceReference() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();
        Long            ITEM_ID;

        try
        {
            {
                final EntityManager em = JPA.createEntityManager();

                tx.begin();

                final Item x = new Item();
                x.setNombre("x-01");

                em.persist(x);

                tx.commit();
                em.close();

                ITEM_ID = x.getId();

                Constants.print(x.toString());
            }
            {
                final EntityManager em = JPA.createEntityManager();

                tx.begin();

                /*
                If the persistence context already contains an Item with the given identifier,
                that Item instance is returned by getReference() without hitting the database.
                Furthermore, if no persistence instance with that identifier is currently managed,
                a hollow placeholder will be produced by Hibernate, a proxy.
                This menans getReference() will not access the database, and it doesn't return
                null, unlike find()
                */
                final Item x = em.getReference(Item.class, ITEM_ID);

                /*
                JPA offers PersistenceUnitUtil helper methods such as isLoaded() to detect
                if you are working with an uninitialized proxy.
                */
                final PersistenceUnitUtil puu = JPA.getEntityManagerFactory().getPersistenceUnitUtil();

                System.out.println("Is loaded: " + puu.isLoaded(x)); // X is loaded: FALSE!!
                Assert.assertFalse(puu.isLoaded(x));
                System.out.println("------------------------------------------------------------");

                /*
                Hibernate.initialize(): metodo estatico que inicializa los datos de un proxy.
                */
                //Hibernate.initialize(x);

                tx.commit();
                em.close();

                /*
                After the persistence context is closed, item is in detached state. If you
                do not initialize the proxy while the persistence context is still open,
                you get a lazyInitializationException if you access the proxy. You can't
                load data on-demand once the persistence context is closed. The solution
                is simple: Load the data before you close the persistence context.
                */
                Assert.assertEquals(x.getNombre(), "x-01");
            }
        }
        finally {_TM.rollback();}
    }

    /**
     * Una entidad debe estar totalmente inicializada durante las transacciones por su ciclo de vida.
     *
     * Cuando se llama al metodo EntityManager.remove() Hibernate lanza una SELECT para cargar los datos
     * de la entidad.
     *
     * @throws Exception
     */
    @Test
    public void makeTransient() throws Exception {
        UserTransaction tx = _TM.getUserTransaction();
        Long            ITEM_ID;

        try
        {
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item x = new Item();
                x.setNombre("X-01");

                em.persist(x);

                tx.commit();
                em.close();

                ITEM_ID = x.getId();
            }
            {
                System.out.println("--------------------------------------------------------------------");
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                /*
                    find(): ejecuta una SELECT para cargar la entidad de base de datos.
                    getReference(): devuelve un Proxy vacio.
                */
                //final Item x = em.find(Item.class, ITEM_ID);
                final Item x = em.getReference(Item.class, ITEM_ID);

                System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111");
                /*
                Calling remove() will queue the entity instance for deletion when the unit
                of work completes, it is now in removed state. If remove() is called
                on a proxy, Hibernate will execute a SELECT to load the data.
                An entity instance has to be fully initialized during life cycle transitions.
                You may have life cycle callback methods or an entity listener enabled,
                and the instance must pass through these interceptors to complete its full life cycle.
                */
                em.remove(x);

                // Una entidad en estado REMOVED ya no esta en el contexto de persistencia.
                Assert.assertFalse(em.contains(x));

                // Una entidad en estado REMOVED se puede incluir de nuevo en el contexto de persistencia
                //em.persist(x);

                // hibernate.use_identifier_rollback was enabled, it now looks like a transient instance
                System.out.println("x.getId(): " + x.getId());

                /*
                When the transaction commits, Hibernate synchronizes the state transitions with the
                database and executes the SQL DELETE. The JVM garbage collector detects that the item
                is no longer referenced by anyone and finally deletes the last trace of the data.
                */
                System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222");

                tx.commit();
                em.close();
            }
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item x = em.find(Item.class, ITEM_ID);

                Constants.print(String.valueOf(x));

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
    public void refresh() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item x = new Item();
                x.setNombre("x-1");

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();

                final EntityManager em = JPA.createEntityManager();

                final Item x = em.find(Item.class, id);
                x.setNombre("x-2");

                System.out.println(".......................1..................");

                // Lanza un thread
                Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        final UserTransaction tx = _TM.getUserTransaction();

                        try
                        {
                            tx.begin();

                            final EntityManager em = JPA.createEntityManager();

                            final Session s = em.unwrap(Session.class);

                            s.doWork(new Work() {
                                @Override
                                public void execute(Connection cnctn) throws SQLException {
                                    final PreparedStatement ps = cnctn.prepareStatement("UPDATE ITEM SET NOMBRE = ? WHERE ID = ?");

                                    ps.setString(1, "xxxxxx");
                                    ps.setLong  (2, id);

                                    /* Alternative: you get an EntityNotFoundException on refresh
                                       PreparedStatement ps = con.prepareStatement("delete from ITEM where ID = ?");
                                       ps.setLong(1, ITEM_ID);
                                     */
                                    if (ps.executeUpdate() != 1) throw new SQLException("ERROR actualizando item!!");
                                    Constants.print("......... 2 .........");
                                }
                            });

                            tx.commit();
                            em.close();
                        }
                        catch (Exception e)
                        {
                            _TM.rollback();
                            throw new RuntimeException("ERROR: thread failed " + e, e);
                        }

                        return null;
                    }
                }).get();

                final String nombre = x.getNombre();

                Constants.print("......... 3 .........");

                em.refresh(x);

                Assert.assertNotEquals(x.getNombre(), nombre);
                Assert.assertEquals   (x.getNombre(), "xxxxxx");

                Constants.print("......... 4 .........");
                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }

    /**
     * Replica el objeto ITEM en otra base de datos.
     *
     * @throws Exception
     */
    @Test(groups = {"H2", "POSTGRESQL", "ORACLE"})
    public void replicate() throws Exception {
        Long id;

        try
        {
            final UserTransaction tx = _TM.getUserTransaction();

            tx.begin();

            final EntityManager em = JPA.createEntityManager();

            final Item x = new Item();
            x.setNombre("x-1");

            em.persist(x);

            tx.commit();
            em.close();

            id = x.getId();
        }
        finally {_TM.rollback();}

        try
        {
            final UserTransaction tx = _TM.getUserTransaction();

            tx.begin();

            final EntityManager emA = _getDatabaseA().createEntityManager();

            final Item o = emA.find(Item.class, id);

            final EntityManager emB = _getDatabaseB().createEntityManager();

            emB.unwrap(Session.class).replicate(o, org.hibernate.ReplicationMode.LATEST_VERSION);

            tx.commit();
            emA.close();
            emB.close();
        }
        finally {_TM.rollback();}
    }

    protected EntityManagerFactory _getDatabaseA() {return JPA.getEntityManagerFactory();}
    protected EntityManagerFactory _getDatabaseB() {return JPA.getEntityManagerFactory();}

    /**
     * Deshabilita FLUSH antes de ejecutar una QUERY
     *
     * @throws Exception
     */
    @Test
    public void flushModeType() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            tx.begin();

            final EntityManager em = JPA.createEntityManager();

            final Item x = new Item();
            x.setNombre("x-1");

            em.persist(x);

            tx.commit();
            em.close();

            id = x.getId();
        }
        finally {_TM.rollback();}

        try
        {
            tx.begin();

            final EntityManager em = JPA.createEntityManager();

            final Item x = em.find(Item.class, id);
            x.setNombre("x-2");

            //Deshabilita FLUSH antes de las queries
            em.setFlushMode(FlushModeType.COMMIT);

            System.out.println("...................................................");
            System.out.println(em.createQuery("select i from Item i where i.id = :id").setParameter("id", id).getSingleResult());
            System.out.println("...................................................");

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void scopeOfIdentity() throws Exception {

    }
}
