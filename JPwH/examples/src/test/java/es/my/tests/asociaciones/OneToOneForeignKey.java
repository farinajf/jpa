/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.asociaciones;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.asociaciones.onetoone.foreignkey.Direccion;
import es.my.model.entities.asociaciones.onetoone.foreignkey.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class OneToOneForeignKey extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myOneToOneForeignKeyPUnit");}

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

                final Usuario   x = new Usuario("Cristina");
                final Direccion d = new Direccion("Avd. Isaac Peral", "12345", "Ourense");

                x.setDireccion(d);

                em.persist(x);

                tx.commit();
                em.close();

                usuarioId   = x.getId();
                direccionId = d.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario x = em.find(Usuario.class, usuarioId);
                Constants.print(x);

                final Direccion d = em.find(Direccion.class, direccionId);
                Constants.print(d);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
