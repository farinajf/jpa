/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.filtering;

import es.my.jph.env.JPATest;
import es.my.model.entities.filtering.callback.Item;
import es.my.model.entities.filtering.callback.Usuario;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.testng.annotations.Test;

/**
 *
 * @author fran
 */
public class Callback extends JPATest {

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
    public void configurePU() throws Exception {super.configurePU("myFilteringCallbackPUnit");}

    /**
     *
     * @throws Throwable
     */
    @Test
    public void test() throws Throwable {
        final UserTransaction tx = _TM.getUserTransaction();

        try
        {
            tx.begin();

            final EntityManager em = _JPA.createEntityManager();

            {
                final Usuario u = new Usuario("u1");

                System.out.println("............. PERSIST .............");
                em.persist(u);

                System.out.println("............. ANTES FLUSH .............");
                em.flush();
                // Ejecuta @PostPersist: Usuario.notifyAdmin()
                System.out.println("........... DESPUES FLUSH .............");

                final Item i = new Item("I-1", u);
                em.persist(i);

                System.out.println("............. ANTES FLUSH .............");
                em.flush();
                // Ejecuta @PostPersist: PersistEntityListener.notifyAdmin()
                System.out.println("........... DESPUES FLUSH .............");

            }

            em.clear();
            tx.commit();
        }
        finally {_TM.rollback();}
    }
}
