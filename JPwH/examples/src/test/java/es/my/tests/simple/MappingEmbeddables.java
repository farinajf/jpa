/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.simple;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.Direccion;
import es.my.model.entities.Usuario;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class MappingEmbeddables extends JPATest {
    private static final Logger LOG = Logger.getLogger(MappingEmbeddables.class.getName());

    @Override
    public void configurePU() throws Exception {this.configurePU("mySimplePUnit");}

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
    /**
     *
     * @throws Exception
     */
    @Test
    public void storeAndLoad() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            id;

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Usuario   u  = new Usuario();
            final Direccion d  = new Direccion("Miguel de Cervantes", "12345", "A Coruña");
            final Direccion df = new Direccion("Hernán Cortes",       "00000", "Vigo");

            u.setName             ("JUAN");
            u.setDireccion        (d);
            u.setDireccionFacturas(df);

            em.persist(u);

            tx.commit();
            em.close();

            id = u.getId();
        }
        finally {_TM.rollback();}

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Usuario u = em.find(Usuario.class, id);

            Assert.assertEquals(u.getName(), "JUAN");

            Constants.print(u);

            tx.commit();
            em.close();
        }
        finally {_TM.rollback();}
    }

    /**
     * Cuando se ejecuta el FLUSH, hibernate intenta hacer un INSERT y falla porque
     * la PROVINCIA es NULL.
     *
     * @throws Exception
     * @throws Throwable
     */
    @Test(expectedExceptions = org.hibernate.exception.ConstraintViolationException.class)
    public void storeAndLoadInvalidObjects() throws Exception, Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            final Usuario   u  = new Usuario();
            final Direccion d  = new Direccion("Miguel de Cervantes", "12345", null); //PROVINCIA es Not Null
            final Direccion df = new Direccion("Hernán Cortes",       "00000", "Vigo");

            u.setName             ("JUAN");
            u.setDireccion        (d);
            u.setDireccionFacturas(df);

            em.persist(u);

            try
            {
                // El INSERT falla porque la PROVINCIA es nula.
                em.flush();

                // Note: If you try instead with tx.commit() and a flush side-effect,
                // you won't get the ConstraintViolationException.
                // Hibernate will catch it internally and simply mark the TX for rollback.

                Constants.print(u);
            }
            catch (Exception e) {throw unwrapCauseOfType(e, org.hibernate.exception.ConstraintViolationException.class);}
        }
        finally {_TM.rollback();}
    }
}
