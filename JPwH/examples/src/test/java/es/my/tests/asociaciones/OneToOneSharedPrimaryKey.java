/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.onetoone.sharedprimarykey.Direccion;
import es.my.model.entities.asociaciones.onetoone.sharedprimarykey.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToOneSharedPrimaryKey extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToOneSharedPrimaryKeyPUnit");}

    @Test
    public void test() throws Exception {
        final UserTransaction tx = _TM.getUserTransaction();
        final Long            direccionId;
        final Long            usuarioId;

        try
        {
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Direccion d = new Direccion("avenida", "12345", "ciudad");

                // Asigna el Id a la entidad Direccion
                em.persist(d);

                final Usuario x = new Usuario(d.getId(), "Juan");

                em.persist(x);

                x.setDireccion(d);

                tx.commit();
                em.close();

                usuarioId   = x.getId();
                direccionId = d.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario x = em.find(Usuario.class, usuarioId);

                // SELECT * FROM USUARIO WHERE ID=?
                // SELECT * FROM DIRECCION WHERE ID=?
                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
