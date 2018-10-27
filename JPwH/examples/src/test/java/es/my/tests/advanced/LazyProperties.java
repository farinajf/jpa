/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.advanced;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.Item2;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class LazyProperties extends JPATest {

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
    public void configurePU() throws Exception {this.configurePU("myAdvancedPUnit");}

    /**
     * Store and load properties.
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();
                final EntityManager em = _JPA.createEntityManager();

                final Item2 x = new Item2();

                x.setNombre("X1");
                x.setDesc  ("Pimer ejemplar del producto.");

                final byte[] bytes = new byte[131072];
                new Random().nextBytes(bytes);

                x.setImagen(bytes);

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();
            }
            {
                tx.begin();
                final EntityManager em = _JPA.createEntityManager();

                final Item2 x = em.find(Item2.class, id);

                // Accessing one initializes all lazy properties in a single SELECT
                Constants.print(x.getDesc());
                Constants.print(x.getImagen().length);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }

    /**
     * Usa un Locator para almacenar binarios.
     *
     * No funciona en H2:
     *           http://groups.google.com/group/h2-database/browse_thread/thread/9c6f4893a62c9b1a
     */
    @Test
    public void test2() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final byte[] bytes = new byte[131072];
                new Random().nextBytes(bytes);

                final InputStream is = new ByteArrayInputStream(bytes);
                final int longitud = bytes.length;

                final Item2 x = new Item2();

                x.setNombre("X1");
                x.setDesc  ("Pimer ejemplar del producto.");

                final Session session = em.unwrap(Session.class);

                final java.sql.Blob b = session.getLobHelper().createBlob(is, longitud);

                x.setImagenBlob(b);

                em.persist(x);

                tx.commit();
                em.close();

                id = x.getId();

                Constants.print(id);
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Item2 x = em.find(Item2.class, id);

                Constants.print(x);

                final InputStream is = x.getImagenBlob().getBinaryStream();
                System.out.println(".........................................");

                final ByteArrayOutputStream result = new ByteArrayOutputStream();
                org.hibernate.engine.jdbc.StreamUtils.copy(is, result);

                final byte[] imagen = result.toByteArray();

                Constants.print("Longitud: " + imagen.length);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
