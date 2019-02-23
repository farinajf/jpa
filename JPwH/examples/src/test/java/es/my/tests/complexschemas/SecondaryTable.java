/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.complexschemas;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.complexschemas.secondarytable.Direccion;
import es.my.model.entities.complexschemas.secondarytable.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class SecondaryTable extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("mySecondaryTablePUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            userId;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = new Usuario();
                u.setNombre("X-1");

                final Direccion d1 = new Direccion("C-1", "12345", "P-1");
                u.setDireccion(d1);

                em.persist(u);

                tx.commit();
                em.close();

                userId = u.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = em.find(Usuario.class, userId);

                Constants.print(u);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }

    @Test
    public void test2() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            userId;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = new Usuario();
                u.setNombre("X-1");

                final Direccion d1 = new Direccion("C-1", "12345", "P-1");
                u.setDireccion(d1);

                final Direccion d2 = new Direccion("C-2", "55555", "P-2");
                u.setDireccion2(d2);

                em.persist(u);

                tx.commit();
                em.close();

                userId = u.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario u = em.find(Usuario.class, userId);

                Constants.print(u);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
