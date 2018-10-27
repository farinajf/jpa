/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.advanced;

import es.my.jph.env.JPATest;
import es.my.model.Constants;
import es.my.model.entities.advanced.Item2;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
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

                byte[] bytes = new byte[131072];
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
}
