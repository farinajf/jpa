/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.advanced;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.Direccion2;
import es.my.model.entities.advanced.Provincia2;
import es.my.model.entities.advanced.Usuario2;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 * Componentes anidados.
 *
 * @author fran
 */
public class ComponentesAnidados extends JPATest {

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
    public void configurePU() throws Exception {configurePU("myAdvancedPUnit");}

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
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Provincia2 p = new Provincia2();
                final Direccion2 d = new Direccion2();
                final Usuario2   u = new Usuario2();

                u.setDireccion(d);
                d.setCalle    ("Miguel de Cervantes");
                d.setProvincia(p);
                p.setNombre   ("Vigo");
                p.setZipCode  ("12345");
                p.setPais     (Locale.GERMANY.getCountry());

                em.persist(u);

                tx.commit();
                em.close();

                id = u.getId();
            }
            {
                tx.begin();

                final EntityManager em = _JPA.createEntityManager();

                final Usuario2 x = em.find(Usuario2.class, id);

                Constants.print(x);

                tx.commit();
                em.close();
            }
        }
        finally {_TM.rollback();}
    }
}
