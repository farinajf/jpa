/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.filtering.cascade.Bid;
import es.my.model.entities.filtering.cascade.BillingDetails;
import es.my.model.entities.filtering.cascade.CuentaBancaria;
import es.my.model.entities.filtering.cascade.Item;
import es.my.model.entities.filtering.cascade.TarjetaCredito;
import es.my.model.entities.filtering.cascade.Usuario;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Cascade extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myFilteringCascadePUnit");}

    /**
     *
     * @throws Throwable
     */
    //@Test
    public void detatchMerge() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Long ITEM_ID;

            {
                final Usuario u1 = new Usuario("U-1");
                em.persist(u1);

                final Item i1 = new Item("I-1", u1);
                em.persist(i1);

                ITEM_ID = i1.getId();

                final Bid b1 = new Bid(i1, new BigDecimal("60.0"));
                i1.getBids().add(b1);
                em.persist(b1);

                final Bid b2 = new Bid(i1, new BigDecimal("70.0"));
                i1.getBids().add(b2);
                em.persist(b2);

                em.flush();
            }

            em.clear();

            final Item i = em.find(Item.class, ITEM_ID);

            Constants.print("Item: " + i.toString());

            em.detach(i);
            em.clear();

            i.setNombre("X-I-1");

            final Bid b3 = new Bid(i, new BigDecimal("90.0"));
            i.getBids().add(b3);

            // Hibernate busca el item primero en el contexto de persistencia.
            // Si no existe en el contexto de persistencia, lo carga de la base de datos.
            // Hibernate carga los bids asociados al Item en la misma query.
            // A continuacion copia el nuevo Bid en el Item y lo anhade al contexto
            // de persistencia.
            //
            // SELECT i.*, b.* FROM Item i
            // LEFT OUTER JOIN Bid b ON i.id = b.item_id
            // WHERE i.id=?
            //
            // INSERT INTO Bid (cantidad, item_id, id) VALUES (?,?,?)
            final Item imerge = em.merge(i);

            for (Bid o : imerge.getBids()) Constants.print("Bid.id: " + o.getId());

            em.flush();
            em.clear();

            final Item i2 = em.find(Item.class, ITEM_ID);

            Constants.print("Nuevo Item: " + i2.toString());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    /**
     *
     * @throws Throwable
     */
    //@Test
    public void refresh() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        Long USUARIO_ID;
        Long TARJETA_CREDITO_ID = null;

        try
        {
            tx.begin();

            EntityManager em = _JPA.createEntityManager();

            {
                final Usuario u = new Usuario("U-1");
                u.getBillingDetails().add(new TarjetaCredito("U-1", "11111111", "12", "1900"));
                u.getBillingDetails().add(new CuentaBancaria("U-1", "00000", "Bank-1", "00"));

                // CascadeType.PERSIST
                //
                // INSERT INTO USUARIOS       (nombre, id)                  VALUES (?, ?)
                // INSERT INTO BillingDetails (propietario, USUARIO_ID, id) VALUES (?, ?, ?)
                // INSERT INTO TarjetaCredito (anho, mes, numero, id)       VALUES (?, ?, ?, ?)
                // INSERT INTO BillingDetails (propietario, USUARIO_ID, id) VALUES (?, ?, ?)
                // INSERT INTO CuentaBancaria (banco, cuenta, swift, id)    VALUES (?, ?, ?, ?)
                //
                // UPDATE BillingDetails SET NUMERO_ID=? WHERE ID=?
                // UPDATE BillingDetails SET NUMERO_ID=? WHERE ID=?
                em.persist(u);
                em.flush();

                USUARIO_ID = u.getId();

                for (BillingDetails bd : u.getBillingDetails()) if (bd instanceof TarjetaCredito) TARJETA_CREDITO_ID = bd.getId();
            }

            Constants.print("USUARIO_ID:         " + USUARIO_ID);
            Constants.print("TARJETA_CREDITO_ID: " + TARJETA_CREDITO_ID);

            tx.commit();
            em.close();

            //2.-
            tx.begin();
            em = _JPA.createEntityManager();

            //
            // SELECT u FROM USUARIOS u WHERE u.id=?
            //
            final Usuario u2 = em.find(Usuario.class, USUARIO_ID);
            Constants.print("u2.id: " + u2.getId());

            //
            // SELECT b0, b1, b2 FROM BillingDetails b0
            //       LEFT OUTER JOIN TarjetaCredito b1 ON b0.id=b1.id
            //       LEFT OUTER JOIN CuentaBancaria b2 ON b0.id=b2.id
            // WHERE b0.USUARIO_ID=?
            //
            for (BillingDetails bd : u2.getBillingDetails()) Constants.print(bd.getPropietario());

            //Alguien modifica la información en la base de datos
            final Long USUARIO_AUX_ID         = USUARIO_ID;
            final Long TARJETA_CREDITO_AUX_ID = TARJETA_CREDITO_ID;

            Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    final UserTransaction tx = _TM.getUserTransaction();

                    try
                    {
                        tx.begin();

                        final EntityManager em = _JPA.createEntityManager();

                        em.unwrap(Session.class).doWork(new Work() {
                            @Override
                            public void execute(Connection connection) throws SQLException {
                                final PreparedStatement ps;

                                ps = connection.prepareStatement("UPDATE BillingDetails set propietario = ? WHERE USUARIO_ID = ?");
                                ps.setString(1, "XXXX-1");
                                ps.setLong  (2, USUARIO_AUX_ID);

                                ps.executeUpdate();
                            }
                        });

                        tx.commit();
                        em.close();
                    }
                    catch (Exception e) {_TM.rollback();}

                    return null;
                }
            }).get();

            Constants.print("---------------------REFRESH------------------------");
            //
            // SELECT t0, t1 FROM TarjetaCredito t0
            //        INNER JOIN BillingDetails t1 ON t0.id = t1.id
            // WHERE t0.id = ?
            //
            // SELECT t0, t1 FROM CuentaBancaria c0
            //        INNER JOIN BillingDetails c1 ON c0.id = c1.id
            // WHERE c0.id = ?
            //
            // SELECT u, b, b1, b2 FROM USUARIO u
            //        LEFT OUTER JOIN BillingDetails b  ON u0.id = b.USUARIO_ID
            //        LEFT OUTER JOIN TarjetaCredito b1 ON b.id = b1.id
            //        LEFT OUTER JOIN CuentaBancaria b2 ON b.id = b2.id
            // WHERE u.id = ?
            //
            em.refresh(u2);

            for (BillingDetails bd : u2.getBillingDetails()) Constants.print("bd.propietario: " + bd.getPropietario());

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    /**
     *
     * @throws Throwable
     */
    @Test
    public void replicate() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        Long USUARIO_ID;
        Long ITEM_ID;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = new Usuario("U-1");
                em.persist(u);
                USUARIO_ID = u.getId();

                final Item i = new Item("I-1", u);
                em.persist(i);
                ITEM_ID = i.getId();

                tx.commit();
                em.close();
            }

            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            // SELECT i FROM Item i WHERE i.id = ?
            final Item i = em.find(Item.class, ITEM_ID);

            // SELECT u FROM USUARIOS u WHERE u.id = ?
            Constants.print("Vendedor: " + i.getVendedor().getNombre());

            tx.commit();
            em.close();

            tx.begin();
            final EntityManager oem = _JPA.createEntityManager();

            // SELECT id FROM Item     WHERE id = ?
            // SELECT id FROM USUARIOS WHERE id = ?
            oem.unwrap(Session.class).replicate(i, ReplicationMode.OVERWRITE);

            Constants.print("__________COMMIT____________");

            // UPDATE Item     SET nombre = ?, VENDEDOR_ID = ? WHERE id = ?
            // UPDATE USUARIOS SET nombre = ?                  WHERE id = ?
            tx.commit();
            oem.close();
        }
        finally {_TM.rollback();}
    }
}
